package com.revolgenx.anilib.common.ui.screen

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.data.state.ResourceState

@Composable
fun <T> ResourceScreen(
    resourceState: ResourceState<T>?,
    refresh: () -> Unit,
    content: @Composable (data: T) -> Unit
) {
    when (resourceState) {
        is ResourceState.Error -> ErrorScreen(error = resourceState.message) {
            refresh.invoke()
        }

        is ResourceState.Loading -> LoadingScreen()

        is ResourceState.Success -> {
            val data = resourceState.data ?: return
            content(data)
        }

        else -> {}
    }
}