package com.jozefv.whatsnew.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomDeleteTextButton
import com.jozefv.whatsnew.core.presentation.model.ClickableIcon
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.horizontalPaddingM
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle.lessImportant
import com.jozefv.whatsnew.feat_auth.presentation.components.CustomInputField

@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    leadingIcon: ClickableIcon? = null,
    trailingIcon: ClickableIcon? = null,
    isLoading: Boolean = false,
    hint: String? = "Search",
    supportingText: String? = null,
    isSupportingTextError: Boolean = false,
    showSuggestions: Boolean = false,
    suggestions: List<String>? = null,
    onSuggestion: (suggestion: String) -> Unit = {},
    onDeleteAllSuggestions: () -> Unit = {},
    onKeyboardSearch: ((query: String) -> Unit)? = null,
    query: String,
    onQueryChange: (query: String) -> Unit,

    ) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        CustomInputField(
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            trailingIsLoading = isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = onKeyboardSearch?.let { KeyboardActions(onSearch = { it(query) }) },
            hint = hint,
            supportingText = supportingText,
            isSupportingTextError = isSupportingTextError,
            value = query,
            onValueChange = { onQueryChange(it) }
        )
        if (showSuggestions && suggestions != null) {
            SpacerVerM()
            CustomDeleteTextButton(
                modifier = Modifier.align(Alignment.End),
                text = stringResource(id = R.string.delete_suggestions), onClick = {
                    onDeleteAllSuggestions()
                })
            SpacerVerL()
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .horizontalPaddingM(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(suggestions) {
                    SuggestionItem(
                        modifier = Modifier.fillMaxWidth(),
                        suggestion = it,
                        onClick = {
                            onSuggestion(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    modifier: Modifier = Modifier,
    suggestion: String,
    onClick: () -> Unit
) {
    Column(modifier.clickable { onClick() }) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                tint = MaterialTheme.colorScheme.onSurface.lessImportant(),
                imageVector = Icons.Default.History, contentDescription = Icons.Default.History.name
            )
            SpacerHorM()
            // Takes remaining row space
            Text(
                style = CustomStyle.Text.SuggestionItemText.copy(
                    color = MaterialTheme.colorScheme.onSurface.lessImportant()
                ),
                modifier = Modifier.weight(1f),
                text = suggestion
            )
            Icon(
                tint = MaterialTheme.colorScheme.onSurface.lessImportant(),
                imageVector = Icons.Default.Search,
                contentDescription = Icons.Default.Search.name
            )
        }
        SpacerVerM()
        HorizontalDivider()
    }
}

@PreviewLightDark
@Composable
private fun CustomTopSearchBarPreview() {
    CustomSearchBar(
        leadingIcon = ClickableIcon(icon = Icons.AutoMirrored.Default.ArrowBack, onAction = {}),
        trailingIcon = ClickableIcon(icon = Icons.Default.Search, onAction = {}),
        hint = "Search",
        showSuggestions = true,
        suggestions = List(20) { "Suggestion" },
        onQueryChange = {},
        query = "",
        onKeyboardSearch = {}
    )
}