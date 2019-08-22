package projekt.andromeda.client

import projekt.andromeda.client.CommandInternal.COMMAND
import projekt.andromeda.client.CommandInternal.DEVICE_IDLE
import projekt.andromeda.client.exception.ServerException
import java.util.ArrayList
import java.util.Arrays

/**
 * Device idle controller (deviceidle)
 */
object AndromedaDeviceIdle {
    private const val FORCE_IDLE = "force-idle"
    private const val FORCE_INACTIVE = "force-inactive"
    private const val STEP = "step"
    private const val UNFORCE = "unforce"
    private const val GET_WHITELIST = "get_whitelist"
    private const val WHITELIST = "whitelist"

    /**
     * Force directly into idle mode, regardless of other device state.
     * @param state Idle state to go, `deep` or `light`.
     */
    fun forceIdle(state: String = "deep"): Boolean {
        val args = ArrayList<String>()
        args.add(state)
        try {
            CommandInternal.run(COMMAND, DEVICE_IDLE, FORCE_IDLE, args)
        } catch (e: ServerException) {
            return false
        }
        return true
    }

    /**
     * Force to be inactive, ready to freely step idle states.
     */
    fun forceInactive() {
        try {
            CommandInternal.run(COMMAND, DEVICE_IDLE, FORCE_INACTIVE, ArrayList())
        } catch (ignored: ServerException) {
        }
    }

    /**
     * Resume normal functioning after force-idle or force-inactive.
     */
    fun unforce() {
        try {
            CommandInternal.run(COMMAND, DEVICE_IDLE, UNFORCE, ArrayList())
        } catch (ignored: ServerException) {
        }
    }

    /**
     * Immediately step to next state, without waiting for alarm.
     * @param state Idle state to go, `deep` or `light`.
     */
    fun step(state: String = "deep") {
        val args = ArrayList<String>()
        args.add(state)
        try {
            CommandInternal.run(COMMAND, DEVICE_IDLE, STEP, args)
        } catch (ignored: ServerException) {
        }
    }

    /**
     * Returns list of currently whitelisted apps.
     */
    fun getWhitelist(): List<String> {
        return CommandInternal.run(COMMAND, DEVICE_IDLE, GET_WHITELIST, ArrayList()).lines()
    }

    /**
     * Add or remove packages from whitelist
     * @param args List of package names with prefix `+` to add or prefix `-` to remove.
     */
    fun whitelist(args: MutableList<String>) {
        val legalIdentifiers = arrayOf("+", "-", "=")
        for (packageName in args) {
            val startChar = packageName.substring(0, 1)
            if (!listOf(*legalIdentifiers).contains(startChar)) {
                args.remove(packageName)
            }
        }
        CommandInternal.run(COMMAND, DEVICE_IDLE, WHITELIST, args)
    }
}
