package rocks.keyless.app.android.data

data class GetLockDetailsResponse(
    val auto_lock_config: AutoLockConfig,
    val battery_updated_at: Int,
    val device_name: String,
    val device_type: String,
    val id: String,
    val iot_thing_name: String,
    val last_activity: Int,
    val location: Any,
    val log_mindate: String,
    val manufacturer: String,
    val model_number: String,
    val occupant_setting: String,
    val power_source: String,
    val product_name: String,
    val remote_device_id: String,
    val settings: Settings,
    val shared_area: Boolean,
    val status: Status,
    val topic_name: String,
    val two_way_power_source: Boolean,
    val zwave_security: String
)