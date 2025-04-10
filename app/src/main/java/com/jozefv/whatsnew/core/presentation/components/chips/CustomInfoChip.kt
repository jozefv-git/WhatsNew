package com.jozefv.whatsnew.core.presentation.components.chips

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingXS

@Composable
fun CustomInfoChip(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = CustomStyle.Text.InfoChipText,
    text: String
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceTint,
                shape = MaterialTheme.shapes.small
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.horizontalPaddingM().verticalPaddingXS(),
            style = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
            text = text
        )
    }
}


@PreviewLightDark
@Composable
private fun InfoChipPreview() {
    WhatsNewTheme {
        CustomInfoChip(text = "Science")
    }
}