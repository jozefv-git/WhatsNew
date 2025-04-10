package com.jozefv.whatsnew.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(
        key1 = flow,
        key2 = key,
        key3 = lifeCycleOwner
    ) {
        // Flow will be collected only if the app is in the foreground or came back to foreground
        // flow collection will be not active during the onDestory() state - but will re-activate again
        // when started
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // Main.immediate makes sure that our emission cannot get lost as it will be processed
            // on the same event cycle in which app is currently on. So if onDestroy() is going to be called
            // ex. during the screen rotation - Dispatcher.Main.immediate will make sure that our emission will be
            // proceeded before that
            withContext(Dispatchers.Main.immediate) {
                flow.collect {
                    onEvent(it)
                }
            }
        }
    }
}