package com.revolgenx.anilib.character.ui.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.type.CharacterRole
import com.skydoves.landscapist.ImageOptions

@Composable
fun CharacterStaffCard(
    character: CharacterModel,
    characterRole: CharacterRole?,
    staff: StaffModel?,
    onCharacterClick: OnClickWithId,
    onStaffClick: OnClickWithId
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
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        onCharacterClick(character.id)
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
                    characterRole?.let {
                        stringArrayResource(id = R.array.character_role).getOrNull(it.ordinal)
                            ?.let {
                                Text(
                                    text = it,
                                    fontSize = 11.sp,
                                    color = colorScheme().onSurfaceVariant,
                                    fontWeight = FontWeight.Light
                                )
                            }
                    }
                }
            }
            staff?.let { staff ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable {
                            onStaffClick(staff.id)
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
                                textAlign = TextAlign.End,
                                fontWeight = FontWeight.Light,
                                color = colorScheme().onSurfaceVariant,
                            )
                        }
                    }
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
                }
            }

        }
    }
}