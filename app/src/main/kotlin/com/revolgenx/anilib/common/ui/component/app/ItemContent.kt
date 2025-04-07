package com.revolgenx.anilib.common.ui.component.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClick


@Composable
fun CharacterOrStaffRowItemContentEnd(
    text: String,
    subTitle: String?,
    imageUrl: String?,
    onClick: OnClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            alignment = Alignment.End
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            MediumText(
                text = text,
                textAlign = TextAlign.End
            )
            subTitle?.let {
                LightText(
                    modifier = Modifier.fillMaxWidth(),
                    text = it,
                    textAlign = TextAlign.End,
                )
            }
        }
        ImageAsync(
            modifier = Modifier
                .fillMaxHeight()
                .width(72.dp),
            imageUrl = imageUrl,
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            previewPlaceholder = R.drawable.bleach
        )
    }
}