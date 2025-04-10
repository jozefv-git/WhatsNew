package com.jozefv.whatsnew.feat_auth.presentation.intro

import WhatsNewTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jozefv.whatsnew.R
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle
import com.jozefv.whatsnew.core.presentation.components.SpacerVerL
import com.jozefv.whatsnew.core.presentation.components.SpacerVerM
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomActionButton
import com.jozefv.whatsnew.core.presentation.components.buttons.CustomOutlinedButton
import com.jozefv.whatsnew.core.presentation.components.CustomScaffold

@Composable
fun IntroScreenRoot(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    IntroScreen(
        modifier = modifier,
        onAction = { action ->
            when (action) {
                IntroAction.OnSignUpClick -> onSignUpClick()
                IntroAction.OnSkipClick -> onSkipClick()
            }
        }
    )
}

@Composable
private fun IntroScreen(
    modifier: Modifier = Modifier,
    onAction: (IntroAction) -> Unit
) {
    val customStyle = CustomStyle.Text
    CustomScaffold(modifier = modifier) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = customStyle.TitleText,
                    text = stringResource(id = R.string.whats_new)
                )
                Text(
                    style = customStyle.DefaultText,
                    text = stringResource(id = R.string.introduction)
                )
            }
            Text(
                style = customStyle.DefaultText,
                text = stringResource(id = R.string.login_benefits)
            )
            SpacerVerL()
            CustomOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.skip),
                onClick = {
                    onAction(IntroAction.OnSkipClick)
                }
            )
            SpacerVerM()
            CustomActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sign_up),
                onClick = {
                    onAction(IntroAction.OnSignUpClick)
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun IntroScreenPreview() {
    WhatsNewTheme {
        IntroScreen(
            onAction = {}
        )
    }
}