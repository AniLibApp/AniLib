package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaStatsBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.toStringRes

@Composable
fun MediaListSmallRowCard(
    list: MediaListModel,
    showIncreaseButton: Boolean,
    increaseProgress: OnClick,
    onLongClick: OnLongClick,
    onClick: OnClick,
) {
    val media = list.media ?: return

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(85.dp),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                MediaCoverImageType { type ->
                    ImageAsync(
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight(),
                        imageUrl = media.coverImage?.image(type),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop, alignment = Alignment.Center
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
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(top = 4.dp)
                ) {
                    MediaTitleType { type ->
                        MediumText(
                            text = media.title?.title(type).naText(),
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                        )
                    }


                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 2.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Column(
                            modifier = Modifier,
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                RegularText(
                                    modifier = Modifier.padding(end = 10.dp),
                                    text = stringResource(id = media.format.toStringRes()),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 11.sp,
                                )

                                MediaListEntryScore(list = list)

                            }


                            MediaListEntryProgressBehind(list = list)


                        }


                        Spacer(modifier = Modifier.weight(1f))

                        Box(modifier = Modifier) {

                            RegularText(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .align(Alignment.BottomEnd),
                                text = "${list.progressState?.value.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                                fontSize = 13.sp
                            )
                        }
                    }

                }

                MediaListEntryLinearProgressIndicator(list = list)
            }

            val progress = list.progressState?.value
            val canShowIncreaseButton =
                progress == null || media.totalEpisodesOrChapters == null || progress < media.totalEpisodesOrChapters


            if(showIncreaseButton && canShowIncreaseButton){
                IconButton(
                    onClick = increaseProgress
                ) {
                    Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaListSmallRowCardPreview() {
    MediaListSmallRowCard(
        list = MediaListModel(
            media = MediaModel(
                title = MediaTitleModel(romaji = ""),
                currentEpisode = 10
            ),
            progressState = remember {
                mutableStateOf(2)
            },
            score = 10.0
        ),
        onClick = {},
        onLongClick = {},
        increaseProgress = {},
        showIncreaseButton = true
    )
}