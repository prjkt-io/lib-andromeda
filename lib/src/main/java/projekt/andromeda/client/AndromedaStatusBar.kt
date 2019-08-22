package projekt.andromeda.client

import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.STATUS_BAR

/**
 * The AndromedaStatusBar class provides control to the system's status bar.
 */
object AndromedaStatusBar {
    /**
     * Expands notifications.
     */
    fun expandNotifications() {
        CommandInternal.run(COMMAND, STATUS_BAR, "expand_notifications", null)
    }

    /**
     * Expands settings.
     */
    fun expandSettings() {
        CommandInternal.run(COMMAND, STATUS_BAR, "expand_settings", null)
    }

    /**
     * Collapses the settings and/or notifications shade.
     */
    fun collapse() {
        CommandInternal.run(COMMAND, STATUS_BAR, "collapse", null)
    }

    /**
     * Add a Quick Settings tile to the settings shade.
     * @param componentName The target app ComponentName to add.
     */
    fun addTile(componentName: String) {
        CommandInternal.run(COMMAND, STATUS_BAR, "add_tile", listOf(componentName))
    }

    /**
     * Removes a Quick Settings tile from the settings shade.
     * @param componentName The target app ComponentName to remove.
     */
    fun removeTile(componentName: String) {
        CommandInternal.run(COMMAND, STATUS_BAR, "remove_tile", listOf(componentName))
    }
}
