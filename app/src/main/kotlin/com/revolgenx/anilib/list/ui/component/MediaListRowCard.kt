package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
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
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaStatsBadge
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.ScoreFormat

@Composable
fun MediaListRowCard(list: MediaListModel) {
    val media = list.media ?: return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(166.dp),
        onClick = {

        },
        onLongClick = {

        }
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
                    .padding(bottom = 4.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MediaTitleType { type ->
                        MediumText(
                            modifier = Modifier.weight(1f),
                            text = media.title?.title(type).naText(),
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                        )
                    }

                    Box(modifier = Modifier.clickable {  /*TODO*/ }) {
                        Icon(
                            modifier = Modifier
                                .padding(3.dp)
                                .size(20.dp),
                            imageVector = AppIcons.IcPlus,
                            contentDescription = null
                        )
                    }
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


                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RegularText(
                        stringResource(id = media.format.toStringRes()),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                    )

                    RegularText(text = "~")

                    RegularText(
                        stringResource(id = media.status.toStringRes()),
                        color = media.status.toColor(),
                        fontSize = 12.sp,
                    )
                }


                RegularText(
                    text = "${media.startDate?.toString().naText()} ~ ${
                        media.endDate?.toString().naText()
                    }",
                    fontSize = 12.sp,
                )


                val scoreFormat = list.user?.mediaListOptions?.scoreFormat
                val score = list.score?.takeIf { it != 0.0 }

                score?.let {
                    val userScore = score.let {
                        when (scoreFormat) {
                            ScoreFormat.POINT_10_DECIMAL -> it.toString()
                            ScoreFormat.POINT_3 -> ""
                            else -> it.toInt().toString()
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RegularText(
                            stringResource(id = anilib.i18n.R.string.score_s).format(
                                userScore
                            ),
                            fontSize = 12.sp,
                        )

                        val scoreIcon = when (scoreFormat) {
                            ScoreFormat.POINT_5 -> {
                                AppIcons.IcStar
                            }

                            ScoreFormat.POINT_3 -> {
                                if (list.isSad) AppIcons.IcSad else if (list.isNeutral) AppIcons.IcNeutral else AppIcons.IcHappy
                            }

                            else -> {
                                null
                            }
                        }
                        scoreIcon?.let {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = it,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                    }
                }


                Spacer(modifier = Modifier.weight(1f))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val progressBehind = list.progress?.let { progress ->
                        media.currentEpisode?.minus(progress)
                    }?.takeIf { it > 0 }

                    progressBehind?.let {
                        LightText(
                            text = pluralStringResource(
                                id = anilib.i18n.R.plurals.s_episodes_behind,
                                progressBehind
                            ).format(progressBehind)
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    )
                    RegularText(
                        text = "${list.progress.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                        fontSize = 13.sp
                    )

                }

                val progress = remember {
                    list.progress?.toFloat()
                        ?.div(media.totalEpisodesOrChapters?.takeIf { it != 0 }?.toFloat() ?: 1f)
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


@Preview
@Composable
private fun MediaListRowCardPreview() {
    MaterialTheme {
        Surface {
            MediaListRowCard(
                list = MediaListModel(media = MediaModel())
            )
        }
    }
}