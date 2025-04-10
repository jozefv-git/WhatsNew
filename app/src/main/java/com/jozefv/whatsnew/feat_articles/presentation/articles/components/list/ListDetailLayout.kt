@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.jozefv.whatsnew.feat_articles.presentation.articles.components.list

import WhatsNewTheme
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.feat_articles.presentation.Action
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesAction
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesState
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.ArticleUi
import com.jozefv.whatsnew.core.presentation.model.IconTextButton
import com.jozefv.whatsnew.core.presentation.util.shareLink
import com.jozefv.whatsnew.feat_articles.presentation.InfoChipAction
import com.jozefv.whatsnew.feat_articles.presentation.articles.model.previewArticle
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.previewFilterState
import com.jozefv.whatsnew.feat_articles.presentation.search.model.previewSearchState
import kotlinx.coroutines.launch

@Composable
fun ListDetailLayout(
    modifier: Modifier = Modifier,
    state: ArticlesState,
    backFromTheDetail: () -> Unit,
    showLoginSnackBar: () -> Unit,
    onCardClick: () -> Unit,
    onAction: (ArticlesAction) -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<ArticleUi>()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    // Triggers when user press back button while on the detail screen
    BackHandler(navigator.canNavigateBack()) {
        backFromTheDetail()
        scope.launch {
            navigator.navigateBack()
        }
    }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            Column {
                TopListFilterSection(
                    searchWithFiltersVisible = state.searchState.searchWithFiltersVisible,
                    query = state.searchState.query,
                    onQueryClick = { onAction(InfoChipAction.OnRemoveSearchQuery) },
                    filtersForDisplay = state.filterState.filtersForDisplay,
                    selectedFilter = state.selectedFilterChip,
                    onFilterClick = { isSelected, filterUi ->
                        onAction(
                            InfoChipAction.OnFilterArticles(
                                action = if (isSelected) {
                                    Action.REMOVE
                                } else {
                                    Action.ADD
                                },
                                filter = filterUi
                            )
                        )
                    })

                // Our List screen
                ArticleList(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onCardClick = { articleUi ->
                        onCardClick()
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = articleUi
                            )
                        }
                    },
                    onRefresh = {
                        onAction(ArticlesAction.OnRefresh)
                    },
                    onLoadMoreNews = {
                        onAction(ArticlesAction.OnLoadMoreArticles)
                    },
                )
            }
        },
        detailPane = {
            // Content from our clicked item - detail screen
            navigator.currentDestination?.contentKey?.let { articleUi ->
                // Our Detail Screen with animation
                AnimatedPane {
                    ArticleDetail(
                        modifier = Modifier.fillMaxSize(),
                        articleUi = articleUi,
                        leadingIcon =
                        IconTextButton(
                            isLoading = state.isLoadingArticleSave,
                            icon = Icons.Default.Save,
                            unavailable = !state.isLoggedIn,
                            text = stringResource(id = R.string.save),
                            onAction = {
                                if (!state.isLoggedIn) {
                                    showLoginSnackBar()
                                } else {
                                    onAction(ArticlesAction.OnSaveArticle(articleUi))
                                }
                            }),
                        trailingIcon = IconTextButton(
                            icon = Icons.Default.Share,
                            text = stringResource(id = R.string.share),
                            onAction = {
                                context.shareLink(articleUi.articleLink)
                            }
                        )
                    )
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun ListDetailLayoutPreview() {
    WhatsNewTheme {
        ListDetailLayout(
            state = ArticlesState(
                searchState = previewSearchState,
                filterState = previewFilterState(),
                isLoggedIn = true,
                error = null,
                articles = List(10) { previewArticle }
            ),
            backFromTheDetail = {  },
            showLoginSnackBar = {  },
            onCardClick = {},
            onAction = {})
    }
}