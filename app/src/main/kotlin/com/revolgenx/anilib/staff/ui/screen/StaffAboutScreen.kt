package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.screen.about.EntityAboutScreenContent
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel

@Composable
fun StaffAboutScreen(
    viewModel: StaffAboutViewModel
) {
    val navigator = localNavigator()
    val context = localContext()
    val scope = rememberCoroutineScope()
    val localUser = localUser()
    val isLoggedIn = localUser.isLoggedIn
    val snackbarHostState = localSnackbarHostState()

    viewModel.getResource()
    LaunchedEffect(viewModel.showToggleErrorMsg.value) {
        if(viewModel.showToggleErrorMsg.value){
            snackbarHostState.showSnackbar(context.getString(anilib.i18n.R.string.operation_failed), withDismissAction = true)
            viewModel.showToggleErrorMsg.value = false
        }
    }

    ResourceScreen(viewModel = viewModel) { staff ->
        EntityAboutScreenContent(
            name = staff.name?.full.naText(),
            alternative = staff.name?.alternativeText,
            imageUrl = staff.image?.image,
            favourites = staff.favourites.orZero(),
            isFavourite = staff.isFavourite.value,
            description = staff.description,
            spannedDescription = staff.spannedDescription,
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
