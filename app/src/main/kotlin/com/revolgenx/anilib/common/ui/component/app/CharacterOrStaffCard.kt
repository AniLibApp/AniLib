package com.revolgenx.anilib.common.ui.component.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClick
import com.skydoves.landscapist.ImageOptions


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
            AsyncImage(
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
                    .padding(start = 6.dp,end = 6.dp, top = 2.dp, bottom = 1.dp),
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
