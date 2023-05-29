package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.component.CharacterStaffCard
import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.screen.StaffScreen
import com.revolgenx.anilib.type.MediaType
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaCharacterScreen(
    mediaId: Int,
    mediaType: MediaType?,
    viewModel: MediaCharacterViewModel = koinViewModel()
) {
    LaunchedEffect(mediaId) {
        viewModel.field.mediaId = mediaId
    }
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