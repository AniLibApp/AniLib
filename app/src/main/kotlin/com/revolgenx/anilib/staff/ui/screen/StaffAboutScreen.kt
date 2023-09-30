package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.about.AboutScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel

@Composable
fun StaffAboutScreen(
    viewModel: StaffAboutViewModel
) {
    val navigator = localNavigator()
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    ResourceScreen(viewModel = viewModel) { staff ->
        AboutScreen(
            name = staff.name?.full.naText(),
            alternative = staff.name?.alternativeText,
            imageUrl = staff.image?.image,
            favourites = staff.favourites.orZero(),
            isFavourite = staff.isFavourite.value,
            description = staff.description,
            spannedDescription = staff.spannedDescription,
            onFavouriteClick = {
                /*todo favourite*/
            },
            onImageClick = {
                navigator.imageViewerScreen(it)
            }
        )
    }
}
