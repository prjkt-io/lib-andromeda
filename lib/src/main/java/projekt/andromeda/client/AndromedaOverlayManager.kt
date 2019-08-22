package projekt.andromeda.client

import android.os.Build
import androidx.annotation.RequiresApi
import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.OVERLAY
import projekt.andromeda.client.CommandInternal.run
import projekt.andromeda.client.util.OverlayInfo
import projekt.andromeda.client.util.OverlayInfo.OverlayState.STATE_DISABLED
import projekt.andromeda.client.util.OverlayInfo.OverlayState.STATE_ENABLED
import projekt.andromeda.client.util.OverlayInfo.OverlayState.STATE_UNKNOWN
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

/**
 * The AndromedaOverlayManager class provides controls to the device overlay.
 */
@RequiresApi(Build.VERSION_CODES.O)
object AndromedaOverlayManager {
    private const val ENABLED_PREFIX = "[x]"
    private const val DISABLED_PREFIX = "[ ]"
    private const val UNKNOWN_PREFIX = "---"

    private val bareOverlaysState: List<String>
        get() = listOf(*run(
                COMMAND,
                OVERLAY,
                "list",
                null
            ).split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

    /**
     * Returns map of overlay package name and the state of said overlay.
     * @see STATE_ENABLED
     * @see STATE_DISABLED
     * @see STATE_UNKNOWN
     */
    val overlayState: Map<String, Int>
        get() {
            val outList = bareOverlaysState
            val out = HashMap<String, Int>()
            for (line in outList) {
                when {
                    line.startsWith(ENABLED_PREFIX) -> out[line.substring(4)] = STATE_ENABLED
                    line.startsWith(DISABLED_PREFIX) -> out[line.substring(4)] = STATE_DISABLED
                    line.startsWith(UNKNOWN_PREFIX) -> out[line.substring(4)] = STATE_UNKNOWN
                }
            }
            return out
        }

    /**
     * Returns map of overlay package name and the [OverlayInfo] of said overlay.
     * @see OverlayInfo
     * @see STATE_ENABLED
     * @see STATE_DISABLED
     * @see STATE_UNKNOWN
     */
    val allOverlay: Map<String, List<OverlayInfo>>
        get() {
            val out = HashMap<String, List<OverlayInfo>>()
            var currentTarget = ""
            var currentTargetOverlays: MutableList<OverlayInfo> = ArrayList()
            val outList = AndromedaOverlayManager.bareOverlaysState
            for ((index, line) in outList.withIndex()) {
                val overlayLine = line.startsWith(ENABLED_PREFIX) ||
                        line.startsWith(DISABLED_PREFIX) ||
                        line.startsWith(UNKNOWN_PREFIX)
                if (overlayLine) {
                    val state: Int = when (line.substring(0, 3)) {
                        DISABLED_PREFIX -> STATE_DISABLED
                        ENABLED_PREFIX -> STATE_ENABLED
                        else -> STATE_UNKNOWN
                    }
                    currentTargetOverlays.add(OverlayInfo(line.substring(4), currentTarget, state))
                }
                if (line.isNotEmpty() && (!overlayLine || index == outList.lastIndex)) {
                    if (currentTargetOverlays.isNotEmpty()) {
                        out[currentTarget] = currentTargetOverlays
                    }
                    currentTarget = line
                    currentTargetOverlays = ArrayList()
                }
            }
            return out
        }

    /**
     * Switches list of overlays on or off.
     * @param overlays List of overlay package names to switch state.
     * @param enabled Set to `true` to enable the overlay and `false` to disable.
     */
    fun switchOverlay(overlays: List<String>, enabled: Boolean) {
        run(COMMAND, OVERLAY, if (enabled) "enable" else "disable", overlays)
    }

    /**
     * Change the priority of the overlays
     * @param overlays List of overlay package names sorted from lower to higher priority.
     */
    fun setPriority(overlays: List<String>) {
        run(COMMAND, OVERLAY, "set_priority", overlays)
    }
}
