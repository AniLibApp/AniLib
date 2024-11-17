import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
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
import com.revolgenx.anilib.list.ui.component.MediaListEntryLinearProgressIndicator
import com.revolgenx.anilib.list.ui.component.MediaListEntryProgressBehind
import com.revolgenx.anilib.list.ui.component.MediaListEntryScore
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
fun MediaListCompactColumnCard(
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
            .width(104.dp)
            .height(250.dp),
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Box {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .height(158.dp)
                        .fillMaxWidth(),
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
                .weight(1f)
        ) {
            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MediaCardTitleBottomPadding),
                    text = media.title?.title(type).naText(),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            MediaListEntryProgressBehind(list = list) {
                Row {
                    val status = stringResource(id = media.status.toStringRes())
                    val statusColor = media.status.toColor()

                    LightText(
                        text = status,
                        color = statusColor,
                        fontSize = 10.sp,
                        lineHeight = 10.sp
                    )

                    val format = stringResource(id = media.format.toStringRes())
                    val year = media.seasonYear

                    LightText(
                        modifier = Modifier.padding(start = 6.dp),
                        text = year?.let {
                            stringResource(id = anilib.i18n.R.string.s_dot_s).format(
                                format,
                                year
                            )
                        } ?: format,
                        fontSize = 10.sp,
                        lineHeight = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                val progress = list.progressState?.value

                Column(modifier = Modifier.weight(1f)) {
                    MediaListEntryScore(
                        list = list,
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        iconSize = 12.dp
                    )

                    RegularText(
                        text = "${progress.naText()} / ${media.totalEpisodesOrChapters.naText()}",
                        fontSize = 11.sp,
                        lineHeight = 12.sp
                    )
                }


                val canShowIncreaseButton =
                    progress == null || media.totalEpisodesOrChapters == null || progress < media.totalEpisodesOrChapters

                if (showIncreaseButton && canShowIncreaseButton) {
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
            }

        }


        MediaListEntryLinearProgressIndicator(list = list)
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaListColumnCardPreview() {
    MediaListCompactColumnCard(
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