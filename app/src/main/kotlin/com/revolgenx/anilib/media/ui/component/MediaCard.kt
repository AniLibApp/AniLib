package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.skydoves.landscapist.ImageOptions

@Composable
fun MediaCard(mediaModel: MediaModel, width: Dp? = null, onClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it.fillMaxWidth()
                }
            }
            .height(236.dp)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick(mediaModel.id)
                },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MediaItem(mediaModel)
            val format = mediaModel.format?.let {
                stringArrayResource(id = R.array.media_format)[it.ordinal]
            }.naText()
            val year = mediaModel.seasonYear.naText()
            Box(
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 4.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.format_year).format(format, year),
                    maxLines = 1,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun MediaItem(mediaModel: MediaModel) {
    Column {
        MediaCoverImageType { type ->
            AsyncImage(
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

        MediaTitleType { type ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp, horizontal = 4.dp),
                text = mediaModel.title?.title(type).naText(),
                maxLines = 2,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}


@Composable
fun MediaRowItem(
    mediaModel: MediaModel,
    onClick: OnClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.Start
        )
    ) {
        MediaCoverImageType { type ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(72.dp),
                imageUrl = mediaModel.coverImage?.image(type),
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = mediaModel.title?.title(type).naText(),
                    maxLines = 2,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            val format = mediaModel.format?.let {
                stringArrayResource(id = R.array.media_format)[it.ordinal]
            }.naText()
            val year = mediaModel.seasonYear.naText()

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.format_year).format(format, year),
                maxLines = 1,
                fontSize = 11.sp,
                letterSpacing = 0.2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
