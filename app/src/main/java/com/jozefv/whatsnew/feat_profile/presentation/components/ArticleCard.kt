package com.jozefv.whatsnew.feat_profile.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.list.ArticleInfoRowSection
import com.jozefv.whatsnew.core.presentation.components.chips.CustomInfoChip
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.paddingM
import com.jozefv.whatsnew.feat_profile.presentation.model.ArticleLocalUi
import com.jozefv.whatsnew.feat_profile.presentation.model.previewArticleLocal

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    showCategoryChips: Boolean = true,
    articleLocalUi: ArticleLocalUi,
    onClick: () -> Unit
) {
    val customStyle = CustomStyle.Text
    var infoBoxHeight by remember {
        mutableIntStateOf(0)
    }
    Card(
        modifier = modifier
            .height(400.dp)
            .clickable { onClick() },
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium,
        content = {
            Box {
                SubcomposeAsyncImage(
                    modifier = modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentScale = ContentScale.FillBounds,
                    model = articleLocalUi.image,
                    contentDescription = null,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                    },
                    error = {
                        Image(
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(id = R.drawable.default_news_image),
                            contentDescription = stringResource(id = R.string.defualt_news_image)
                        )
                    }
                )
                if (showCategoryChips) {
                    Box(
                        modifier = Modifier
                            .horizontalPaddingM()
                            .onSizeChanged { infoBoxHeight = it.height }
                            .align(Alignment.BottomEnd)
                            // Center info box according to its height
                            .offset { IntOffset(x = 0, y = infoBoxHeight / 2) }) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            articleLocalUi.filters?.let { filters ->
                                // Take only first 2 elements
                                repeat(2) {
                                    CustomInfoChip(text = filters[it])
                                }
                            }
                        }
                    }
                }
            }
            SpacerVerM()
            // Weight 1f, take rest of the space
            Column(
                modifier = Modifier
                    .paddingM()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = customStyle.CardTitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = articleLocalUi.title
                        ?: stringResource(id = R.string.title_wasnt_provided)
                )
                SpacerVerM()
                Text(
                    modifier = Modifier.weight(1f),
                    style = customStyle.CardDescription.copy(color = MaterialTheme.colorScheme.onSurface.lessImportant()),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    text = articleLocalUi.description
                        ?: stringResource(id = R.string.description_wasnt_provided)
                )
                SpacerVerL()
                ArticleInfoRowSection(
                    modifier = Modifier.fillMaxWidth(),
                    leftText = "${stringResource(id = R.string.published_at)} ${articleLocalUi.publishedDate}",
                    rightText = "${stringResource(id = R.string.source)} ${articleLocalUi.sourceName}"
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun NewsCardPreview() {
    WhatsNewTheme {
        ArticleCard(
            articleLocalUi = previewArticleLocal,
            showCategoryChips = true,
            onClick = {}
        )
    }
}