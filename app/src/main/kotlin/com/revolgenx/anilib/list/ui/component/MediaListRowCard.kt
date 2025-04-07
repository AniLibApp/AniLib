package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaStatsBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes

@Composable
fun MediaListRowCard(
    list: MediaListModel,
    showIncreaseButton: Boolean,
    increaseProgress: OnClick,
    onLongClick: OnLongClick,
    onClick: OnClick,
) {
    val media = list.media ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(min = 160.dp)
            .height(IntrinsicSize.Min),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
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

                MediaStatsBadge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                    media = media
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp, top = 6.dp)
            ) {


                MediaTitleType { type ->
                    MediumText(
                        text = media.title?.title(type).naText(),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                    )
                }


                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
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


                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RegularText(
                        stringResource(id = media.format.toStringRes()),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                    )

                    RegularText(text = "~", fontSize = 11.sp)

                    RegularText(
                        stringResource(id = media.status.toStringRes()),
                        color = media.status.toColor(),
                        fontSize = 11.sp,
                    )
                }


                RegularText(
                    text = "${media.startDate?.toString().naText()} ~ ${
                        media.endDate?.toString().naText()
                    }",
                    fontSize = 11.sp,
                )


                MediaListEntryScore(list = list)


                Spacer(modifier = Modifier.weight(1f))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MediaListEntryProgressBehind(list = list)

                    Spacer(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    )

                    val progress = list.progressState?.value

                    RegularText(
                        modifier = Modifier.padding(2.dp),
                        text = "${progress.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                        fontSize = 13.sp
                    )

                    val canShowIncreaseButton =
                        progress == null || media.totalEpisodesOrChapters == null || progress < media.totalEpisodesOrChapters

                    if(showIncreaseButton && canShowIncreaseButton){
                        Box(
                            modifier = Modifier.clickable {
                                increaseProgress()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(3.dp)
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


@Preview
@Composable
private fun MediaListRowCardPreview() {
    MaterialTheme {
        Surface {
            MediaListRowCard(
                list = MediaListModel(media = MediaModel(title = MediaTitleModel(romaji = "1234444444444444444444444444444444444"))),
                showIncreaseButton = true,
                onClick = {},
                onLongClick = {},
                increaseProgress = {}
            )
        }
    }
}