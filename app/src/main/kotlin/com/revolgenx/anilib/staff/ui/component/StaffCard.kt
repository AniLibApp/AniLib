package com.revolgenx.anilib.staff.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.media.CardRowEndItem
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.media.ui.component.MediaRowItem
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.skydoves.landscapist.ImageOptions

@Composable
fun StaffCard(staff: StaffModel, onClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .height(224.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick.invoke(staff.id)
            }) {
            AsyncImage(
                modifier = Modifier
                    .height(165.dp)
                    .fillMaxWidth(),
                imageUrl = staff.image?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    staff.name?.full.naText(),
                    maxLines = 2,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}


@Composable
fun StaffRowCard(staff: StaffModel, role: String? = null, onClick: OnClickWithId) {
    Card(
        modifier = Modifier
            .height(124.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable {
                    onClick(staff.id)
                },
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.Start
            )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(72.dp),
                imageUrl = staff.image?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = staff.name?.full.naText(), maxLines = 2)

                role?.let {
                    Text(
                        it,
                        maxLines = 3,
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        color = colorScheme().onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.2.sp,
                    )
                }
            }
        }
    }
}


@Composable
fun StaffMediaCharacterCard(
    mediaModel: MediaModel,
    character: CharacterModel?,
    onMediaClick: OnClickWithId,
    onCharacterClick: OnClickWithId
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),

                ) {
                MediaRowItem(mediaModel = mediaModel) {
                    onMediaClick(mediaModel.id)
                }
            }

            character ?: return@Row

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val role = mediaModel.characterRole?.let {
                    stringArrayResource(id = R.array.character_role).getOrNull(it.ordinal)
                }
                CardRowEndItem(
                    text = character.name?.full.naText(),
                    subTitle = role,
                    imageUrl = character.image?.image
                ) {
                    onCharacterClick(character.id)
                }
            }
        }
    }
}