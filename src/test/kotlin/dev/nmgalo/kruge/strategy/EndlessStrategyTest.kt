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
internal class EndlessStrategyTest {

    private fun TestScope.createKruge() = Kruge.init<Any>(
        coroutineScope = this,
        pollingStrategy = EndlessStrategy(),
        pollInterval = 5_000
    )

    @Test
    fun `should start polling and return correct state`() = runTest {

        val testScope = TestScope()
        val kruge = testScope.createKruge()

        var counter = 1
        kruge.poll {
            counter++
        }

        assertThat(kruge.isPolling()).isTrue()
        assertThat(kruge.canPoll()).isTrue()
        testScope.advanceTimeBy(1_000)
        assertThat(kruge.state.value).isEqualTo(PollingState.Polling(1))
    }

    @Test
    fun `should update polling state with result overtime`() {

        val testScope = TestScope()

        val kruge = testScope.createKruge()

        var counter = 1
        kruge.poll {
            counter++
        }

        testScope.advanceTimeBy(5_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Polling(1))

        testScope.advanceTimeBy(15_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Polling(4))
    }
}
