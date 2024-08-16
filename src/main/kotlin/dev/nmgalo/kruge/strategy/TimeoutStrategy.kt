package dev.nmgalo.kruge.strategy

import dev.nmgalo.kruge.util.ElapsedPollTime
import dev.nmgalo.kruge.util.PollInterval

/**
 * ### Timeout
 *
 * Executes the polling task for a specified duration and then stops.
 */
class TimeoutStrategy(private val timeOutInMillis: Long) : KrugeStrategy {
    override fun canRun(pollInterval: PollInterval, elapsedPollTime: ElapsedPollTime): Boolean {
        return elapsedPollTime < timeOutInMillis
    }
}
