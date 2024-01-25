package com.revolgenx.anilib.character.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.about.AboutScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen

@Composable
fun CharacterAboutScreen(
    viewModel: CharacterAboutViewModel
) {
    val navigator = localNavigator()
    val context = localContext()
    val snackbarHostState = localSnackbarHostState()
    viewModel.getResource()

    LaunchedEffect(viewModel.showToggleErrorMsg.value) {
        if (viewModel.showToggleErrorMsg.value) {
            snackbarHostState.showSnackbar(context.getString(anilib.i18n.R.string.operation_failed), withDismissAction = true)
            viewModel.showToggleErrorMsg.value = false
        }
    }

    ResourceScreen(viewModel = viewModel) { character ->
        AboutScreen(
            name = character.name?.full.naText(),
            alternative = character.name?.alternativeText,
            imageUrl = character.image?.image,
            favourites = character.favourites.orZero(),
            isFavourite = character.isFavourite.value,
            description = character.description,
            spannedDescription = character.spannedDescription,
            onFavouriteClick = {
                viewModel.toggleFavorite()
            },
            onImageClick = {
                navigator.imageViewerScreen(it)
            }
        )
    }
}