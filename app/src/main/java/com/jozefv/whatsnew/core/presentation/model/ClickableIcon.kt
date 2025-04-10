package com.jozefv.whatsnew.core.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class ClickableIcon(
    val enabled: Boolean = true,
    val icon: ImageVector,
    val onAction: () -> Unit
)
