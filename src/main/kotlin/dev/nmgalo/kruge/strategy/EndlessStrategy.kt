package dev.nmgalo.kruge.strategy

import dev.nmgalo.kruge.util.ElapsedPollTime
import dev.nmgalo.kruge.util.PollInterval

/**
 * ### Endless run
 *
 * Repeatedly executes the polling task indefinitely until manually stopped.
 */
class EndlessStrategy : KrugeStrategy {
    override fun canRun(pollInterval: PollInterval, elapsedPollTime: ElapsedPollTime): Boolean = true
}
