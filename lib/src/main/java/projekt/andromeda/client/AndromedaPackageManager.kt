package projekt.andromeda.client

import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.PACKAGE_MANAGER
import projekt.andromeda.client.CommandInternal.run

/**
 * The AndromedaPackageManager class provides utilities with packages on the system.
 */
object AndromedaPackageManager {

    private const val INSTALL = "install"
    private const val UNINSTALL = "uninstall"
    private const val ENABLE = "enable"
    private const val DISABLE = "disable"
    private const val GRANT = "grant"
    private const val REVOKE = "revoke"

    /**
     * Set flag to forward lock application.
     * @see install
     */
    const val INSTALL_WITH_FORWARD_LOCK = 1
    /**
     * Set flag to reinstall/replace existing application.
     * @see install
     */
    const val INSTALL_REINSTALL_APP = 2
    /**
     * Set flag to allow test packages.
     * @see install
     */
    const val INSTALL_ALLOW_TEST = 4
    /**
     * Set flag to allow version code downgrade (debuggable packages only).
     * @see install
     */
    const val INSTALL_ALLOW_DOWNGRADE = 8
    /**
     * Set flag to grant all runtime permissions upon install.
     * @see install
     */
    const val INSTALL_GRANT_PERMISSIONS = 16

    /**
     * Set flag to keep data and cache directories after package removal.
     * @see uninstall
     */
    const val UNINSTALL_KEEP_DATA = 1

    /**
     * Installs a single APK.
     * @param path Path to APK.
     * @param flags Flags for install options.
     * @see INSTALL_WITH_FORWARD_LOCK
     * @see INSTALL_REINSTALL_APP
     * @see INSTALL_ALLOW_TEST
     * @see INSTALL_ALLOW_DOWNGRADE
     * @see INSTALL_GRANT_PERMISSIONS
     */
    fun install(path: String, flags: Int) {
        run(COMMAND, PACKAGE_MANAGER, INSTALL,
                listOf(flags.toString(), path, AndromedaClient.packageName))
    }

    /**
     * Removes a package from the system.
     * @param packageName The application package name to remove.
     * @param flags Flags for uninstall options
     * @see UNINSTALL_KEEP_DATA
     */
    fun uninstall(packageName: String, flags: Int) {
        run(COMMAND, PACKAGE_MANAGER, UNINSTALL, listOf(flags.toString(), packageName))
    }

    /**
     * Enables the given package or component.
     * @param packageOrComponentName package or component name to enable.
     */
    fun enable(packageOrComponentName: String) {
        run(COMMAND, PACKAGE_MANAGER, ENABLE, listOf(packageOrComponentName))
    }

    /**
     * Disables the given package or component.
     * @param packageOrComponentName package or component name to disable.
     */
    fun disable(packageOrComponentName: String) {
        run(COMMAND, PACKAGE_MANAGER, DISABLE, listOf(packageOrComponentName))
    }

    /**
     * Grants permissions to an app. The permissions must be declared
     * as used in the app's manifest, be runtime permissions
     * (protection level dangerous), and the app targeting SDK greater
     * than Lollipop MR1.
     * @param packageName The application package name.
     * @param permissions The permission to be grant.
     */
    fun grant(packageName: String, permissions: String) {
        run(COMMAND, PACKAGE_MANAGER, GRANT, listOf(packageName, permissions))
    }

    /**
     * Revokes permissions to an app. The permissions must be declared
     * as used in the app's manifest, be runtime permissions
     * (protection level dangerous), and the app targeting SDK greater
     * than Lollipop MR1.
     * @param packageName The application package name.
     * @param permissions The permission to be revoke.
     */
    fun revoke(packageName: String, permissions: String) {
        run(COMMAND, PACKAGE_MANAGER, REVOKE, listOf(packageName, permissions))
    }
}
