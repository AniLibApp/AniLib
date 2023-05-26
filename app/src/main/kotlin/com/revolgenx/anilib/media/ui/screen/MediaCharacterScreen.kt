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
import com.revolgenx.anilib.character.ui.model.CharacterEdgeModel
import com.revolgenx.anilib.character.ui.screen.CharacterScreen
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        navigator.characterScreen(character.id)
                    },
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.Start
                )
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp),
                    imageUrl = character.image?.image,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 2.dp)
                ) {
                    Text(text = character.name?.full.naText())
                    characterEdgeModel.role?.let {
                        stringArrayResource(id = R.array.character_role).getOrNull(it.ordinal)
                            ?.let {
                                Text(text = it, fontSize = 11.sp)
                            }
                    }
                }
            }
            voiceActors?.firstOrNull()?.let { staff ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable {
                            navigator.push(StaffScreen(staff.id))
                        },
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        alignment = Alignment.End
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(vertical = 2.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = staff.name?.full.naText(),
                            textAlign = TextAlign.End
                        )
                        staff.languageV2?.let {
                            Text(
                                text = it,
                                fontSize = 11.sp,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(80.dp),
                        imageUrl = staff.image?.image,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        previewPlaceholder = R.drawable.bleach
                    )
                }
            }

        }
    }
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