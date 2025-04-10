package com.jozefv.whatsnew.core.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle

@Composable
fun InfoIconRowText(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Default.Info,
    textStyle: TextStyle = CustomStyle.Text.InfoText.copy(color = MaterialTheme.colorScheme.onSurface),
    text: String
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.secondaryContainer,
            imageVector = imageVector,
            contentDescription = imageVector.name
        )
        SpacerHorM()
        Text(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(),
            style = textStyle,
            text = text
        )
    }
}

@PreviewLightDark
@Composable
private fun IconRowTextPreview() {
    WhatsNewTheme {
        InfoIconRowText(
            imageVector = Icons.Default.Info,
            text = "This is a great example of this section."
        )
    }
}