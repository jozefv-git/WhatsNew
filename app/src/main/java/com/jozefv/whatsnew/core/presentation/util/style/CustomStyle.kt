package com.jozefv.whatsnew.core.presentation.util.style

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jozefv.whatsnew.core.presentation.theme.DMSerif

object CustomStyle {
    // Based on the IconButtonToken.DisabledIconOpacity - so we will match color of our icons
    private const val LESS_PROMINENT_OPACITY = 0.38f
    private const val LESS_IMPORTANT_OPACITY = 0.75f

    fun Color.lessProminent(): Color {
        return this.copy(alpha = LESS_PROMINENT_OPACITY)
    }

    fun Color.lessImportant(): Color {
        return this.copy(alpha = LESS_IMPORTANT_OPACITY)
    }

    data class Size(
        val extraSmall: Dp = 4.dp,
        val small: Dp = 8.dp,
        val medium: Dp = 16.dp,
        val large: Dp = 24.dp,
        val extraLarge: Dp = 32.dp
    )

    object Text {
        val TitleText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
        val HeadlineText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        val DefaultText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val ErrorDefaultText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val SectionTitle = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val CardTitle = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val CardDescription = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Thin,
            fontSize = 12.sp
        )
        val CardInfoText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        val InfoChipText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        val FilterChipText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        val InfoText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        val InfoTextLessImportant = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Thin,
            fontSize = 10.sp
        )
        val SuggestionItemText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
        val InputFieldText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
        val ButtonText = TextStyle(
            fontFamily = DMSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }

    val size = Size()

    fun Modifier.horizontalPaddingXS(): Modifier {
        return this.padding(horizontal = size.extraSmall)
    }

    fun Modifier.horizontalPaddingS(): Modifier {
        return this.padding(horizontal = size.small)
    }

    fun Modifier.horizontalPaddingM(): Modifier {
        return this.padding(horizontal = size.medium)
    }

    fun Modifier.horizontalPaddingL(): Modifier {
        return this.padding(horizontal = size.large)
    }

    fun Modifier.verticalPaddingXS(): Modifier {
        return this.padding(vertical = size.extraSmall)
    }

    fun Modifier.verticalPaddingS(): Modifier {
        return this.padding(vertical = size.small)
    }

    fun Modifier.verticalPaddingM(): Modifier {
        return this.padding(vertical = size.medium)
    }

    fun Modifier.verticalPaddingL(): Modifier {
        return this.padding(vertical = size.large)
    }

    fun Modifier.paddingXS(): Modifier {
        return this.padding(size.extraSmall)
    }

    fun Modifier.paddingS(): Modifier {
        return this.padding(size.small)
    }

    fun Modifier.paddingM(): Modifier {
        return this.padding(size.medium)
    }

    fun Modifier.paddingL(): Modifier {
        return this.padding(size.large)
    }
}