package rocks.keyless.app.android.data

data class AutoLockConfig(
    val auto_lock_enabled: Boolean,
    val auto_lock_timer: Any,
    val is_auto_lock_supported: Boolean,
    val reported: Reported,
    val supported_timers: List<Int>
)