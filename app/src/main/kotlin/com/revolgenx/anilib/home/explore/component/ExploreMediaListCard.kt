package com.revolgenx.anilib.home.explore.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnLongClick
import com.revolgenx.anilib.home.explore.ui.screen.ExploreMediaListCardHeight
import com.revolgenx.anilib.home.explore.ui.screen.ExploreMediaListCardWidth
import com.revolgenx.anilib.list.ui.component.MediaListEntryLinearProgressIndicator
import com.revolgenx.anilib.list.ui.component.MediaListEntryProgressBehind
import com.revolgenx.anilib.list.ui.component.MediaListEntryScore
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.component.MediaCardTitleBottomPadding
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaInfoHorizontalPadding
import com.revolgenx.anilib.media.ui.component.MediaInfoVerticalPadding
import com.revolgenx.anilib.media.ui.component.MediaStatsBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes


@Composable
fun ExploreMediaListCard(
    list: MediaListModel,
    increaseProgress: OnClick,
    onLongClick: OnLongClick,
    onClick: OnClick
) {
    val media = list.media ?: return

    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(ExploreMediaListCardWidth)
            .height(ExploreMediaListCardHeight),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Box {
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


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = MediaInfoHorizontalPadding,
                            vertical = MediaInfoVerticalPadding
                        )
                ) {
                    MediaTitleType { type ->
                        MediumText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MediaCardTitleBottomPadding),
                            text = media.title?.title(type).naText()
                        )
                    }


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


                        MediaListEntryScore(list = list)
                        Spacer(modifier = Modifier.weight(1f))
                        RegularText(
                            text = "${list.progressState?.value.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                            fontSize = 13.sp
                        )
                        Box(
                            modifier = Modifier.clickable { increaseProgress() }
                        ) {

                            Icon(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(20.dp),
                                imageVector = AppIcons.IcPlus,
                                contentDescription = null
                            )
                        }
                    }

                    MediaListEntryLinearProgressIndicator(list = list)
                }
            }


        }

    }
}