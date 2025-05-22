package com.revolgenx.anilib.list.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import cafe.adriel.voyager.core.screen.Screen
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCheck
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.icons.appicon.IcStarOutline
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnLongClick
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.model.toStringRes
import com.revolgenx.anilib.list.ui.viewmodel.MediaListCompareViewModel
import com.revolgenx.anilib.media.ui.component.MediaCardTitleBottomPadding
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import org.koin.androidx.compose.koinViewModel

class MediaListCompareScreen(
    private val type: MediaType,
    private val userId: Int,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: MediaListCompareViewModel = koinViewModel()
        viewModel.field.also {
            it.type = type
            it.userId = userId
        }
        MediaListCompareScreenContent(viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListCompareScreenContent(viewModel: MediaListCompareViewModel) {
    val navigator = localNavigator()
    ScreenScaffold(
        title = stringResource(R.string.compare_with_your_list),
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()

        LazyPagingList(
            pagingItems = pagingItems,
            onRefresh = {
                viewModel.refresh()
            },
        ) { model ->
            model ?: return@LazyPagingList
            MediaListCompareItem(model, onLongClick = {
                model.media?.let {
                    navigator.mediaListEntryEditorScreen(mediaId = it.id)
                }
            }, onClick = {
                model.media?.let {
                    navigator.mediaScreen(mediaId = it.id, type = it.type)
                }
            })
        }
    }
}


@Composable
fun MediaListCompareItem(mediaList: MediaListModel, onClick: OnClick, onLongClick: OnLongClick) {
    val media = mediaList.media

    Card(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = Modifier
            .heightIn(min = 100.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
            ) {
                MediaCoverImageType { type ->
                    ImageAsync(
                        modifier = Modifier.matchParentSize(),
                        imageUrl = media?.coverImage?.image(type),
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        ),
                        previewPlaceholder = com.revolgenx.anilib.R.drawable.bleach
                    )
                }
            }

            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                MediaTitleType { type ->
                    MediumText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MediaCardTitleBottomPadding),
                        text = media?.title?.title(type).naText(),
                        fontSize = 16.sp
                    )
                }


                MediaListCompareScore(
                    whoseScore = stringResource(R.string.your_score),
                    scoreFormat = mediaList.media?.mediaListEntry?.user?.mediaListOptions?.scoreFormat,
                    score = mediaList.media?.mediaListEntry?.score
                )

                MediaListCompareScore(
                    whoseScore = stringResource(R.string.s_other_score).format(mediaList.user?.name.naText()),
                    scoreFormat = mediaList.user?.mediaListOptions?.scoreFormat,
                    score = mediaList.score
                )


                Text(
                    text = "Your status ${
                        media?.mediaListEntry?.status?.value?.toStringRes(
                            media.type ?: MediaType.ANIME
                        )?.let { stringResource(it) }
                    }",
                    fontSize = textSize
                )

                Text(
                    text = "${mediaList.user?.name.naText()} status ${
                        mediaList.status.value?.toStringRes(
                            media?.type ?: MediaType.ANIME
                        )?.let { stringResource(it) }
                    }",
                    fontSize = textSize
                )

            }
        }

    }
}

private val textSize = 14.sp

@Composable
private fun MediaListCompareScore(
    whoseScore: String,
    scoreFormat: ScoreFormat?,
    score: Double?,
) {
    val userScore = score.let {
        when (scoreFormat) {
            ScoreFormat.POINT_10_DECIMAL -> it?.toString().naText()
            ScoreFormat.POINT_3 -> if (it == null) "?" else ""
            else -> it?.toInt()?.toString().naText()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val titleScore = "$whoseScore $userScore"
        Text(
            text = titleScore,
            fontSize = textSize
        )

        val scoreIcon = remember { getScoreIcon(scoreFormat, score) }

        scoreIcon?.let {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun getScoreIcon(
    scoreFormat: ScoreFormat?,
    score: Double?
): ImageVector? {
    val isHappy = score == 3.0
    val isNeutral = score == 2.0
    val isSad = score == 1.0

    return when (scoreFormat) {
        ScoreFormat.POINT_5 -> {
            AppIcons.IcStar
        }

        ScoreFormat.POINT_3 -> {
            if (isSad) AppIcons.IcSad else if (isNeutral) AppIcons.IcNeutral else if (isHappy) AppIcons.IcHappy else null
        }

        else -> {
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaListCompareScorePreview() {
    MediaListCompareScore(
        scoreFormat = ScoreFormat.POINT_3,
        score = 3.0,
        whoseScore = stringResource(R.string.s_other_score)
    )
}

@Preview
@Composable
private fun MediaListCompareItemPreview() {
    MediaListCompareItem(
        MediaListModel(
            media = MediaModel(
                id = 1,
                title = MediaTitleModel(romaji = "Bleach")
            )
        ),{}
    ) {}
}