package rocks.keyless.app.android.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Util {

    const val BASE_URL = "https://keyless.rentlyqe.com/"
    private var accessToken = ""
    private var deviceId = ""

    // Shared Preference
    private const val APP_PREFERENCES = "wearSharedPreference"
    const val TOKEN = "token"
    const val DEVICE_ID = "deviceId"

    fun getAppPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun setAccessToken(authToken: String) {
        this.accessToken = authToken
    }

    fun getAccessToken(): String {
        return accessToken
    }

    fun setDeviceId(deviceId: String) {
        this.deviceId = deviceId
    }

    fun getDeviceId(): String {
        return deviceId
    }

    fun isNetworkAvail(ctx: Context): Boolean {
        var isConnectedToWifi = false
        var isConnectedToCellular = false
        var isConnectedToVpn = false
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isConnectedToWifi = true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isConnectedToCellular = true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                isConnectedToVpn = true
            }
        }
        return isConnectedToWifi || isConnectedToCellular || isConnectedToVpn
    }
}