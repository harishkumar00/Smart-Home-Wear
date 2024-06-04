package rocks.keyless.app.android.data

data class Status(
    val battery: Int,
    val battery_zwave: Int,
    val codes: Codes,
    val max_slots: Int,
    val mode: Mode,
    val power_source: String,
    val zwave_signal: Int
)