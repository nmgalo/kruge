package dev.nmgalo.kruge

import dev.nmgalo.kruge.strategy.KrugeStrategy
import dev.nmgalo.kruge.util.InternalUse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

@InternalUse
interface Kruge<T> {

    /**
     * A StateFlow of [PollingState] providing real-time updates on the polling process.
     */
    val state: StateFlow<PollingState>


    /**
     * Executes the given [block] suspending function repeatedly based on the configured polling strategy.
     * Returns a [Job] representing the running poll.
     *
     * @param block The code block needs to be executed
     * @return a [Job] for the running poll
     */
    fun poll(block: suspend () -> T): Job


    /**
     * Indicates whether a polling operation is currently in progress
     */
    fun isPolling(): Boolean

    /**
     * Returns `true` if a new polling operation can be started, `false` otherwise.
     */
    fun canPoll(): Boolean

    /**
     * Terminates the current polling operation.
     */
    fun stop()

    companion object {

        /**
         * Creates a new Kruge instance with the specified [coroutineScope], [pollingStrategy], and [pollInterval]
         *
         * @param coroutineScope The scope in which the poll should execute
         * @param pollingStrategy The strategy for the poll to continue
         */
        fun <T> init(
            coroutineScope: CoroutineScope,
            pollingStrategy: KrugeStrategy,
            pollInterval: Long
        ): Kruge<T> = KrugeImpl(coroutineScope, pollingStrategy, pollInterval)
    }

}
