package dev.nmgalo.kruge.strategy

import com.google.common.truth.Truth.assertThat
import dev.nmgalo.kruge.Kruge
import dev.nmgalo.kruge.PollingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class RetryLimitStrategyTest {

    private fun TestScope.createKruge(maxRetries: Int) = Kruge.init<Any>(
        coroutineScope = this,
        pollingStrategy = RetryLimitStrategy(maxRetries),
        pollInterval = 1_000
    )

    @Test
    fun `should complete after max retries reached`() = runTest {

        val scope = TestScope()
        val kruge = scope.createKruge(5)

        var counter = 1
        kruge.poll {
            counter++
        }

        scope.advanceTimeBy(5_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Polling(5))

        scope.advanceTimeBy(5_001)

        assertThat(kruge.state.value).isEqualTo(PollingState.Completed)
    }
}
