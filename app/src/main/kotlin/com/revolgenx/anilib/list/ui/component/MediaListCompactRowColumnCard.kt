package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.MediaListStatus


@Composable
fun MediaListCompactRowColumnCard(
    list: MediaListModel,
    showIncreaseButton: Boolean,
    increaseProgress: OnClick,
    onLongClick: OnLongClick,
    onClick: OnClick
) {

    val media = list.media ?: return

    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp)
            .height(100.dp),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Row {
            Box {
                MediaCoverImageType { type ->
                    ImageAsync(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(66.dp),
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
                    media = media,
                    fontSize = 10.sp,
                    iconSize = 12.dp
                )

            }

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Column(modifier = Modifier.padding(horizontal = 3.dp).padding(top = 2.dp)) {
                    MediaTitleType { type ->
                        MediumText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MediaCardTitleBottomPadding),
                            text = media.title?.title(type).naText(),
                            fontSize = 13.sp,
                        )
                    }
                    MediaListEntryScore(modifier = Modifier.padding(bottom = 1.dp), list = list, iconSize = 13.dp, fontSize = 11.sp)
                    MediaListEntryProgressBehind(list = list)

                }

                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 3.dp),
                        ) {
                            RegularText(
                                modifier = Modifier.weight(1f),
                                text = stringResource(id = media.format.toStringRes()),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,
                                maxLines = 1
                            )

                            RegularText(
                                text = "${list.progressState?.value.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                                fontSize = 12.sp
                            )
                        }

                        val progress = list.progressState?.value
                        val canShowIncreaseButton =
                            progress == null || media.totalEpisodesOrChapters == null || progress < media.totalEpisodesOrChapters

                        if (showIncreaseButton && canShowIncreaseButton) {
                            Box(
                                modifier = Modifier.clickable { increaseProgress() }
                            ) {

                                Icon(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .size(20.dp),
                                    imageVector = AppIcons.IcPlus,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    MediaListEntryLinearProgressIndicator(list = list)


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaListCompactColumnCardPreview() {
    MediaListCompactRowColumnCard(
        list = MediaListModel(
            media = MediaModel(
                title = MediaTitleModel(romaji = "One Punch Man Season 2"),
                currentEpisode = 10,
                averageScore = 20,
                mediaListEntry = MediaListModel(
                    status = remember {
                        mutableStateOf(MediaListStatus.CURRENT)
                    }
                )
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