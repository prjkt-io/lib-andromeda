package projekt.andromeda.client

import android.net.TrafficStats
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import projekt.andromeda.client.exception.ServerException
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException

internal object CommandInternal {
    private const val SOCKET = 13212
    private const val THREAD_ID = 10000
    private const val MAX_RETRY_COUNTS = 10

    private const val VERIFICATION_AUTHORIZED = 0

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(VOID, COMMAND, VERIFY)
    annotation class MessageType

    const val VOID = 0
    const val COMMAND = 1
    const val VERIFY = 2

    const val VOID_RETURN_PASS = "pass"

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(EXCEPTION_NONE, EXCEPTION_SECURITY, EXCEPTION_COMMAND)
    annotation class ExceptionCode

    const val EXCEPTION_NONE = 0
    const val EXCEPTION_SECURITY = -1
    const val EXCEPTION_COMMAND = -2

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(DEFAULT, APP_OPS, DEVICE_IDLE, OVERLAY, PACKAGE_MANAGER, STATUS_BAR, WINDOW_MANAGER, INPUT, SCREEN_CAPTURE)
    annotation class CommandService

    const val DEFAULT = "def" // does nothing
    const val APP_OPS = "app_ops"
    const val DEVICE_IDLE = "device_idle"
    const val INPUT = "input"
    const val MEDIA = "media"
    const val OVERLAY = "overlay"
    const val PACKAGE_MANAGER = "package_manager"
    const val SETTINGS = "settingsz"
    const val STATUS_BAR = "status_bar"
    const val WINDOW_MANAGER = "window_manager"
    const val SCREEN_CAPTURE = "screen_capture"

    @Throws(SecurityException::class, ServerException::class)
    fun run(
        @MessageType type: Int,
        @CommandService service: String?,
        command: String?,
        args: List<String>?
    ): String {
        return run(type, service, command, args, 1)
    }

    @Throws(SecurityException::class, ServerException::class)
    private fun run(
        @MessageType type: Int,
        @CommandService service: String?,
        command: String?,
        args: List<String>?,
        attemptCount: Int
    ): String {
        return runBlocking(Dispatchers.IO) {
            // Token preparation
            if (!AndromedaClient.isTokenInitialized()) {
                var i = 0
                while (i < MAX_RETRY_COUNTS && !AndromedaClient.isTokenInitialized()) {
                    AndromedaClient.requestToken()
                    i++
                }

                // If still not initialized, probably forgot to get permission
                if (!AndromedaClient.isTokenInitialized()) {
                    throw SecurityException("${AndromedaClient.packageName} " +
                            "does not have permission to access Andromeda")
                }
            }

            var i = 0
            while (i < MAX_RETRY_COUNTS && !verifyToken()) {
                AndromedaClient.requestToken()
                i++
            }

            // If still not verified, probably forgot to get permission
            if (!verifyToken()) {
                throw SecurityException("${AndromedaClient.packageName} " +
                        "does not have permission to access Andromeda")
            }

            TrafficStats.setThreadStatsTag(THREAD_ID)
            AndromedaClient.token?.let {
                try {
                    Socket(InetAddress.getLoopbackAddress(), SOCKET).use { socket ->
                        DataOutputStream(socket.getOutputStream()).use { dOut ->
                            DataInputStream(socket.getInputStream()).use { dIn ->
                                /**
                                 * Push input to server, example:
                                 * HELO
                                 * SERVICE:overlay
                                 * COMMAND:enable
                                 * ARGUMENTS:2
                                 * android.luvie
                                 * com.android.settings.luvie
                                 * BYE
                                 */
                                dOut.writeInt(type)
                                dOut.writeLong(it.mostSignificantBits)
                                dOut.writeLong(it.leastSignificantBits)
                                if (type == COMMAND) {
                                    dOut.writeUTF("HELO") // Ik it's a typo but it's intentional
                                    service?.let { dOut.writeUTF("SERVICE:$service") }
                                    command?.let { dOut.writeUTF("COMMAND:$command") }
                                    args?.let {
                                        dOut.writeUTF("ARGUMENTS:${it.size}")
                                        it.forEach { arg -> dOut.writeUTF(arg) }
                                    }
                                    dOut.writeUTF("BYE")
                                }
                                dOut.flush()

                                // Read output
                                when (dIn.readInt()) {
                                    EXCEPTION_COMMAND -> throw ServerException(dIn.readUTF())
                                    EXCEPTION_SECURITY ->
                                        throw SecurityException("Client not authorized.")
                                    else -> if (type == VOID) {
                                        return@runBlocking VOID_RETURN_PASS
                                    } else {
                                        return@runBlocking dIn.readUTF()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: SocketException) {
                    // When doing a quick operation and server decided to derp, retry
                    if (attemptCount <= MAX_RETRY_COUNTS) {
                        return@runBlocking run(type, service, command, args, attemptCount + 1)
                    }
                } catch (ignored: IOException) {
                }
            }
            return@runBlocking "null"
        }
    }

    private fun verifyToken(): Boolean {
        TrafficStats.setThreadStatsTag(THREAD_ID)
        AndromedaClient.token?.let {
            try {
                Socket(InetAddress.getLoopbackAddress(), SOCKET).use { socket ->
                    DataOutputStream(socket.getOutputStream()).use { dOut ->
                        DataInputStream(socket.getInputStream()).use { dIn ->
                            dOut.writeInt(VERIFY)
                            dOut.writeLong(it.mostSignificantBits)
                            dOut.writeLong(it.leastSignificantBits)
                            dOut.flush()
                            val result = dIn.readInt()
                            return result == VERIFICATION_AUTHORIZED
                        }
                    }
                }
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    internal fun isServerActive(): Boolean {
        return runBlocking(Dispatchers.IO) {
            TrafficStats.setThreadStatsTag(THREAD_ID)
            try {
                Socket(InetAddress.getLoopbackAddress(), SOCKET).use { socket ->
                    DataOutputStream(socket.getOutputStream()).use { dOut ->
                        dOut.writeInt(VOID)
                        dOut.flush()
                        return@runBlocking true
                    }
                }
            } catch (e: IOException) {
                return@runBlocking false
            }
        }
    }
}
