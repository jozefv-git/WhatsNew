package com.jozefv.whatsnew.core.presentation.components.list

import WhatsNewTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerHorL

@Composable
fun ArticleInfoRowSection(
    modifier: Modifier = Modifier,
    leftText: String = "",
    rightText: String = "",
) {
    val customStyle = CustomStyle.Text
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = customStyle.CardInfoText.copy(color = MaterialTheme.colorScheme.onBackground),
            text = leftText
        )
        SpacerHorL()
        // Takes all remaining space
        Text(
            style = customStyle.CardInfoText.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Right,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = rightText
        )
    }
}

@PreviewLightDark
@Composable
private fun NewsInfoRowSectionPreview() {
    WhatsNewTheme {
        ArticleInfoRowSection(
            leftText = "10-08-2024 10:19:00",
            rightText = "Long source New Your Times and even more"
        )
    }
}