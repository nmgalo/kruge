package dev.nmgalo.kruge.strategy

import com.google.common.truth.Truth.assertThat
import dev.nmgalo.kruge.Kruge
import dev.nmgalo.kruge.PollingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
internal class TimeoutStrategyTest {

    private fun TestScope.createKruge(timeOutInMillis: Long) = Kruge.init<Any>(
        coroutineScope = this,
        pollingStrategy = TimeoutStrategy(timeOutInMillis),
        pollInterval = 1_000
    )

    @Test
    fun `should continue polling within timeout`() = runTest {

        val scope = TestScope()
        val kruge = scope.createKruge(2_000)

        var counter = 0

        kruge.poll {
            counter++
        }

        scope.advanceTimeBy(2_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Polling(1))
    }

    @Test
    fun `should stop polling after timeout`() = runTest {

        val scope = TestScope()
        val kruge = scope.createKruge(500)

        var counter = 0

        kruge.poll {
            counter++
        }

        scope.advanceTimeBy(2_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Completed)
    }
}
