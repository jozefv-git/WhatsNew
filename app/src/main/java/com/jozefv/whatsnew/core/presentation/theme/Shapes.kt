package com.jozefv.whatsnew.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle

private val size = CustomStyle.Size()
val Shapes = Shapes(
    // Components
    small = RoundedCornerShape(size.small),
    // Main cards
    medium = RoundedCornerShape(size.medium)
)