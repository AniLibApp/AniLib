package com.revolgenx.anilib.character.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.naInt
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.about.AboutScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen

@Composable
fun CharacterAboutScreen(
    viewModel: CharacterAboutViewModel
) {
    val navigator = localNavigator()
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    ResourceScreen(resourceState = viewModel.resource.value, refresh = {
        viewModel.refresh()
    }) { character ->
        AboutScreen(
            name = character.name?.full.naText(),
            alternative = character.name?.alternativeText,
            imageUrl = character.image?.image,
            favourites = character.favourites.naInt(),
            isFavourite = character.isFavourite.value,
            description = character.description,
            spannedDescription = character.spannedDescription,
            onFavouriteClick = {
                /*todo favourite*/
            },
            onImageClick = {
                navigator.imageViewerScreen(it)
            }
        )
    }
}