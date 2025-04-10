@file:OptIn(ExperimentalMaterial3Api::class)

package com.jozefv.whatsnew.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL

@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topAppBar,
        snackbarHost = snackBarHost
    ) { padding ->
        Box(
            modifier = Modifier
                .horizontalPaddingM()
                .verticalPaddingL()
        ){
            content(padding)
        }
    }
}

@PreviewLightDark
@Composable
private fun CustomScaffoldPreview() {
    CustomScaffold(
        topAppBar = {
            CustomToolBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = "Custom toolbar"
            )
        }
    ) {
        // Possible content
    }
}