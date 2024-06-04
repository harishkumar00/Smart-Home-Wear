package rocks.keyless.app.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import rocks.keyless.app.android.presentation.theme.LocalColor


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = LocalColor.Primary.Dark,
    size: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1F)
            .alpha(1f)
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0x12FFFFFF),
                        Color(0xDFFFFFFF),
                        Color(0x9FFFFFFF)

                    ),
                    radius = 100f,
                    center = Offset.Infinite
                )
            )
            .pointerInteropFilter { true },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = color,
            strokeWidth = size
        )
    }
}