package com.revolgenx.anilib.character.ui.component

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
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.app.CharacterOrStaffRowItemContentEnd
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.type.CharacterRole
import com.revolgenx.anilib.common.ui.component.image.ImageOptions

@Composable
fun CharacterStaffCard(
    character: CharacterModel,
    characterRole: CharacterRole?,
    staff: StaffModel?,
    onCharacterClick: OnClickWithValue<Int>,
    onStaffClick: OnClickWithValue<Int>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(124.dp)
            .padding(6.dp)
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
                        onCharacterClick(character.id)
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
                        .padding(vertical = 4.dp)
                ) {
                    MediumText(text = character.name?.full.naText())
                    characterRole?.let {
                        stringArrayResource(id = R.array.character_role).getOrNull(it.ordinal)
                            ?.let {
                                SmallLightText(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it
                                )
                            }
                    }
                }
            }
            staff?.let { staff ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    CharacterOrStaffRowItemContentEnd(
                        text = staff.name?.full.naText(),
                        subTitle = staff.languageV2,
                        imageUrl = staff.image?.image
                    ) {
                        onStaffClick(staff.id)
                    }
                }
            }

        }
    }
}