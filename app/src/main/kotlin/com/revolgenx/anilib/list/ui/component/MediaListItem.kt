package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.RegularText
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaInfoBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes

@Composable
fun MediaListItem(list: MediaListModel) {
    val media = list.media ?: return
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(8.dp), onClick = {

    }, onLongClick = {

    }) {
        Row {
            Box {
                MediaCoverImageType { type ->
                    ImageAsync(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(104.dp),
                        imageUrl = media.coverImage?.image(type),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop, alignment = Alignment.Center
                        ),
                        previewPlaceholder = R.drawable.bleach
                    )
                }

                MediaInfoBadge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp), media = media
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp, top = 8.dp),
            ) {
                MediaTitleType { type ->
                    MediumText(
                        text = media.title?.title(type).naText(),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                    )
                }

                Row(
                    modifier = Modifier.padding(PaddingValues(vertical = 2.dp)),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    media.genres?.take(4)?.map { genre ->
                        LightText(
                            text = genre,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                RegularText(
                    stringResource(id = media.status.toStringRes()),
                    color = media.status.toColor(),
                    fontSize = 12.sp,
                )

                RegularText(
                    text = "${media.startDate?.toString().naText()} ~ ${
                        media.endDate?.toString().naText()
                    }",
                    fontSize = 12.sp,
                )

                Row {

                    RegularText(
                        stringResource(id = media.format.toStringRes()),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                    )

                    RegularText(
                        stringResource(id = anilib.i18n.R.string.ep_d_s).format(
                            media.episodes.naText(), media.duration.naText()
                        ),
                        fontSize = 12.sp,
                    )

                }

                Spacer(modifier = Modifier.weight(1f))

                RegularText(text = "${list.progress} / ${media.episodes}", fontSize = 13.sp)
                val progress = remember {
                    list.progress?.toFloat()?.div(media.episodes?.takeIf { it != 0 }?.toFloat() ?: 1f)
                }
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { progress ?: 0f },
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}