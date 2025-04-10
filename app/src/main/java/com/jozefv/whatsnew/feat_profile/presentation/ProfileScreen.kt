package com.jozefv.whatsnew.feat_profile.presentation

import WhatsNewTheme
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold
import com.jozefv.whatsnew.core.presentation.components.CustomToolBar
import com.jozefv.whatsnew.core.presentation.components.CustomAlertDialog
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomFabButton
import com.jozefv.whatsnew.core.presentation.components.CustomSearchBar
import com.jozefv.whatsnew.core.presentation.components.CustomSnackBar
import com.jozefv.whatsnew.core.presentation.components.SpacerVerXS
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomDeleteTextButton
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.core.presentation.model.IconTextButton
import com.jozefv.whatsnew.core.presentation.util.shareLink
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL
import com.jozefv.whatsnew.feat_profile.presentation.components.ArticleCard
import com.jozefv.whatsnew.feat_profile.presentation.components.ArticleDetail
import com.jozefv.whatsnew.feat_profile.presentation.model.ArticleLocalUi
import com.jozefv.whatsnew.feat_profile.presentation.model.previewArticleLocal
import kotlinx.coroutines.launch

@Composable
fun ProfileScreenRoot(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel,
) {
    ProfileScreen(
        modifier = modifier,
        state = viewModel.state,
        onBackClick = { onBackClick() },
        onAction = { action ->
            // Propagate navigation up
            if (action is ProfileAction.OnLogout) {
                onLogoutClick()
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier,
    state: ProfileState,
    onBackClick: () -> Unit,
    onAction: (ProfileAction) -> Unit,
) {
    val customStyle = CustomStyle.Text
    val navigator = rememberListDetailPaneScaffoldNavigator<ArticleLocalUi>()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLogoutDialogVisible by remember {
        mutableStateOf(false)
    }
    var isDeleteAllDialogVisible by remember {
        mutableStateOf(false)
    }
    var isDetailVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
        isDetailVisible = !isDetailVisible
    }


    val lazyListState = rememberLazyListState()
    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    val scrollUpButtonVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 3
        }
    }
    CustomScaffold(
        modifier = modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
        topAppBar = {
            CustomToolBar(
                scrollBehavior = scrollBehaviour,
                title = if (!isDetailVisible) {
                    stringResource(id = R.string.profile)
                } else {
                    stringResource(id = R.string.detail)
                },
                leadingContent = {
                    if (!isDetailVisible) {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = Icons.AutoMirrored.Default.ArrowBack.name
                            )
                        }
                    }
                },
                trailingContent = {
                    if (!isDetailVisible) {
                        IconButton(onClick = { isLogoutDialogVisible = true }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Logout,
                                contentDescription = Icons.AutoMirrored.Default.Logout.name
                            )
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
                CustomSnackBar(maxLines = 1, snackBarData = data)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(visible = !lazyListState.canScrollBackward && !isDetailVisible) {
                Column {
                    Text(
                        style = customStyle.HeadlineText,
                        text = "${stringResource(id = R.string.hi)} ${state.nick}!"
                    )
                    SpacerVerXS()
                    Text(
                        style = customStyle.InfoTextLessImportant,
                        text = state.email)
                    SpacerVerM()
                    Text(
                        style = customStyle.DefaultText,
                        text = stringResource(id = R.string.profile_description)
                    )
                    SpacerVerL()
                }
            }
            if (state.articles.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(style = customStyle.DefaultText, text = stringResource(id = R.string.hmm))
                    SpacerVerM()
                    Text(
                        style = customStyle.DefaultText,
                        text = stringResource(id = R.string.zero_liked_articles)
                    )
                }
            } else {
                AnimatedVisibility(!isDetailVisible) {
                    Column {
                        CustomSearchBar(
                            supportingText = stringResource(id = R.string.search_for_info_text),
                            trailingIcon = if (state.query.isNotBlank()) {
                                ClickableIcon(
                                    icon = Icons.Default.Clear,
                                    onAction = {
                                        onAction(ProfileAction.OnClearQuery)
                                    }
                                )
                            } else {
                                null
                            },
                            query = state.query,
                            onQueryChange = {
                                onAction(ProfileAction.OnQueryChange(it))
                            })
                        if (lazyListState.canScrollBackward) {
                            SpacerVerL()
                        }
                    }
                }
                AnimatedVisibility(!lazyListState.canScrollBackward && !isDetailVisible) {
                    Column(Modifier.fillMaxWidth()) {
                        SpacerVerM()
                        if (state.articles.size > 2) {
                            CustomDeleteTextButton(
                                modifier = Modifier.align(Alignment.End),
                                text = stringResource(id = R.string.delete_all_articles_title),
                                onClick = {
                                    isDeleteAllDialogVisible = !isDeleteAllDialogVisible
                                })
                            SpacerVerM()
                        }
                    }
                }
                ListDetailPaneScaffold(
                    directive = navigator.scaffoldDirective,
                    value = navigator.scaffoldValue,
                    listPane = {
                        Box {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = lazyListState,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(if (state.query.isBlank()) state.articles else state.filteredArticles) { articleLocalUi ->
                                    ArticleCard(
                                        articleLocalUi = articleLocalUi,
                                        onClick = {
                                            isDetailVisible = !isDetailVisible
                                            // Navigate to detail screen with provided content
                                            scope.launch {
                                                navigator.navigateTo(
                                                    ListDetailPaneScaffoldRole.Detail,
                                                    contentKey = articleLocalUi
                                                )
                                            }
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
                                        lazyListState.animateScrollToItem(0)
                                    }
                                })
                        }
                    },
                    detailPane = {
                        // Content from our clicked item
                        navigator.currentDestination?.contentKey?.let { articleLocalUi ->
                            AnimatedPane {
                                ArticleDetail(
                                    modifier = Modifier.fillMaxSize(),
                                    articleLocalUi = articleLocalUi,
                                    leadingIcon = IconTextButton(
                                        icon = Icons.Default.Delete,
                                        text = stringResource(id = R.string.delete),
                                        onAction = {
                                            scope.launch {
                                                // After delete is clicked - navigate back and show a SnackBar
                                                navigator.navigateBack()
                                                isDetailVisible = !isDetailVisible
                                                val snackResult = snackBarHostState.showSnackbar(
                                                    message = "${context.getString(R.string.deleted)} ${articleLocalUi.title}",
                                                    actionLabel = context.getString(R.string.undo),
                                                    duration = SnackbarDuration.Short
                                                )
                                                if (snackResult == SnackbarResult.Dismissed) {
                                                    // No action performed - item is deleted
                                                    onAction(
                                                        ProfileAction.OnRemoveArticle(
                                                            articleLocalUi = articleLocalUi
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    ),
                                    trailingIcon = IconTextButton(
                                        icon = Icons.Default.Share,
                                        text = stringResource(id = R.string.share),
                                        onAction = {
                                            context.shareLink(articleLocalUi.articleLink)
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            }

            if (isDeleteAllDialogVisible) {
                CustomAlertDialog(
                    dialogTitle = stringResource(id = R.string.delete_all_articles_title),
                    dialogText = stringResource(id = R.string.delete_all_articles_warning),
                    dialogIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = Icons.Default.Delete.name
                        )
                    },
                    onDismiss = { isDeleteAllDialogVisible = !isDeleteAllDialogVisible },
                    onConfirm = {
                        onAction(ProfileAction.OnDeleteAllArticles)
                        isDeleteAllDialogVisible = !isDeleteAllDialogVisible
                    }
                )
            }

            if (isLogoutDialogVisible) {
                CustomAlertDialog(
                    dialogTitle = stringResource(id = R.string.logout),
                    dialogText = stringResource(id = R.string.logout_warning),
                    dialogIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Logout,
                            contentDescription = Icons.AutoMirrored.Default.Logout.name
                        )
                    },
                    onDismiss = { isLogoutDialogVisible = !isLogoutDialogVisible },
                    onConfirm = {
                        onAction(ProfileAction.OnLogout)
                        isLogoutDialogVisible = !isLogoutDialogVisible
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ProfileScreenPreview() {
    WhatsNewTheme {
        ProfileScreen(
            state = ProfileState(
                nick = "Jozef",
                email = "jozef@mail.com",
                articles = List(10) {
                    previewArticleLocal
                }
            ),
            onBackClick = {},
            onAction = {}
        )
    }
}
