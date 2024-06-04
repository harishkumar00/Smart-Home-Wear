package rocks.keyless.app.android.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import rocks.keyless.app.android.presentation.theme.LocalColor
import rocks.keyless.app.android.presentation.theme.LocalFont
import androidx.compose.material3.Button as MaterialButton

@Composable
fun Button(
    modifier: Modifier = Modifier,
    id: String,
    heapData: Any = "",
    textModifier: Modifier = Modifier,
    title: String,
    // Font Size
    xs: Boolean = false,
    s: Boolean = false,
    m: Boolean = false,
    l: Boolean = false,
    xl18: Boolean = false,
    xl20: Boolean = false,
    xl22: Boolean = false,
    xl24: Boolean = false,
    // Color
    primary: Boolean = false,
    primarySemiDark: Boolean = false,
    dangerPrimary: Boolean = false,
    dangerSecondary: Boolean = false,
    black: Boolean = false,
    white: Boolean = false,
    inverse: Boolean = false,
    // Font Weight
    light: Boolean = false,
    medium: Boolean = false,
    bold: Boolean = false,
    cornerRadius: Dp = 5.dp,
    disabled: Boolean = false,
    elevation: Boolean = true,
    onClick: () -> Unit = {}
) {

    var buttonColor = when {
        primary -> LocalColor.Monochrome.Light
        primarySemiDark -> LocalColor.Primary.SemiDark
        dangerPrimary -> LocalColor.Danger.Medium
        dangerSecondary -> LocalColor.Danger.Dark
        black -> LocalColor.Monochrome.Black
        white -> LocalColor.Monochrome.Light
        else -> LocalColor.Monochrome.Light
    }

    var textColor = when {
        primary -> LocalColor.Primary.Dark
        primarySemiDark -> LocalColor.Monochrome.White
        dangerPrimary -> LocalColor.Monochrome.White
        dangerSecondary -> LocalColor.Monochrome.White
        black -> LocalColor.Monochrome.White
        white -> LocalColor.Primary.Dark
        else -> LocalColor.Primary.Dark
    }

    if (inverse) {
        // Swap
        buttonColor = textColor.also { textColor = buttonColor }
    }

    val borderModifier = if (!inverse) Modifier.border(
        Dp.Hairline,
        textColor,
        RoundedCornerShape(cornerRadius)
    ) else Modifier

    val fontFamily = when {
        light -> LocalFont.FontFamily.light
        medium -> LocalFont.FontFamily.medium
        bold -> LocalFont.FontFamily.bold
        else -> LocalFont.FontFamily.normal
    }

    val fontSize = when {
        xs -> LocalFont.FontSize.XS
        s -> LocalFont.FontSize.S
        m -> LocalFont.FontSize.M
        l -> LocalFont.FontSize.L
        xl18 -> LocalFont.FontSize.XL18
        xl20 -> LocalFont.FontSize.XL20
        xl22 -> LocalFont.FontSize.XL22
        xl24 -> LocalFont.FontSize.XL24
        else -> LocalFont.FontSize.L
    }

    MaterialButton(
        modifier = Modifier
            .semantics { contentDescription = "${id}Button" }
            .then(borderModifier)
            .then(modifier),
        enabled = !disabled,
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            disabledContainerColor = buttonColor.copy(alpha = 0.5f)
        ),
        elevation = when {
            elevation -> ButtonDefaults.elevatedButtonElevation()
            else -> null
        }
    ) {
        Text(
            text = title,
            fontSize = fontSize,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier
                .semantics { contentDescription = "${id}Text" }
                .then(textModifier)
        )
    }
}