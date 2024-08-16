package dev.nmgalo.kruge.strategy

import dev.nmgalo.kruge.util.ElapsedPollTime
import dev.nmgalo.kruge.util.InternalUse
import dev.nmgalo.kruge.util.PollInterval

@InternalUse
interface KrugeStrategy {
    fun canRun(pollInterval: PollInterval, elapsedPollTime: ElapsedPollTime): Boolean
}
