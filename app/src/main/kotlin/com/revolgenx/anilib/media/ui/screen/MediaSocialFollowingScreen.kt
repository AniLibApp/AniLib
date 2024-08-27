package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.list.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.model.MediaSocialFollowingModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialFollowingScreenViewModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.user.ui.model.MediaListOptionModel
import com.revolgenx.anilib.user.ui.model.UserModel

@Composable
fun MediaSocialFollowingScreen(viewModel: MediaSocialFollowingScreenViewModel) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val navigator = localNavigator()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
    ) { following ->
        following ?: return@LazyPagingList
        MediaSocialFollowingItem(following){
            following.user?.id?.let {
                navigator.userScreen(it)
            }
        }
    }
}

@Composable
private fun MediaSocialFollowingItem(followingModel: MediaSocialFollowingModel, onClick: OnClick) {
    Card(
        modifier = Modifier.padding(4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            val user = followingModel.user

            ImageAsync(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                imageUrl = user?.avatar?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )

            Box(
                modifier = Modifier.weight(1f)
            ) {
                MediumText(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = user?.name.naText(),
                    maxLines = 1
                )
            }


            val listStatus = followingModel.status

            Box(
                modifier = Modifier.weight(1f),
            ) {
                MediumText(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(
                        id = listStatus.toStringRes(
                            followingModel.type ?: MediaType.ANIME
                        )
                    )
                )
            }

            val scoreFormat = user?.mediaListOptions?.scoreFormat

            val scoreIcon = when (scoreFormat) {
                ScoreFormat.POINT_5 -> AppIcons.IcStar
                ScoreFormat.POINT_3 -> followingModel.point3Icon
                else -> null
            }

            val totalScore = when (scoreFormat) {
                ScoreFormat.POINT_100 -> "100"
                ScoreFormat.POINT_10_DECIMAL -> "10.0"
                ScoreFormat.POINT_10 -> "10"
                else -> null
            }

            val score =
                if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) followingModel.score else followingModel.score?.toInt()

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    totalScore?.let {
                        MediumText(text = "${score ?: "?"}/$it")
                    }

                    if (scoreFormat == ScoreFormat.POINT_5) {
                        MediumText(text = "${score ?: "?"}")
                    }

                    scoreIcon?.let {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = scoreIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (totalScore == null && scoreIcon == null) {
                        MediumText(text = "?")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaSocialFollowingItemPreview() {
    MediaSocialFollowingItem(
        followingModel = MediaSocialFollowingModel(
            id = -1,
            user = UserModel(
                mediaListOptions = MediaListOptionModel(
                    scoreFormat = ScoreFormat.POINT_10_DECIMAL
                )
            ),
            type = MediaType.ANIME,
            status = MediaListStatus.COMPLETED,
            score = 10.0
        )
    ){}
}