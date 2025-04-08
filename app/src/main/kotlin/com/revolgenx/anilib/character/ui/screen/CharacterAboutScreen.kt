package com.revolgenx.anilib.character.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.common.ext.anilify
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.markdown
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.about.EntityAboutScreenContent
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen

@Composable
fun CharacterAboutScreen(
    viewModel: CharacterAboutViewModel
) {
    val navigator = localNavigator()
    val context = localContext()
    val scope = rememberCoroutineScope()
    val snackbarHostState = localSnackbarHostState()
    val localUser = localUser()
    val isLoggedIn = localUser.isLoggedIn
    viewModel.getResource()

    LaunchedEffect(viewModel.showToggleErrorMsg.value) {
        if (viewModel.showToggleErrorMsg.value) {
            snackbarHostState.showSnackbar(context.getString(anilib.i18n.R.string.operation_failed), withDismissAction = true)
            viewModel.showToggleErrorMsg.value = false
        }
    }

    ResourceScreen(viewModel = viewModel) { character ->
        val description = remember(viewModel) {
            markdown.toMarkdown(character.generalDescription(context) + anilify(character.description))
        }
        EntityAboutScreenContent(
            name = character.name?.full.naText(),
            alternative = character.name?.alternativeText,
            imageUrl = character.image?.image,
            favourites = character.favourites.orZero(),
            isFavourite = character.isFavourite.value,
            description = character.description,
            spannedDescription = description,
            onFavouriteClick = {
                if(isLoggedIn){
                    viewModel.toggleFavorite()
                }else{
                    snackbarHostState.showLoginMsg(context, scope)
                }
            },
            onImageClick = {
                navigator.imageViewerScreen(it)
            }
        )
    }
}