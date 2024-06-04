/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package rocks.keyless.app.android.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rocks.keyless.app.android.api.ApiService
import rocks.keyless.app.android.api.RetrofitClient
import rocks.keyless.app.android.data.Commands
import rocks.keyless.app.android.data.UpdateDeviceRequest
import rocks.keyless.app.android.presentation.components.Button
import rocks.keyless.app.android.presentation.components.Card
import rocks.keyless.app.android.presentation.components.Label
import rocks.keyless.app.android.presentation.components.LoadingIndicator
import rocks.keyless.app.android.presentation.theme.LocalColor
import rocks.keyless.app.android.presentation.theme.SmartHomeWearTheme
import rocks.keyless.app.android.util.Util
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            SmartHomeWearTheme {
                Dashboard()
            }
        }
    }
}

@Composable
fun Dashboard() {

    val timeOutForPlay = 1800L
    val scope = rememberCoroutineScope()
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lockOperation.json"))

    val isLocked = remember {
        mutableStateOf(true)
    }
    val isLoading = remember {
        mutableStateOf(false)
    }
    val isPlaying = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (Util.getAccessToken() != "" && Util.getDeviceId() != "") {
            isLoading.value = true
            val api = RetrofitClient.instance
            val getDeviceDetailsResponse = api.getDeviceDetails(deviceId = Util.getDeviceId())
            isLocked.value = getDeviceDetailsResponse.status.mode.type == "locked"
            isLoading.value = false
        }
    }

    suspend fun checkDeviceMode(
        api: ApiService,
        deviceId: String,
        mode: String,
        delayMillis: Long
    ): Boolean {
        delay(delayMillis)
        val deviceDetailsResponse = api.getDeviceDetails(deviceId = deviceId)
        Log.i(
            "Smart Home: GetDeviceDetailsResponse after $delayMillis ms",
            deviceDetailsResponse.toString()
        )

        return deviceDetailsResponse.status.mode.type == "${mode}ed"
    }

    suspend fun updateLock(deviceId: String, mode: String) {
        Log.i("Smart Home:", mode)
        isLoading.value = true
        val api = RetrofitClient.instance

        val response = api.updateDeviceDetails(
            deviceId = deviceId,
            deviceDetails = UpdateDeviceRequest(commands = Commands(mode = mode))
        )

        Log.i("Smart Home: UpdateDeviceDetailsResponse", response.toString())

        if (response.success) {
            repeat(3) {
                Log.i("Harish","times")
                if (checkDeviceMode(api, deviceId, mode, 5000)) {
                    isLoading.value = false
                    isPlaying.value = true
                    isLocked.value = mode == "lock"
                    delay(timeOutForPlay)
                    isPlaying.value = false
                    Log.i("Smart Home: GetDeviceDetailsResponse", "Mode changed successfully")
                    return
                }
            }
            isLoading.value = false
            Log.i("Smart Home: GetDeviceDetailsResponse", "Mode not changed within 15 seconds")
        } else {
            isLoading.value = false
            Log.i(
                "Smart Home: UpdateDeviceDetailsResponse",
                "Failed to update device details"
            )
        }
    }

    Scaffold(
        timeText = {
            TimeText(
                timeTextStyle = TextStyle(
                    color = LocalColor.Monochrome.Black,
                    fontWeight = FontWeight(600)
                )
            )
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }
    ) {
        Box {
            if (isLoading.value) {
                LoadingIndicator()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalColor.Monochrome.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    id = "",
                    modifier = Modifier
                        .size(100.dp),
                    cornerRadius = 50.dp,
                    shadowDensity = 5.dp,
                    disabled = false,
                    onClick = {
                        if (!isLocked.value) {
                            scope.launch {
                                updateLock(
                                    deviceId = Util.getDeviceId(),
                                    mode = "lock"
                                )
                            }
                        } else {
                            scope.launch {
                                updateLock(
                                    deviceId = Util.getDeviceId(),
                                    mode = "unlock"
                                )
                            }
                        }
                    }
                ) {
                    LottieAnimation(
                        composition = composition,
                        isPlaying = isPlaying.value,
                        restartOnPlay = false,
                        reverseOnRepeat = true,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(1.4f),
                        alignment = Alignment.Center,
                    )
                }

                Label(
                    modifier = Modifier
                        .padding(vertical = 5.dp),
                    title = if (!isLocked.value) {
                        "Tap to Lock"
                    } else {
                        "Tap to Unlock"
                    },
                    primary = isLocked.value,
                    danger = !isLocked.value,
                    semiBold = true,
                    s = true
                )

//                Card(id = "armDisarm") {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Button(
//                            id = "armButton",
//                            title = "Arm",
//                            modifier = Modifier
//                                .width(45.dp)
//                                .height(25.dp),
//                            inverse = true,
//                            primary = true,
//                            medium = true,
//                            xl18 = true,
//                            onClick = {
//
//                            }
//                        )
//                        Button(
//                            id = "disarmButton",
//                            title = "Disarm",
//                            modifier = Modifier
//                                .width(45.dp)
//                                .height(25.dp),
//                            dangerPrimary = true,
//                            medium = true,
//                            xl18 = true,
//                            onClick = {
//
//                            }
//                        )
//                    }
//                }
            }
        }
    }
}

//
//fun change() {
//    scope.launch {
//        isLoading.value = false
//        isPlaying.value = true
//        isLocked.value = !isLocked.value
//        delay(1800)
//        isPlaying.value = false
//    }
//}
//


//@Composable
//fun Extra() {
//    Label(
//        modifier = Modifier
//            .padding(vertical = 10.dp),
//        title = if (isLocked.value) {
//            "Locked"
//        } else {
//            "Unlocked"
//        },
//        primary = isLocked.value,
//        danger = !isLocked.value,
//        semiBold = true,
//        xl20 = true
//    )
//
//    SwipeButton(isLocked = isLocked.value) {
//        if (it == SwipeDirection.RIGHT) {
//            Log.i("SwipeButton", "Performed lock")
//            scope.launch {
//                updateLock(
//                    deviceId = Util.getDeviceId(),
//                    mode = "lock"
//                )
//            }
//        } else {
//            Log.i("SwipeButton", "Performed unlock")
//            scope.launch {
//                updateLock(
//                    deviceId = Util.getDeviceId(),
//                    mode = "unlock"
//                )
//            }
//        }
//    }
//}