package com.jozefv.whatsnew.core.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class IconTextButton(
    val isEnabled: Boolean = true,
    val unavailable: Boolean = false,
    val isLoading: Boolean = false,
    val icon: ImageVector,
    val text: String,
    val onAction: () -> Unit
)

