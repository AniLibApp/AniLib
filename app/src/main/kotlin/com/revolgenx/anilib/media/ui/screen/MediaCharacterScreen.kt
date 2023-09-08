package com.revolgenx.anilib.media.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.revolgenx.anilib.character.ui.component.CharacterStaffCard
import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.type.MediaType
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaCharacterScreen(
    viewModel: MediaCharacterViewModel,
    mediaType: MediaType?
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { characterEdgeModel ->
        characterEdgeModel ?: return@LazyPagingList
        MediaCharacterItem(characterEdgeModel)
    }
}

@Composable
private fun MediaCharacterItem(
    characterEdgeModel: CharacterEdgeModel
) {
    val character = characterEdgeModel.node ?: return
    val voiceActors = characterEdgeModel.voiceActors
    val navigator = LocalMainNavigator.current
    CharacterStaffCard(
        character = character,
        characterRole = characterEdgeModel.role,
        staff = voiceActors?.firstOrNull(),
        onCharacterClick = { navigator.characterScreen(it) },
        onStaffClick = { navigator.staffScreen(it) }
    )
}


@Preview
@Composable
fun MediaCharacterItemPreview() {
    MediaCharacterItem(
        CharacterEdgeModel(
            voiceActors = listOf(StaffModel())
        )
    )
}