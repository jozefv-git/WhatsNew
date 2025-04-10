package com.jozefv.whatsnew.feat_articles.presentation.search


import WhatsNewTheme
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.ObserveAsEvents
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.CustomAlertDialog
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold
import com.jozefv.whatsnew.core.presentation.components.CustomSearchBar
import com.jozefv.whatsnew.core.presentation.components.CustomSnackBar
import com.jozefv.whatsnew.core.presentation.components.CustomSwitch
import com.jozefv.whatsnew.core.presentation.components.CustomToolBar
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesEvent
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesState
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesViewModel
import com.jozefv.whatsnew.feat_articles.presentation.SearchAction
import com.jozefv.whatsnew.feat_articles.presentation.search.model.previewSearchState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


@Composable
fun SearchScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SharedArticlesViewModel,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.channel) { event ->
        when (event) {
            SharedArticlesEvent.SearchSuccess -> onBackClick()
            is SharedArticlesEvent.ErrorEventShared -> {
                Toast.makeText(context, event.value.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }
    SearchScreen(
        modifier = modifier,
        state = viewModel.state,
        onBackClick = { onBackClick() },
        onLoginCLick = { onLoginClick() },
        onAction = { action -> viewModel.onAction(action) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    state: ArticlesState,
    // Just a back lambda - we don't need to react to navigation anyhow in our VM
    onBackClick: () -> Unit,
    onLoginCLick: () -> Unit,
    onAction: (SearchAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isDeleteAllSuggestionsDialogVisible by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    BackHandler {
        onBackClick()
    }

    CustomScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            CustomToolBar(
                title = stringResource(id = R.string.search),
                scrollBehavior = scrollBehavior,
                leadingContent = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = Icons.AutoMirrored.Default.ArrowBack.name
                        )
                    }
                }
            )
        },
        snackBarHost = {
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalPaddingM()
                    .verticalPaddingL(),
                hostState = snackBarHostState,
                snackbar = { data ->
                    CustomSnackBar(snackBarData = data)
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier
                .run {
                    if (!state.isLoggedIn) {
                        clickable {
                            scope.coroutineContext.cancelChildren()
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = context.getString(R.string.to_be_able_to_use_sign_in),
                                    actionLabel = context.getString(R.string.sign_in),
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onLoginCLick()
                                }
                            }
                        }
                    } else this
                }
            ) {
                CustomSwitch(
                    enabled = state.isLoggedIn,
                    title = stringResource(id = R.string.search_with_filters),
                    description = stringResource(id = R.string.search_with_filters_description),
                    checked = state.searchState.searchWithFilters,

                    onCheckedChange = { onAction(SearchAction.OnSearchWithFilters) })
                SpacerVerM()
                CustomSwitch(
                    enabled = state.isLoggedIn,
                    title = stringResource(id = R.string.search_in_title),
                    description = stringResource(id = R.string.search_in_title_description),
                    checked = state.searchState.searchInTitle,
                    onCheckedChange = { onAction(SearchAction.OnSearchInTitle) })
                SpacerVerM()
                CustomSwitch(
                    enabled = state.isLoggedIn,
                    title = stringResource(id = R.string.exact_phrases),
                    description = stringResource(id = R.string.exact_phrases_description),
                    checked = state.searchState.searchExactPhrases,
                    onCheckedChange = { onAction(SearchAction.OnSearchExactPhrases) })
            }
            SpacerVerL()
            CustomSearchBar(
                modifier = Modifier,
                trailingIcon = ClickableIcon(
                    icon = Icons.Default.Search,
                    onAction = { onAction(SearchAction.OnQuerySearch) }
                ),
                isLoading = state.searchState.isLoadingSearch,
                supportingText = stringResource(id = R.string.maximum_query_limit_info),
                isSupportingTextError = state.searchState.query.length > 100,
                showSuggestions = state.isLoggedIn,
                suggestions = state.searchState.querySuggestions,
                onSuggestion = { suggestion -> onAction(SearchAction.OnQuery(suggestion)) },
                onDeleteAllSuggestions = {
                    isDeleteAllSuggestionsDialogVisible = !isDeleteAllSuggestionsDialogVisible
                },
                onKeyboardSearch = { onAction(SearchAction.OnQuerySearch) },
                onQueryChange = { onAction(SearchAction.OnQuery(it)) },
                query = state.searchState.query
            )
        }

        if (isDeleteAllSuggestionsDialogVisible) {
            CustomAlertDialog(
                dialogTitle = stringResource(id = R.string.delete_all_suggestions_title),
                dialogText = stringResource(id = R.string.delete_all_suggestions_warning),
                onDismiss = {
                    isDeleteAllSuggestionsDialogVisible = !isDeleteAllSuggestionsDialogVisible
                },
                onConfirm = {
                    onAction(SearchAction.OnDeleteAllQuerySuggestions)
                    isDeleteAllSuggestionsDialogVisible = !isDeleteAllSuggestionsDialogVisible
                })
        }
    }
}


@Composable
@PreviewLightDark
private fun FilterPreview() {
    WhatsNewTheme {
        SearchScreen(
            state = ArticlesState(isLoggedIn = true, searchState = previewSearchState),
            onBackClick = {},
            onLoginCLick = {},
            onAction = {},
        )
    }
}