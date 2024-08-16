package dev.nmgalo.kruge

import dev.nmgalo.kruge.util.InternalUse


/**
 * [PollingState] is a sealed interface representing the different states a polling operation can be in.
 */
@InternalUse
sealed interface PollingState {

    /**
     * The polling is currently inactive.
     **/
    data object Idle : PollingState

    /**
     * The polling is in progress, and the current result is available as R
     */
    data class Polling<R>(val result: R) : PollingState

    /**
     * The polling has been explicitly stopped.
     */
    data object Terminated : PollingState

    /**
     * The polling has finished naturally (e.g., due to a timeout or retry limit).
     */
    data object Completed : PollingState

}
