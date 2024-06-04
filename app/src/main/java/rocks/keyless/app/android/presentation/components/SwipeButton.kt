package rocks.keyless.app.android.presentation.components

import android.animation.ValueAnimator
import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.RequestDisallowInterceptTouchEvent
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import rocks.keyless.app.android.R
import rocks.keyless.app.android.presentation.theme.LocalColor
import kotlin.math.abs

enum class SwipeDirection {
    LEFT,
    RIGHT
}

fun leftButtonGestureHandler(
    action: Int,
    rawX: Float,
    leftButtonInitialX: MutableFloatState,
    leftButtonX: MutableFloatState,
    leftButtonTouched: MutableState<Boolean>,
    actionThreshold: Float,
    onSwipe: (direction: SwipeDirection) -> Unit
): Boolean {
    return when (action) {
        MotionEvent.ACTION_DOWN -> {
            leftButtonInitialX.floatValue = rawX
            leftButtonTouched.value = true
            true
        }

        MotionEvent.ACTION_MOVE -> {
            var newX = rawX - leftButtonInitialX.floatValue
            if (newX < 0F) {
                newX = 0F
            }
            if (newX > actionThreshold) {
                newX = actionThreshold
            }

            leftButtonX.floatValue = newX
            true
        }

        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            if (abs(leftButtonX.floatValue) >= actionThreshold * 0.9) {
                onSwipe(SwipeDirection.RIGHT)
            }
            val animator = ValueAnimator.ofFloat(leftButtonX.floatValue, 0F)
            animator.duration = 300 //in millis
            animator.addUpdateListener { animation ->
                leftButtonX.floatValue = animation.animatedValue as Float
            }
            animator.start()
            leftButtonInitialX.floatValue = 0F
            leftButtonTouched.value = false
            true
        }

        else -> false
    }
}

fun rightButtonGestureHandler(
    action: Int,
    rawX: Float,
    rightButtonInitialX: MutableFloatState,
    rightButtonX: MutableFloatState,
    rightButtonTouched: MutableState<Boolean>,
    actionThreshold: Float,
    onSwipe: (direction: SwipeDirection) -> Unit
): Boolean {
    return when (action) {
        MotionEvent.ACTION_DOWN -> {
            rightButtonInitialX.floatValue = rawX
            rightButtonTouched.value = true
            true
        }

        MotionEvent.ACTION_MOVE -> {
            var newX = rawX - rightButtonInitialX.floatValue

            if (newX > 0F) {
                newX = 0F
            }
            if (abs(newX) > actionThreshold) {
                newX = -actionThreshold
            }

            rightButtonX.floatValue = newX
            true
        }

        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            if (abs(rightButtonX.floatValue) >= actionThreshold * 0.9) {
                onSwipe(SwipeDirection.LEFT)
            }
            val animator = ValueAnimator.ofFloat(rightButtonX.floatValue, 0F)
            animator.duration = 300 //in millis
            animator.addUpdateListener { animation ->
                rightButtonX.floatValue = animation.animatedValue as Float
            }
            animator.start()
            rightButtonInitialX.floatValue = 0F
            rightButtonTouched.value = false
            true
        }

        else -> false
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwipeButton(
    isLocked: Boolean,
    onSwipe: (direction: SwipeDirection) -> Unit
) {
    val leftButtonInitialX = remember { mutableFloatStateOf(0F) }
    val rightButtonInitialX = remember { mutableFloatStateOf(0F) }

    val leftButtonX = remember { mutableFloatStateOf(0F) }
    val rightButtonX = remember { mutableFloatStateOf(0F) }

    val leftButtonTouched = remember { mutableStateOf(false) }
    val rightButtonTouched = remember { mutableStateOf(false) }

    val height = 40.dp
    val width = 130.dp

    val actionThreshold = width.value - 2 * height.value

    Surface(
        modifier = Modifier
            .height(height)
            .width(width),
        shape = RoundedCornerShape(height),
        color = LocalColor.Primary.ExtraWhite
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val lRequestDisallowInterceptTouchEvent =
                remember { RequestDisallowInterceptTouchEvent() }

            Box(modifier = Modifier
                .semantics { contentDescription = SwipeDirection.RIGHT.name }
                .height(height)
                .width(height)
                .padding(2.dp)
                .offset(leftButtonX.floatValue.dp, 0.dp)
                .pointerInteropFilter(requestDisallowInterceptTouchEvent = lRequestDisallowInterceptTouchEvent) { event ->
                    lRequestDisallowInterceptTouchEvent.invoke(true)
                    leftButtonGestureHandler(
                        action = event.action,
                        rawX = event.rawX,
                        leftButtonInitialX = leftButtonInitialX,
                        leftButtonX = leftButtonX,
                        leftButtonTouched = leftButtonTouched,
                        actionThreshold = actionThreshold,
                        onSwipe = onSwipe
                    )
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = if (!isLocked) LocalColor.Primary.Dark else LocalColor.Primary.ExtraWhite
                ) {
                    Icon(
                        modifier = Modifier
                            .scale(if (!isLocked) 1f else 0.75F)
                            .padding(10.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.locks_state_white_lock),
                        contentDescription = "lock icon",
                        tint = if (!isLocked) LocalColor.Monochrome.White else LocalColor.Secondary.SemiDark
                    )
                }
            }
            val rRequestDisallowInterceptTouchEvent =
                remember { RequestDisallowInterceptTouchEvent() }
            Box(
                modifier = Modifier
                    .semantics { contentDescription = SwipeDirection.LEFT.name }
                    .height(height)
                    .width(height)
                    .padding(2.dp)
                    .offset(rightButtonX.floatValue.dp, 0.dp)
                    .pointerInteropFilter(requestDisallowInterceptTouchEvent = rRequestDisallowInterceptTouchEvent) { event ->
                        rRequestDisallowInterceptTouchEvent.invoke(true)
                        rightButtonGestureHandler(
                            action = event.action,
                            rawX = event.rawX,
                            rightButtonInitialX = rightButtonInitialX,
                            rightButtonX = rightButtonX,
                            rightButtonTouched = rightButtonTouched,
                            actionThreshold = actionThreshold,
                            onSwipe = onSwipe
                        )
                    }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = if (isLocked) LocalColor.Danger.Medium else LocalColor.Primary.ExtraWhite
                ) {
                    Icon(
                        modifier = Modifier
                            .scale(if (isLocked) 1f else 0.75F)
                            .padding(10.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.locks_state_white_unlock),
                        contentDescription = "unlock icon",
                        tint = if (isLocked) LocalColor.Monochrome.White else LocalColor.Secondary.SemiDark
                    )
                }
            }
        }
    }
}