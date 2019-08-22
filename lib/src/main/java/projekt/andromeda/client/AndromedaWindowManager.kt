package projekt.andromeda.client

import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.WINDOW_MANAGER
import java.util.ArrayList

/**
 * The AndromedaWindowManager provides control to WindowManager
 */
object AndromedaWindowManager {
    /**
     * Overrides display density.
     * @param value The density number to set.
     */
    fun setDensity(value: Int) {
        val args = ArrayList<String>()
        args.add(value.toString())
        CommandInternal.run(COMMAND, WINDOW_MANAGER, "density", args)
    }

    /**
     * Resets the display density number to system default.
     */
    fun resetDensity() {
        val args = ArrayList<String>()
        args.add("-1")
        CommandInternal.run(COMMAND, WINDOW_MANAGER, "density", args)
    }
}
