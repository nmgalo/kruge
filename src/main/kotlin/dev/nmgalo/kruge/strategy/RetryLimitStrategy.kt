package dev.nmgalo.kruge.strategy

import dev.nmgalo.kruge.util.ElapsedPollTime
import dev.nmgalo.kruge.util.PollInterval

/**
 * ### Retry Limit
 *
 * Executes the polling task until a specified number of retries is reached or a successful result is obtained.
 */
class RetryLimitStrategy(private val maxRetries: Int) : KrugeStrategy {
    override fun canRun(pollInterval: PollInterval, elapsedPollTime: ElapsedPollTime): Boolean {
        return (elapsedPollTime / pollInterval) < maxRetries
    }
}
