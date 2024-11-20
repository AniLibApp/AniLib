package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnLongClick
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.component.MediaCardTitleBottomPadding
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaStatsBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes

@Composable
fun MediaListColumnCard(
    list: MediaListModel,
    showIncreaseButton: Boolean,
    increaseProgress: OnClick,
    onLongClick: OnLongClick,
    onClick: OnClick
) {
    val media = list.media ?: return

    Card(
        modifier = Modifier
            .padding(6.dp)
            .width(160.dp)
            .height(270.dp),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }

            MediaStatsBadge(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
                media = media
            )

        }

        Column(
            modifier = Modifier
                .padding(3.dp)
        ) {
            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MediaCardTitleBottomPadding),
                    text = media.title?.title(type).naText()
                )
            }

            Spacer(modifier = Modifier.size(1.dp))

            Row {
                val format = stringResource(id = media.format.toStringRes())
                val status = stringResource(id = media.status.toStringRes())
                val statusColor = media.status.toColor()
                val year = media.seasonYear



                MediaListEntryProgressBehind(list = list) {
                    LightText(
                        text = status,
                        color = statusColor,
                        lineHeight = 11.sp
                    )


                    Spacer(modifier = Modifier.size(6.dp))

                    LightText(
                        text = year?.let {
                            stringResource(id = anilib.i18n.R.string.s_dot_s).format(
                                format,
                                year
                            )
                        } ?: format,
                        lineHeight = 11.sp
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {


                MediaListEntryScore(list = list, fontSize = 11.sp)
                Spacer(modifier = Modifier.weight(1f))

                val progress = list.progressState?.value
                RegularText(
                    text = "${progress.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                    fontSize = 11.sp,
                    lineHeight = 12.sp
                )

                val canShowIncreaseButton =
                    progress == null || media.totalEpisodesOrChapters == null || progress < media.totalEpisodesOrChapters

                if (showIncreaseButton && canShowIncreaseButton) {
                    Box(
                        modifier = Modifier.clickable { increaseProgress() }
                    ) {

                        Icon(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(18.dp),
                            imageVector = AppIcons.IcPlus,
                            contentDescription = null
                        )
                    }
                }
            }

        }


        MediaListEntryLinearProgressIndicator(list = list)
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaListColumnCardPreview() {
    MediaListColumnCard(
        list = MediaListModel(
            media = MediaModel(
                title = MediaTitleModel(romaji = "One Punch Man Season 2"),
                currentEpisode = 10
            ),
            progressState = remember {
                mutableStateOf(2)
            },
            score = 10.0
        ),
        showIncreaseButton = true,
        onClick = {},
        onLongClick = {},
        increaseProgress = {}
    )
}