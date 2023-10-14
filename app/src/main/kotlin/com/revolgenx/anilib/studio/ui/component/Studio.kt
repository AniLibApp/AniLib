package com.revolgenx.anilib.studio.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.button.SmallTextButton
import com.revolgenx.anilib.common.ui.component.common.HeaderText
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.studio.ui.model.StudioModel
import anilib.i18n.R as I18nR


@Composable
fun StudioItem(studio: StudioModel, onMediaClick: OnMediaClick, onClick: OnClickWithId) {
    val medias = studio.media?.nodes.orEmpty()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onClick(studio.id)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            HeaderText(text = studio.name.naText())

            SmallTextButton(
                text = stringResource(id = I18nR.string.view_all)
            ) {
                onClick(studio.id)
            }
        }
        LazyRow {
            items(items = medias) {
                MediaCard(media = it, width = 120.dp, onMediaClick = onMediaClick)
            }
        }
    }
}

