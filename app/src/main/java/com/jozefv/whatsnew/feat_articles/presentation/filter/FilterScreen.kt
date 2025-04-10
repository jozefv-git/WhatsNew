package com.jozefv.whatsnew.feat_articles.presentation.filter

import WhatsNewTheme
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.ObserveAsEvents
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomActionButton
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomDeleteOutlinedButton
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold
import com.jozefv.whatsnew.core.presentation.components.CustomSnackBar
import com.jozefv.whatsnew.core.presentation.components.CustomSwitch
import com.jozefv.whatsnew.core.presentation.components.CustomToolBar
import com.jozefv.whatsnew.core.presentation.components.InfoIconRowText
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.verticalPaddingL
import com.jozefv.whatsnew.feat_articles.presentation.filter.components.ChipFilters
import com.jozefv.whatsnew.feat_articles.presentation.filter.util.Filters
import com.jozefv.whatsnew.feat_articles.presentation.Action
import com.jozefv.whatsnew.feat_articles.presentation.FilterAction
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesEvent
import com.jozefv.whatsnew.feat_articles.presentation.ArticlesState
import com.jozefv.whatsnew.feat_articles.presentation.SharedArticlesViewModel
import com.jozefv.whatsnew.feat_articles.presentation.filter.model.previewFilterState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


@Composable
fun FilterScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SharedArticlesViewModel,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.channel) { event ->
        when (event) {
            is SharedArticlesEvent.ErrorEventShared -> {
                Toast.makeText(
                    context,
                    event.value.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }

            SharedArticlesEvent.FilterSuccess -> onBackClick()
            SharedArticlesEvent.FiveCategoriesSelected -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.five_categories_selected),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (event is SharedArticlesEvent.FilterSuccess) {
            onBackClick()
        }
    }

    FilterScreen(
        modifier = modifier,
        state = viewModel.state,
        onBackClick = { onBackClick() },
        onLoginClick = { onLoginClick() },
        onAction = { action ->
            viewModel.onAction(action)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterScreen(
    modifier: Modifier = Modifier,
    state: ArticlesState,
    // Just a back lambda - we don't need to react to navigation anyhow in our VM
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onAction: (FilterAction) -> Unit,
) {
    val context = LocalContext.current

    val categoryFilters = Filters.CATEGORY_FILTERS
    val countryFilters = Filters.COUNTRY_FILTERS
    val languageFilters = Filters.LANGUAGE_FILTERS

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    CustomScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topAppBar = {
            CustomToolBar(
                title = stringResource(id = R.string.filter_title),
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
                snackbar = { snackBarData ->
                    CustomSnackBar(snackBarData = snackBarData)
                }
            )
        }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            CustomSwitch(
                modifier = Modifier
                    .run {
                        if (!state.isLoggedIn) {
                            clickable {
                                // Re-launch coroutine if clicked again
                                scope.coroutineContext.cancelChildren()
                                scope.launch {
                                    val snackResult = snackBarHostState.showSnackbar(
                                        message = context.getString(R.string.to_be_able_to_use_sign_in),
                                        actionLabel = context.getString(R.string.sign_in),
                                        duration = SnackbarDuration.Short
                                    )
                                    if (snackResult == SnackbarResult.ActionPerformed) {
                                        onLoginClick()
                                    }
                                }

                            }
                        } else this
                    },
                enabled = state.isLoggedIn,
                title = stringResource(id = R.string.exclude_category),
                description = stringResource(id = R.string.exclude_category_description),
                checked = state.filterState.excludeFilterCategory,
                onCheckedChange = {
                    onAction(FilterAction.OnExcludeCategories)
                }
            )
            SpacerVerM()
            ChipFilters(
                filtersCategory = stringResource(id = R.string.category_section),
                filterDescription = stringResource(id = R.string.category_description),
                filters = categoryFilters.filters,
                selectedFilters = state.filterState.selectedFilters,
                onClick = { currentFilter, isSelected ->
                    onAction(
                        FilterAction.OnSelectFilter(
                            action = if (isSelected) Action.REMOVE else Action.ADD,
                            filter = currentFilter
                        )
                    )
                }
            )
            SpacerVerM()
            HorizontalDivider()
            SpacerVerM()
            ChipFilters(
                filtersCategory = stringResource(id = R.string.country_section),
                filterDescription = stringResource(id = R.string.country_description),
                filters = countryFilters.filters,
                selectedFilters = state.filterState.selectedFilters,
                onClick = { currentFilter, isSelected ->
                    onAction(
                        FilterAction.OnSelectFilter(
                            action = if (isSelected) Action.REMOVE else Action.ADD,
                            filter = currentFilter
                        )
                    )
                }
            )
            SpacerVerM()
            HorizontalDivider()
            SpacerVerM()
            ChipFilters(
                filtersCategory = stringResource(id = R.string.language_section),
                filterDescription = stringResource(id = R.string.language_description),
                filters = languageFilters.filters,
                selectedFilters = state.filterState.selectedFilters,
                onClick = { currentFilter, isSelected ->
                    onAction(
                        FilterAction.OnSelectFilter(
                            action = if (isSelected) Action.REMOVE else Action.ADD,
                            filter = currentFilter
                        )
                    )
                }
            )
            SpacerVerM()
            HorizontalDivider()
            SpacerVerM()
            InfoIconRowText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.select_up_to_five_filters)
            )
            SpacerVerM()
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = if (state.filterState.selectedFilters.size > 2) Arrangement.SpaceBetween else Arrangement.End
            ) {
                if (state.filterState.selectedFilters.size > 2) {
                    CustomDeleteOutlinedButton(
                        icon = Icons.Default.Clear,
                        text = stringResource(id = R.string.button_clear_all),
                        onClick = {
                            onAction(FilterAction.OnRemoveAllFilters)
                        })
                }
                CustomActionButton(
                    enabled = !state.isLoadingArticles,
                    isLoading = state.isLoadingArticles,
                    text = stringResource(id = R.string.button_filter),
                    onClick = { onAction(FilterAction.OnFilter) })
            }
        }
    }
}


@Composable
@PreviewLightDark
private fun FilterPreview() {
    WhatsNewTheme {
        FilterScreen(
            state = ArticlesState(filterState = previewFilterState()),
            onBackClick = {},
            onLoginClick = {},
            onAction = {},
        )
    }
}