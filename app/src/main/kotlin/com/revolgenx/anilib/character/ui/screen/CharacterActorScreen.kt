package com.revolgenx.anilib.character.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.character.ui.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.character.ui.component.CharacterOrStaffCard
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen

@Composable
fun CharacterActorScreen(viewModel: CharacterActorViewModel) {
    val navigator = localNavigator()
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }
    ResourceScreen(
        viewModel = viewModel
    ) { staffModels ->
        LazyPagingList(
            items = staffModels,
            type = ListPagingListType.GRID,
            gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
        ) { staff ->
            staff ?: return@LazyPagingList
            CharacterOrStaffCard(
                title = staff.name?.full.naText(),
                subTitle = staff.languageV2,
                imageUrl = staff.image?.image
            ) {
                navigator.staffScreen(staff.id)
            }
        }
    }
}