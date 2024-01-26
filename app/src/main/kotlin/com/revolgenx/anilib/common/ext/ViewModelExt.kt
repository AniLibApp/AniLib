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

//
//@OptIn(KoinInternalApi::class)
//inline fun <reified T : ViewModel> ScreenLifecycleOwner.koinViewModel(
//    qualifier: Qualifier? = null,
//    key: String? = null,
//    extras: CreationExtras = defaultExtras(this as AndroidScreenLifecycleOwner),
//    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
//    noinline parameters: ParametersDefinition? = null,
//): T {
//    return (this as AndroidScreenLifecycleOwner).viewModel(
//        qualifier = qualifier,
//        key = key,
//        extras = extras,
//        scope = scope,
//        parameters = parameters
//    )
//}

//@OptIn(KoinInternalApi::class)
//inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(
//    qualifier: Qualifier? = null,
//    key: String? = null,
//    extras: CreationExtras = defaultExtras(this),
//    scope: Scope = GlobalContext.get().scopeRegistry.rootScope,
//    noinline parameters: ParametersDefinition? = null,
//): T {
//    val currentBundle = (this as? NavBackStackEntry)?.arguments?.toExtras(this)
//    return resolveViewModel(
//        T::class, this.viewModelStore, key, currentBundle ?: extras, qualifier, scope, parameters
//    )
//}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(block = block)
}
fun ViewModel.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launchIO(block = block)
}

