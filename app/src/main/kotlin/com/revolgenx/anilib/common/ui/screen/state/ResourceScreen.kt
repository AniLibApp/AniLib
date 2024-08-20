package com.revolgenx.anilib.common.ui.screen.state

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import anilib.i18n.R
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.maybeLocalSnackbarHostState
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel

@Composable
fun <M : Any, F : BaseField<*>> ResourceScreen(
    viewModel: ResourceViewModel<M, F>,
    showRetry: Boolean = true,
    content: @Composable (data: M) -> Unit
) {
    ResourceLoadingSection(viewModel)
    ResourceSaveSection(viewModel, showRetry)
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



@Composable
private fun <M : Any, F : BaseField<*>> ResourceSaveSection(
    viewModel: ResourceViewModel<M, F>,
    showRetry: Boolean
) {
    when (viewModel.saveResource) {
        is ResourceState.Error -> {
            val snackbar = maybeLocalSnackbarHostState()
            if(snackbar == null){
                viewModel.saveResource = null
                return
            }

            val failedToSave = stringResource(id = R.string.failed_to_save)
            val retry = stringResource(id = anilib.i18n.R.string.retry)
            LaunchedEffect(viewModel) {
                when (snackbar.showSnackbar(
                    failedToSave, if(showRetry) retry else null, duration = SnackbarDuration.Long
                )) {
                    SnackbarResult.Dismissed -> {
                        viewModel.saveResource = null
                    }

                    SnackbarResult.ActionPerformed -> {
                        viewModel.save()
                    }
                }
            }
        }

        is ResourceState.Success ->{
            val snackbar = maybeLocalSnackbarHostState()
            if(snackbar == null){
                viewModel.saveResource = null
                return
            }

            val savedMsg = stringResource(id = R.string.saved_successfully)
            LaunchedEffect(viewModel) {
                when (snackbar.showSnackbar(savedMsg, withDismissAction = true, duration = SnackbarDuration.Short)) {
                    SnackbarResult.Dismissed -> {
                        viewModel.saveResource = null
                    }
                    else->{}
                }
            }
        }

        else -> {}
    }
}
