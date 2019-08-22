package projekt.andromeda.client.util

/**
 * Provides basic info of an overlay.
 * @param packageName The package name of the overlay.
 * @param targetPackageName The target package name of the overlay.
 * @param state The current state of the overlay.
 * @see OverlayState.STATE_UNKNOWN
 * @see OverlayState.STATE_DISABLED
 * @see OverlayState.STATE_ENABLED
 */
class OverlayInfo internal constructor(
    val packageName: String,
    val targetPackageName: String,
    val state: Int
) {
    /**
     * Returns `true` if the overlay is enabled.
     */
    val isEnabled: Boolean
        get() = state == OverlayState.STATE_ENABLED

    /**
     * Contains overlay state values.
     */
    object OverlayState {
        /**
         * When the overlay state is unknown.
         */
        const val STATE_UNKNOWN = -1
        /**
         * When the overlay is disabled.
         */
        const val STATE_DISABLED = 0
        /**
         * When the overlay is enabled.
         */
        const val STATE_ENABLED = 1
    }
}
