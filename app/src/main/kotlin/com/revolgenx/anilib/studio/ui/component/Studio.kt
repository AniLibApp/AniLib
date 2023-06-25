package com.revolgenx.anilib.studio.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.theme.colorScheme
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.media.ui.component.MediaItemCard
import com.revolgenx.anilib.studio.ui.model.StudioModel


@Composable
fun StudioItem(studio: StudioModel, onMediaClick: OnMediaClick, onClick: OnClickWithId) {
    val medias = studio.media?.nodes ?: emptyList()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onClick(studio.id)
                    },
                text = studio.name.naText(),
                fontSize = 15.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .clickable {
                        onClick(studio.id)
                    },
                text = stringResource(id = R.string.view_all),
                color = colorScheme().onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        LazyRow {
            items(items = medias) {
                MediaItemCard(media = it, width = 120.dp, onMediaClick = onMediaClick)
            }
        }
    }
}

