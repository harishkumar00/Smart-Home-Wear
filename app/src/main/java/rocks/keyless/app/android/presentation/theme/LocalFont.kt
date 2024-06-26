package rocks.keyless.app.android.presentation.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import rocks.keyless.app.android.R

object LocalFont {
    object FontSize {
        val XS = 10.sp
        val S = 12.sp
        val M = 14.sp
        val L = 16.sp
        val XL18 = 18.sp
        val XL20 = 20.sp
        val XL22 = 22.sp
        val XL24 = 24.sp
        val XL26 = 26.sp
        val XL28 = 28.sp
        val XL30 = 30.sp
        val XL34 = 34.sp
        val XL40 = 40.sp
        val XL50 = 50.sp
        val XL80 = 80.sp
        val XL200 = 200.sp
    }

    object FontFamily {
        val light = FontFamily(Font(R.font.inter_light))
        val normal = FontFamily(Font(R.font.inter))
        val medium = FontFamily(Font(R.font.inter_medium))
        val semiBold = FontFamily(Font(R.font.inter_semibold))
        val bold = FontFamily(Font(R.font.inter_bold))
    }
}