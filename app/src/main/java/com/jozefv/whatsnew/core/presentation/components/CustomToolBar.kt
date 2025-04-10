@file:OptIn(ExperimentalMaterial3Api::class)

package com.jozefv.whatsnew.core.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle


@Composable
fun CustomToolBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = { Text(style = CustomStyle.Text.HeadlineText, text = title) },
        navigationIcon = { leadingContent() },
        actions = { trailingContent() }
    )
}


@PreviewLightDark
@Composable
private fun CustomToolBarPreview() {
    WhatsNewTheme {
        CustomToolBar(
            title = "Best news",
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            leadingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = Icons.AutoMirrored.Default.ArrowBack.name
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = Icons.Default.AccountCircle.name
                )
            }
        )
    }
}