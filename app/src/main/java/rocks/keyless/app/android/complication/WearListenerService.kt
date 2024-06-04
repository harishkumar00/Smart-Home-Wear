package rocks.keyless.app.android.complication

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import rocks.keyless.app.android.util.Util

data class MyMessage(
    @SerializedName(value = "selectedLockId")
    val selectedLockId: String,
    @SerializedName("accessToken")
    val accessToken: String
)

class WearListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.i("MyWearListenerService", "Data changed")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == "/data-response") {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val message = dataMap.getString("message")
                    Log.i("MyWearListenerService", "Received message: $message")
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        val received = Gson().fromJson(messageEvent.path, MyMessage::class.java)
        Log.i("Message Received from mobile", received.selectedLockId)
        Log.i("Message Received from mobile", received.accessToken)
        Util.setDeviceId(received.selectedLockId)
        Util.setAccessToken(received.accessToken)

//        val sharedPreferences = Util.getAppPreference(this)
//        // Get
//        sharedPreferences.getString(Util.TOKEN, "")
//        sharedPreferences.getString(Util.DEVICE_ID, "")
//
//        // Set
//        sharedPreferences.edit().putString(Util.TOKEN, received.accessToken).apply()
//        sharedPreferences.edit().putString(Util.DEVICE_ID, received.selectedLockId).apply()

    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MyWearListenerService", "Service created")
    }
}
