package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.toStringResource
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.list.ui.model.toColor
import com.revolgenx.anilib.list.ui.model.toImageVector
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes
import anilib.i18n.R as I18nR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaCard(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 248.dp,
    footerContent: @Composable (ColumnScope.() -> Unit)? = null,
    onMediaClick: OnMediaClick
) {
    Card(
        modifier = Modifier
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it.fillMaxWidth()
                }
            }
            .height(height)
            .padding(4.dp),
        onClick = {
            onMediaClick(media.id, media.type)
        }
    ) {
        MediaCardContent(media) {
            Spacer(modifier = Modifier.weight(1f))
            if (footerContent != null) {
                footerContent()
            } else {
                val format = media.format.toStringRes().toStringResource()
                val status = media.status.toStringRes().toStringResource()
                val statusColor = media.status.toColor()
                val year = media.seasonYear.naText()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SmallLightText(
                        text = status,
                        color = statusColor,
                        lineHeight = 11.sp
                    )

                    media.mediaListEntry?.let {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = it.status.toImageVector(),
                            tint = it.status.toColor(),
                            contentDescription = null
                        )
                    }
                }

                SmallLightText(
                    text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
                    lineHeight = 11.sp
                )

            }
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
fun ColumnScope.MediaCardContent(
    mediaModel: MediaModel,
    footerContent: @Composable ColumnScope.() -> Unit
) {
    MediaCoverImageType { type ->
        ImageAsync(
            modifier = Modifier
                .height(165.dp)
                .fillMaxWidth(),
            imageUrl = mediaModel.coverImage?.image(type),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            previewPlaceholder = R.drawable.bleach
        )
    }

    Column(
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        MediaTitleType { type ->
            MediumText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = mediaModel.title?.title(type).naText()
            )
        }
        footerContent()
    }
}


@Composable
fun MediaItemRowContent(
    media: MediaModel,
    content: @Composable () -> Unit = {},
    onMediaClick: OnMediaClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMediaClick(media.id, media.type)
            },
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            alignment = Alignment.Start
        )
    ) {
        MediaCoverImageType { type ->
            ImageAsync(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(72.dp),
                imageUrl = media.coverImage?.image(type),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 2.dp)
        ) {

            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = media.title?.title(type).naText(),
                )
            }


            Text(
                stringResource(id = media.status.toStringRes()),
                color = media.status.toColor(),
                fontSize = 12.sp
            )

            val format = stringResource(id = media.format.toStringRes())
            val year = media.seasonYear.naText()

            SmallLightText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
            )

            content()
        }
    }
}

@Composable
fun MediaRowItemContentEnd(
    media: MediaModel,
    content: @Composable () -> Unit = {},
    onMediaClick: OnMediaClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onMediaClick(media.id, media.type)
            },
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            alignment = Alignment.End
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 2.dp),
            horizontalAlignment = Alignment.End
        ) {


            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = media.title?.title(type).naText(),
                    textAlign = TextAlign.End
                )
            }


            Text(
                stringResource(id = media.status.toStringRes()),
                color = media.status.toColor(),
                fontSize = 12.sp,
                textAlign = TextAlign.End,
            )

            val format = stringResource(id = media.format.toStringRes())

            val year = media.seasonYear.naText()

            SmallLightText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
                textAlign = TextAlign.End,
            )

            content()
        }

        MediaCoverImageType { type ->
            ImageAsync(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(72.dp),
                imageUrl = media.coverImage?.image(type),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
        }
    }
}