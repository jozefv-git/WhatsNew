package com.jozefv.whatsnew.feat_articles.presentation.articles.components.list

import WhatsNewTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerHorXS
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomFabButton
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesState
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.ArticleUi
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.previewArticle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    state: ArticlesState,
    onCardClick: (articleUi: ArticleUi) -> Unit,
    onRefresh: () -> Unit,
    onLoadMoreNews: () -> Unit,
) {
    val customStyle = CustomStyle.Text
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollUpButtonVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 3
        }
    }

    val pullToRefreshState = rememberPullToRefreshState()
    val articlesToShow = state.filteredArticles ?: state.articles
    articlesToShow?.let { articles ->
        var previousLastArticle by remember {
            mutableStateOf<ArticleUi?>(null)
        }
        // If list size isn't more than 1, then there is no need for fetch more data
        var previousListSize by remember {
            mutableIntStateOf(1)
        }
        // Can be empty if there are no articles based on the selected filter
        if(articles.isNotEmpty()) {
            LaunchedEffect(key1 = listState.canScrollForward, key2 = articles.size) {
                // If we are at the bottom of the list try to load more data again
                if (
                    !listState.canScrollForward &&
                    previousLastArticle != articles.last()
                    && articles.size > previousListSize
                ) {
                    onLoadMoreNews()
                    previousLastArticle = articles.last()
                    previousListSize = articles.size
                }
            }
        }
        PullToRefreshBox(
            modifier = modifier,
            state = pullToRefreshState,
            isRefreshing = state.isLoadingArticles,
            onRefresh = { onRefresh() },
            content = {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onRefresh() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            style = customStyle.InfoText.copy(color = MaterialTheme.colorScheme.onSurface.lessImportant()),
                            text = stringResource(id = R.string.updated_at) + state.refreshedTime
                        )
                        SpacerHorXS()
                        if (!state.isLoadingArticles) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface.lessImportant(),
                                imageVector = Icons.Default.Refresh,
                                contentDescription = Icons.Default.Refresh.name
                            )
                        }
                    }
                    SpacerVerM()
                    if (articles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                style = customStyle.InfoText,
                                text = stringResource(id = R.string.we_havent_find_any_filtered_results)
                            )
                        }
                    } else {
                        Box {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(articles) { articleUi ->
                                    ArticleCard(
                                        articleUi = articleUi,
                                        onClick = {
                                            onCardClick(articleUi)
                                        }
                                    )
                                }
                            }
                            CustomFabButton(
                                modifier = Modifier.align(Alignment.TopCenter),
                                isVisible = scrollUpButtonVisible,
                                icon = Icons.Default.ArrowUpward,
                                onclick = {
                                    scope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                })
                        }
                    }
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun ArticleListPreview() {
    WhatsNewTheme {
        ArticleList(
            state = ArticlesState(
                articles = List(10) { previewArticle }
            ),
            onCardClick = {},
            onRefresh = {},
            onLoadMoreNews = {})
    }
}