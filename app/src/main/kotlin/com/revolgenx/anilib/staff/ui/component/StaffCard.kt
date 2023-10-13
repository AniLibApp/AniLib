package com.revolgenx.anilib.staff.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.component.CharacterOrStaffCard
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.app.CharacterOrStaffRowItemContentEnd
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.media.ui.component.MediaItemRowContent
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.common.ui.component.image.ImageOptions


@Composable
fun StaffCard(model: StaffModel, onClick: OnClickWithId) {
    CharacterOrStaffCard(
        title = model.name?.full.naText(),
        subTitle = model.languageV2,
        imageUrl = model.image?.image
    ) {
        onClick(model.id)
    }
}

@Composable
fun StaffRowCard(
    staff: StaffModel,
    role: String? = null,
    onRoleClick: OnClickWithValue<String>? = null,
    onClick: OnClickWithValue<Int>
) {
    Card(
        modifier = Modifier
            .height(124.dp)
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clickable {
                    onClick(staff.id)
                },
            horizontalArrangement = Arrangement.spacedBy(
                6.dp,
                alignment = Alignment.Start
            )
        ) {
            ImageAsync(
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
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MediumText(text = staff.name?.full.naText())

                role?.let {
                    SmallLightText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRoleClick?.invoke(it)
                            },
                        text = it,
                        maxLines = 4
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
    onMediaClick: OnMediaClick,
    onCharacterClick: OnClickWithValue<Int>
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
                MediaItemRowContent(media = mediaModel) { id, type ->
                    onMediaClick(id, type)
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
                CharacterOrStaffRowItemContentEnd(
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