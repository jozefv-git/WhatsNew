package com.jozefv.whatsnew.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jozefv.whatsnew.core.presentation.util.style.CustomStyle

private val size = CustomStyle.Size()

@Composable
fun SpacerHorXS() {
    Spacer(modifier = Modifier.width(size.extraSmall))
}

@Composable
fun SpacerHorS() {
    Spacer(modifier = Modifier.width(size.small))
}

@Composable
fun SpacerHorM() {
    Spacer(modifier = Modifier.width(size.medium))
}

@Composable
fun SpacerHorL() {
    Spacer(modifier = Modifier.width(size.large))
}

@Composable
fun SpacerHorXL() {
    Spacer(modifier = Modifier.width(size.extraSmall))
}

@Composable
fun SpacerVerXS() {
    Spacer(modifier = Modifier.height(size.extraSmall))
}

@Composable
fun SpacerVerS() {
    Spacer(modifier = Modifier.height(size.small))
}

@Composable
fun SpacerVerM() {
    Spacer(modifier = Modifier.height(size.medium))
}

@Composable
fun SpacerVerL() {
    Spacer(modifier = Modifier.height(size.large))
}

@Composable
fun SpacerVerXL() {
    Spacer(modifier = Modifier.height(size.extraLarge))
}