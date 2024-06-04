package rocks.keyless.app.android.data

data class Commands(
    val mode: String
)

data class UpdateDeviceRequest(
    val commands: Commands
)
