package com.jozefv.whatsnew.feat_profile.presentation.components

import WhatsNewTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.list.ArticleInfoRowSection
import com.jozefv.whatsnew.core.presentation.model.IconTextButton
import com.jozefv.whatsnew.core.presentation.components.list.CardFiltersActionSection
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL
import com.jozefv.whatsnew.feat_profile.presentation.model.ArticleLocalUi
import com.jozefv.whatsnew.feat_profile.presentation.model.previewArticleLocal

@Composable
fun ArticleDetail(
    modifier: Modifier = Modifier,
    articleLocalUi: ArticleLocalUi,
    leadingIcon: IconTextButton,
    trailingIcon: IconTextButton
) {
    val customStyle = CustomStyle.Text
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .heightIn(min = 200.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
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
        Column(modifier = Modifier.fillMaxSize()) {
            SpacerVerM()
            ArticleInfoRowSection(
                modifier = Modifier.fillMaxWidth(),
                leftText = "${stringResource(id = R.string.published_at)} ${articleLocalUi.publishedDate}",
                rightText = "${stringResource(id = R.string.source)} ${articleLocalUi.sourceName}"
            )
            SpacerVerL()
            HorizontalDivider()
            SpacerVerL()
            CardFiltersActionSection(
                modifier = Modifier.fillMaxWidth(),
                clickable = articleLocalUi.filters!!.size > 3,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                filters = articleLocalUi.filters
            )
            SpacerVerL()
            HorizontalDivider()
            SpacerVerL()
            // Cannot be null as we displaying data obtained from the previous screen
            Text(style = customStyle.HeadlineText, text = articleLocalUi.title!!)
            SpacerVerM()
            Text(style = customStyle.DefaultText,
                text =
                buildAnnotatedString {
                    append(articleLocalUi.description)
                    withLink(
                        link = LinkAnnotation.Url(
                            url = articleLocalUi.articleLink,
                            TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.secondary))
                        ),
                        block = {
                            append(stringResource(id = R.string.read_more))
                        }
                    )
                }
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun NewsDetailPreview() {
    WhatsNewTheme {
        ArticleDetail(
            modifier = Modifier
                .horizontalPaddingM()
                .verticalPaddingL(),
            articleLocalUi = previewArticleLocal,
            leadingIcon = IconTextButton(icon = Icons.Default.Save, text = "Save", onAction = {}),
            trailingIcon = IconTextButton(icon = Icons.Default.Share, text = "Share", onAction = {})
        )
    }
}