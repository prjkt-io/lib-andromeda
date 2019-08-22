package projekt.andromeda.client

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.WorkerThread
import java.util.UUID

/**
 * A class providing the client app needs to prepare for using this library.
 */
object AndromedaClient {
    private const val MIN_SERVER_VERSION = 30

    /**
     * Permission needed to access Andromeda APIs
     */
    const val ACCESS_PERMISSION = "projekt.andromeda.permission.ACCESS_API"
    private const val KEY_TOKEN_MOST_PREF = "andromeda.token_most"
    private const val KEY_TOKEN_LEAST_PREF = "andromeda.token_least"
    private val TOKEN_PROVIDER_URI = Uri.parse("content://projekt.andromeda.app.provider.TokenProvider")

    private lateinit var sContentResolver: ContentResolver
    private lateinit var sPackageManager: PackageManager
    private lateinit var sPreferences: SharedPreferences
    internal lateinit var packageName: String
        private set
    internal var token: UUID? = null
        private set

    /**
     * Returns `true` if the Andromeda server is active
     */
    val isServerActive: Boolean
        @WorkerThread
        get() = CommandInternal.isServerActive()

    internal fun isTokenInitialized() = token != null

    /**
     * Initialize the client app to use the server
     * @param context The client application context.
     * @param minVersion minimum supported version of server, defaults to 30 if not set.
     * @return `true` if minimum supported version is passed and app successfully initialized.
     */
    fun initialize(context: Context): Boolean {
        if (checkServerVersion(context, MIN_SERVER_VERSION)) {
            sContentResolver = context.contentResolver
            sPackageManager = context.packageManager
            sPreferences = context.getSharedPreferences("andromeda.token", Context.MODE_PRIVATE)
            packageName = context.packageName
            requestToken()
            return true
        }
        return false
    }

    /**
     * Returns `true` if the Andromeda server is installed.
     */
    fun doesServerExist(context: Context): Boolean {
        try {
            context.packageManager.getPackageInfo("projekt.andromeda", 0)
            return true
        } catch (ignored: Exception) {
        }
        return false
    }

    private fun checkServerVersion(context: Context, minVersion: Int): Boolean {
        try {
            val pInfo = context.packageManager.getPackageInfo("projekt.andromeda", 0)
            @Suppress("DEPRECATION")
            val vCode: Long = if (Build.VERSION.SDK_INT >= 28) {
                pInfo.longVersionCode
            } else pInfo.versionCode.toLong()

            if (vCode >= minVersion) {
                return true
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        return false
    }

    internal fun requestToken() {
        if (sPackageManager.checkPermission(ACCESS_PERMISSION, packageName) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val bundle = sContentResolver.call(TOKEN_PROVIDER_URI, "request", null, null)
            bundle?.let { setToken(it) }
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun setToken(data: Bundle) {
        val most = data.getLong(KEY_TOKEN_MOST_PREF, 0)
        val least = data.getLong(KEY_TOKEN_LEAST_PREF, 0)
        token = UUID(most, least)
    }
}
