package com.revolgenx.anilib.notification.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.notification.ui.model.ActivityNotificationModel
import com.revolgenx.anilib.notification.ui.model.AiringNotificationModel
import com.revolgenx.anilib.notification.ui.model.FollowingNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDataChangeNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaDeletionNotificationModel
import com.revolgenx.anilib.notification.ui.model.MediaMergeNotificationModel
import com.revolgenx.anilib.notification.ui.model.RelatedMediaNotificationModel
import com.revolgenx.anilib.notification.ui.model.ThreadNotificationModel
import com.revolgenx.anilib.notification.ui.viewmodel.NotificationViewModel
import com.revolgenx.anilib.social.ui.screen.ActivityDetailScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object NotificationScreen : AndroidScreen() {
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val navigator = localNavigator()
    ScreenScaffold(
        title = stringResource(id = I18nR.string.notifications),
        scrollBehavior = scrollBehavior,
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        viewModel.field?: return@ScreenScaffold

        val snackbarHostState = localSnackbarHostState()
        val scope = rememberCoroutineScope()
        val pagingItems = viewModel.collectAsLazyPagingItems()

        LazyPagingList(
            pagingItems = pagingItems,
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
                            navigator.mediaScreen(
                                notificationModel.animeId,
                                notificationModel.media?.type
                            )
                        }
                    }

                    is ActivityNotificationModel -> {
                        image = notificationModel.user?.avatar?.image
                        text = "${notificationModel.user?.name} ${notificationModel.context}"

                        onImageClick = {
                            navigator.push(UserScreen(notificationModel.userId))
                        }
                        onClick = {
                            navigator.push(ActivityDetailScreen(notificationModel.activityId))
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
                            navigator.mediaScreen(
                                notificationModel.mediaId,
                                notificationModel.media?.type
                            )
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
                            navigator.mediaScreen(
                                notificationModel.mediaId,
                                notificationModel.media?.type
                            )
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
                            navigator.mediaScreen(
                                notificationModel.mediaId,
                                notificationModel.media?.type
                            )
                        }
                    }

                    is MediaDeletionNotificationModel -> {
                        text =
                            "${notificationModel.deletedMediaTitle} ${notificationModel.context}"
                        reason = notificationModel.reason
                    }
                }

                val notificationTitle = text.orEmpty()
                NotificationItem(
                    notificationTitle = notificationTitle,
                    image = image,
                    reason = reason,
                    isUnread = notificationModel.unreadNotificationCount >= index + 1,
                    createdAt = createdAt,
                    onImageClick = onImageClick,
                    onTitleClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = notificationTitle,
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    showReasonClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = reason!!,
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onClick = onClick
                )
            }
        )
    }
}


@Composable
private fun NotificationItem(
    notificationTitle: String,
    image: String?,
    reason: String?,
    createdAt: String,
    isUnread: Boolean,
    onImageClick: (() -> Unit)? = null,
    onTitleClick: OnClick,
    showReasonClick: OnClick,
    onClick: OnClick
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageAsync(
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
                MediumText(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onTitleClick()
                    },
                    text = notificationTitle,
                    maxLines = 2,
                )
                Column {
                    reason?.let {
                        val showReason = stringResource(id = I18nR.string.show_reason)
                        val reasonState = remember { mutableStateOf(showReason) }
                        LightText(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (reasonState.value == reason) {
                                    showReasonClick()
                                } else {
                                    reasonState.value = reason
                                }
                            },
                            text = reasonState.value
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
                VerticalDivider(
                    thickness = 8.dp,
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
        notificationTitle = "DarthMarine started following you.",
        reason = "reason",
        createdAt = "1 week ago",
        isUnread = true,
        image = "",
        onTitleClick = {},
        showReasonClick = {}
    ) {

    }
}