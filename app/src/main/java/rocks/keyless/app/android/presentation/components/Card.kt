package rocks.keyless.app.android.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import rocks.keyless.app.android.presentation.theme.LocalColor
import androidx.compose.material3.Card as MaterialCard

@Composable
fun Card(
    modifier: Modifier = Modifier,
    id: String,
    heapData: Any = "",
    backgroundColor: Color = LocalColor.Monochrome.White,
    disabled: Boolean = false,
    cornerRadius: Dp = 10.dp,
    shadowDensity: Dp = 1.dp,
    onClick: () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit)
) {

    val clickModifier: Modifier = if (disabled) {
        Modifier
    } else {
        Modifier
            .clickable(
                enabled = true,
                onClick = {
                    onClick()
                }
            )
    }

    MaterialCard(
        modifier = Modifier
            .semantics { contentDescription = id }
            .then(modifier)
            .shadow(shadowDensity, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .then(clickModifier),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f)
        ),
        content = content,
    )
}