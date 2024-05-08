package com.revolgenx.anilib.common.ui.screen.state

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.common.util.OnClick

@Composable
fun <M : Any, F : BaseField<*>> ResourceScreen(
    viewModel: ResourceViewModel<M, F>,
    content: @Composable (data: M) -> Unit
) {
    ResourceLoadingSection(viewModel)
    ResourceScreenContent(viewModel, content)
}

@Composable
private fun <M : Any, F : BaseField<*>> ResourceScreenContent(
    viewModel: ResourceViewModel<M, F>,
    content: @Composable (data: M) -> Unit
) {
    when (val resourceState = viewModel.resource.value) {
        is ResourceState.Error -> ErrorScreen(error = resourceState.message) {
            viewModel.refresh()
        }

        is ResourceState.Loading -> LoadingScreen()

        is ResourceState.Success -> {
            val data = resourceState.data ?: return
            content(data)
        }

        else -> {}
    }
}

@Composable
private fun <M : Any, F : BaseField<*>> ResourceLoadingSection(viewModel: ResourceViewModel<M, F>) {
    if (viewModel.saveResource is ResourceState.Loading || viewModel.deleteResource is ResourceState.Loading) {
        LinearLoadingSection()
    }
}
