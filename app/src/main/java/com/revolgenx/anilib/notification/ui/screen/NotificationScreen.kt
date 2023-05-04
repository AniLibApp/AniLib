package com.revolgenx.anilib.notification.ui.screen

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localNavigator
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.screen.MediaScreen
import com.revolgenx.anilib.notification.ui.model.ActivityNotificationModel
import com.revolgenx.anilib.notification.ui.model.AiringNotificationModel
import com.revolgenx.anilib.notification.ui.model.FollowingNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDataChangeNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDeletionNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaMergeNotificationModel
import com.revolgenx.anilib.notification.ui.model.RelatedMediaNotificationModel
import com.revolgenx.anilib.notification.ui.model.ThreadNotificationModel
import com.revolgenx.anilib.notification.ui.viewmodel.NotificationViewModel
import com.revolgenx.anilib.social.ui.screen.ActivityScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import org.koin.androidx.compose.koinViewModel

class NotificationScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        NotificationScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationScreenContent(
    viewModel: NotificationViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navigator = localNavigator()
    ScreenScaffold(
        title = stringResource(id = R.string.notifications),
        scrollBehavior = scrollBehavior
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyPagingList(
                items = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
                itemContentIndex = { index, notificationModel ->
                    notificationModel ?: return@LazyPagingList
                    var text by remember { mutableStateOf<String?>(null) }
                    var image by remember { mutableStateOf<String?>(null) }
                    var reason: String? = null
                    val createdAt: String = notificationModel.createdAtPrettyTime ?: ""
                    var onImageClick: (() -> Unit)? = null
                    var onClick = {}

                    when (notificationModel) {
                        is AiringNotificationModel -> {
                            MediaCoverImageType {
                                image = notificationModel.media?.coverImage?.image(it)
                            }
                            MediaTitleType {
                                val (c1, c2, c3) = notificationModel.contexts
                                    ?: return@MediaTitleType
                                text =
                                    "$c1 ${notificationModel.episode} $c2 ${
                                        notificationModel.media?.title?.title(
                                            it
                                        )
                                    } $c3"
                            }
                            onClick = {
                                navigator.push(MediaScreen(notificationModel.animeId, notificationModel.media?.type))
                            }
                        }

                        is ActivityNotificationModel -> {
                            image = notificationModel.user?.avatar?.image
                            text = "${notificationModel.user?.name} ${notificationModel.context}"

                            onImageClick = {
                                navigator.push(UserScreen(notificationModel.userId))
                            }
                            onClick = {
                                navigator.push(ActivityScreen())
                            }
                        }

                        is FollowingNotificationModel -> {
                            image = notificationModel.user?.avatar?.image
                            text = "${notificationModel.user?.name} ${notificationModel.context}"

                            onClick = {
                                navigator.push(UserScreen(notificationModel.userId))
                            }
                        }

                        is ThreadNotificationModel -> {
                            image = notificationModel.user?.avatar?.image
                            text =
                                "${notificationModel.user?.name} ${notificationModel.context} ${notificationModel.thread?.title}"
                            onImageClick = {
                                navigator.push(UserScreen(notificationModel.userId))
                            }
                            onClick = {
                                //todo: open thread link
                            }
                        }

                        is RelatedMediaNotificationModel -> {
                            MediaCoverImageType {
                                image = notificationModel.media?.coverImage?.image(it)
                            }
                            MediaTitleType {
                                text =
                                    "${notificationModel.media?.title?.title(it)} ${notificationModel.context}"
                            }
                            onClick = {
                                navigator.push(MediaScreen(notificationModel.mediaId, notificationModel.media?.type))
                            }
                        }

                        is MediaDataChangeNotificationModel -> {
                            MediaCoverImageType {
                                image = notificationModel.media?.coverImage?.image(it)
                            }
                            MediaTitleType {
                                text =
                                    "${notificationModel.media?.title?.title(it)} ${notificationModel.context}"
                            }
                            reason = notificationModel.reason

                            onClick = {
                                navigator.push(MediaScreen(notificationModel.mediaId, notificationModel.media?.type))
                            }
                        }

                        is MediaMergeNotificationModel -> {
                            MediaCoverImageType {
                                image = notificationModel.media?.coverImage?.image(it)
                            }
                            MediaTitleType {
                                text =
                                    "${notificationModel.media?.title?.title(it)} ${notificationModel.context}"
                            }
                            reason = notificationModel.reason

                            onClick = {
                                navigator.push(MediaScreen(notificationModel.mediaId, notificationModel.media?.type))
                            }
                        }

                        is MediaDeletionNotificationModel -> {
                            text =
                                "${notificationModel.deletedMediaTitle} ${notificationModel.context}"
                            reason = notificationModel.reason
                        }
                    }

                    NotificationItem(
                        context = text ?: "",
                        image = image,
                        reason = reason,
                        isUnread = notificationModel.unreadNotificationCount >= index + 1,
                        createdAt = createdAt,
                        onImageClick = onImageClick,
                        onClick = onClick
                    )
                }
            )
        }
    }
}


@Composable
private fun NotificationItem(
    context: String,
    image: String?,
    reason: String?,
    createdAt: String,
    isUnread: Boolean,
    onImageClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(8.dp)
    ) {
        Row {
            FrescoImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp)
                    .clickable {
                        onImageClick?.invoke() ?: onClick()
                    },
                imageUrl = image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        onClick()
                    }
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = context,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Column {
                    reason?.let {
                        val showReason = stringResource(id = R.string.show_reason)
                        val reasonState = remember { mutableStateOf(showReason) }
                        Text(
                            modifier = Modifier.clickable {
                                reasonState.value = reason
                            },
                            text = reasonState.value,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = createdAt,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isUnread) {
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@Preview
@Composable
fun NotificationItemPreview() {
    NotificationItem(
        context = "DarthMarine started following you.",
        reason = "reason",
        createdAt = "1 week ago",
        isUnread = true,
        image = ""
    ) {

    }
}