package dev.nmgalo.kruge

import com.google.common.truth.Truth.assertThat
import dev.nmgalo.kruge.strategy.EndlessStrategy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
internal class KrugeTest {

    private fun TestScope.createKruge() = Kruge.init<Any>(
        coroutineScope = this,
        pollingStrategy = EndlessStrategy(),
        pollInterval = 5_000
    )

    @Test
    fun `should return Idle state without polling task`() = runTest {

        val kruge = createKruge()

        assertEquals(kruge.state.value, PollingState.Idle)
    }

    @Test
    fun `should have expected initial state without tas`() = runTest {

        val kruge = createKruge()

        assertThat(kruge.isPolling()).isFalse()
        assertThat(kruge.canPoll()).isFalse()
        assertThat(kruge.state.value).isEqualTo(PollingState.Idle)
    }

    @Test
    fun `should stop polling when stop is called`() = runTest {

        val scope = TestScope()
        val kruge = scope.createKruge()

        kruge.poll {
            kruge.stop()
        }

        scope.advanceTimeBy(1_000)

        assertThat(kruge.state.value).isEqualTo(PollingState.Terminated)
    }
}
