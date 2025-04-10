@file:OptIn(ExperimentalMaterial3Api::class)

package com.jozefv.whatsnew.feat_articles.presentation.articles

import WhatsNewTheme
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.util.ObserveAsEvents
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomOutlinedButton
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold
import com.jozefv.whatsnew.core.presentation.components.CustomSnackBar
import com.jozefv.whatsnew.core.presentation.components.CustomToolBar
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesAction
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesEvent
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesState
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesViewModel
import com.jozefv.whatsnew.feat_articles.presentation.FilterAction
import com.jozefv.whatsnew.feat_articles.presentation.SearchAction
import com.jozefv.whatsnew.feat_articles.presentation.articles.components.list.ListDetailLayout
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

@Composable
fun NewsScreenRoot(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SharedArticlesViewModel
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.channel) { event ->
        when (event) {
            is SharedArticlesEvent.ErrorEventShared -> {
                Toast.makeText(context, event.value.asString(context), Toast.LENGTH_SHORT).show()
            }
            is SharedArticlesEvent.SaveSuccess -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.save_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    NewsScreen(
        modifier = modifier,
        state = viewModel.state,
        onProfileClick = { onProfileClick() },
        onLoginClick = { onLoginClick() },
        onAction = { action ->
            // Propagate navigation up
            when(action){
                SearchAction.OnSearchOpen -> onSearchClick()
                FilterAction.OnFilterOpen -> onFilterClick()
                else -> Unit
            }
            // Handle actions with VM
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun NewsScreen(
    modifier: Modifier = Modifier,
    state: ArticlesState,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAction: (ArticlesAction) -> Unit,
) {
    val context = LocalContext.current
    val customStyle = CustomStyle.Text
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    var detailVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    CustomScaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topAppBar = {
            CustomToolBar(
                scrollBehavior = scrollBehaviour,
                title = if (!detailVisible) {
                    stringResource(id = R.string.latest_news)
                } else {
                    stringResource(id = R.string.detail)
                },
                trailingContent = {
                    if (!detailVisible) {
                        IconButton(onClick = { onAction(SearchAction.OnSearchOpen) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = Icons.Default.Search.name
                            )
                        }
                        IconButton(onClick = { onAction(FilterAction.OnFilterOpen) }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = Icons.Default.FilterList.name
                            )
                        }
                        if (state.isLoggedIn) {
                            IconButton(onClick = { onProfileClick() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = Icons.Default.AccountCircle.name
                                )
                            }
                        }
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
                hostState = snackBarHostState
            ) { data ->
                CustomSnackBar(snackBarData = data)
            }
        }
    ) { paddingValues ->
        if (state.error == null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!state.isLoadingArticles) {
                    ListDetailLayout(
                        modifier = Modifier.weight(1f),
                        state = state,
                        backFromTheDetail = {
                            detailVisible = false
                        },
                        onCardClick = {
                            detailVisible = true
                        },
                        onAction = {
                            onAction(it)
                        },
                        showLoginSnackBar = {
                            scope.coroutineContext.cancelChildren()
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = context.getString(R.string.to_be_able_to_save_login),
                                    actionLabel = context.getString(R.string.sign_in),
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    onLoginClick()
                                }
                            }
                        }
                    )
                    if (state.isLoadingMoreArticles) {
                        CircularProgressIndicator()
                    }
                    if (!detailVisible) {
                        SpacerVerM()
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = customStyle.InfoTextLessImportant.copy(color = MaterialTheme.colorScheme.onSurface.lessImportant()),
                            text = stringResource(id = R.string.all_data_from_previous_48h)
                        )
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = customStyle.DefaultText,
                    text = stringResource(id = R.string.error_occured)
                )
                SpacerVerM()
                Text(
                    style = customStyle.ErrorDefaultText,
                    textAlign = TextAlign.Center,
                    text = state.error.asComposeString()
                )
                SpacerVerL()
                CustomOutlinedButton(
                    text = stringResource(id = R.string.fetch_news_again),
                    onClick = {
                        onAction(ArticlesAction.OnRefresh)
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun NewsScreenPreview() {
// Add theme here
    WhatsNewTheme {
        NewsScreen(
            state = ArticlesState(),
            onProfileClick = {},
            onLoginClick = {},
            onAction = {}
        )
    }
}
