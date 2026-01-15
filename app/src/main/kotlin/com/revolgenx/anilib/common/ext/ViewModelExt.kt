package com.revolgenx.anilib.common.ext

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.ui.composition.GlobalViewModelStoreOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable()
inline fun <reified T : ViewModel> activityViewModel(): T {
    return koinViewModel(viewModelStoreOwner = GlobalViewModelStoreOwner.current)
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(block = block)
}
fun ViewModel.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launchIO(block = block)
}

