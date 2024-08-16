package dev.nmgalo.kruge

import dev.nmgalo.kruge.strategy.KrugeStrategy
import dev.nmgalo.kruge.util.ElapsedPollTime
import dev.nmgalo.kruge.util.InternalUse
import dev.nmgalo.kruge.util.PollInterval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@InternalUse
class KrugeImpl<T>(
    private val coroutineScope: CoroutineScope,
    private val pollingStrategy: KrugeStrategy,
    private val pollInterval: PollInterval
) : Kruge<T> {

    private val _state: MutableStateFlow<PollingState> = MutableStateFlow(PollingState.Idle)

    override val state: StateFlow<PollingState>
        get() = _state.asStateFlow()

    private var pollingJob: Job? = null

    private var isPolling: Boolean = false

    private var elapsedPollTime: ElapsedPollTime = ELAPSED_TIME_ZERO

    override fun poll(block: suspend () -> T): Job {
        isPolling = true
        elapsedPollTime = ELAPSED_TIME_ZERO

        return coroutineScope.launch {
            while (isActive && canPoll()) {
                val result = block()
                _state.value = PollingState.Polling(result)
                elapsedPollTime += pollInterval

                delay(pollInterval)
            }

            _state.value = PollingState.Completed
        }.also {
            it.invokeOnCompletion { throwable ->
                _state.value = when (throwable) {
                    is CancellationException -> PollingState.Terminated
                    else -> PollingState.Completed
                }
                stop()
            }
            pollingJob = it
        }
    }

    override fun isPolling() = isPolling

    override fun canPoll() = isPolling && pollingStrategy.canRun(pollInterval, elapsedPollTime)

    override fun stop() {
        isPolling = false
        pollingJob?.cancel()
        pollingJob = null
    }

    companion object {
        const val ELAPSED_TIME_ZERO: ElapsedPollTime = 0L
    }
}
