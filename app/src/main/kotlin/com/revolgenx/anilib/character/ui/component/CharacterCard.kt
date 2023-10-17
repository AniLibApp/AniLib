package com.revolgenx.anilib.character.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.common.ui.component.image.ImageOptions

@Composable
fun CharacterCard(model: CharacterModel, onClick: OnClickWithId) {
    CharacterOrStaffCard(
        title = model.name?.full.naText(),
        imageUrl = model.image?.image,
    ) {
        onClick(model.id)
    }
}

@Composable
fun CharacterOrStaffCard(
    title: String,
    subTitle: String? = null,
    imageUrl: String?,
    onClick: OnClick
) {
    Card(
        modifier = Modifier
            .let {
                if (subTitle != null) {
                    it.height(236.dp)
                } else {
                    it.height(224.dp)
                }
            }
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        ) {
            ImageAsync(
                modifier = Modifier
                    .height(165.dp)
                    .fillMaxWidth(),
                imageUrl = imageUrl,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 6.dp, end = 6.dp, top = 2.dp, bottom = 1.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MediumText(text = title)
                subTitle?.let {
                    LightText(text = it)
                }
            }
        }
    }
}