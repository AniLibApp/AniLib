package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.common.ext.activityScreen
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcMessage
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotificationOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPencil
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ListActivityModel
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivityUnionScreenContent(
    viewModel: ActivityUnionViewModel,
    scrollTarget: ScrollTarget,
    onShowReplies: OnClickWithId,
    onEditTextActivity: (model: TextActivityModel) -> Unit,
    onEditMessageActivity: (model: MessageActivityModel) -> Unit,
) {
    val activityUnionServiceViewModel: ActivityUnionServiceViewModel = koinViewModel()
    val scrollViewModel: ScrollViewModel = activityViewModel()
    val navigator = localNavigator()
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val snackbarHostState = localSnackbarHostState()
    val context = localContext()
    val user = localUser()

    LaunchedEffect(activityUnionServiceViewModel.showToggleError) {
        if (activityUnionServiceViewModel.showToggleError) {
            snackbarHostState.showSnackbar(
                context.getString(anilib.i18n.R.string.operation_failed),
                withDismissAction = true
            )
            activityUnionServiceViewModel.showToggleError = false
        }
    }

    LaunchedEffect(activityUnionServiceViewModel.showDeleteError) {
        if (activityUnionServiceViewModel.showDeleteError) {
            snackbarHostState.showSnackbar(
                context.getString(anilib.i18n.R.string.failed_to_delete),
                withDismissAction = true
            )
            activityUnionServiceViewModel.showDeleteError = false
        }
    }

    val scrollableState = rememberLazyListState()
    LaunchedEffect(scrollViewModel) {
        scrollViewModel.scrollEventFor(scrollTarget).collectLatest {
            scrollableState.animateScrollToItem(0)
        }
    }

    LazyPagingList(
        pagingItems = pagingItems,
        scrollState = scrollableState,
        onRefresh = {
            viewModel.refresh()
        },
    ) { activityModel ->
        activityModel ?: return@LazyPagingList
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            if (activityModel.isDeleted.value) {
                ActivityDeletedItem()
                return@Box
            }

            when (activityModel) {
                is ListActivityModel -> {
                    ListActivityItem(
                        model = activityModel,
                        loggedInUserId = user.userId,
                        onActivityClick = {
                            navigator.activityScreen(activityModel.id)
                        },
                        onMediaClick = {
                            activityModel.media?.let {
                                navigator.mediaScreen(it.id, it.type)
                            }
                        },
                        onUserClick = {
                            navigator.userScreen(it)
                        },
                        onSubscribeClick = {
                            activityUnionServiceViewModel.toggleSubscription(activityModel)
                        },
                        onLikeClick = {
                            activityUnionServiceViewModel.toggleLike(activityModel)
                        },
                        onShowReplies = onShowReplies,
                        onDelete = {
                            activityUnionServiceViewModel.delete(model = activityModel)
                        })
                }

                is MessageActivityModel -> {
                    MessageActivityItem(
                        model = activityModel,
                        loggedInUserId = user.userId,
                        onActivityClick = {
                            navigator.activityScreen(activityModel.id)
                        },
                        onUserClick = {
                            navigator.userScreen(it)
                        },
                        onSubscribeClick = {
                            activityUnionServiceViewModel.toggleSubscription(activityModel)
                        },
                        onLikeClick = {
                            activityUnionServiceViewModel.toggleLike(activityModel)
                        },
                        onShowReplies = onShowReplies,
                        onEdit = {
                            onEditMessageActivity(activityModel)
                        },
                        onDelete = {
                            activityUnionServiceViewModel.delete(model = activityModel)
                        }
                    )
                }

                is TextActivityModel -> {
                    TextActivityItem(
                        model = activityModel,
                        loggedInUserId = user.userId,
                        onActivityClick = {
                            navigator.activityScreen(activityModel.id)
                        },
                        onUserClick = {
                            navigator.userScreen(it)
                        },
                        onSubscribeClick = {
                            activityUnionServiceViewModel.toggleSubscription(activityModel)
                        },
                        onLikeClick = {
                            activityUnionServiceViewModel.toggleLike(activityModel)
                        },
                        onShowReplies = onShowReplies,
                        onEdit = {
                            onEditTextActivity(activityModel)
                        },
                        onDelete = {
                            activityUnionServiceViewModel.delete(model = activityModel)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ListActivityItem(
    model: ListActivityModel,
    loggedInUserId: Int?,
    showActionMenu: Boolean = true,
    onActivityClick: OnClick,
    onMediaClick: OnClick,
    onUserClick: OnClickWithId,
    onSubscribeClick: OnClick,
    onShowReplies: OnClickWithId,
    onLikeClick: OnClick,
    onDelete: OnClick
) {
    val media = model.media
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            MediaCoverImageType { imageType ->
                ImageAsync(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .clickable(onClick = onMediaClick),
                    imageUrl = media?.coverImage?.image(imageType),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .clickable(onClick = onActivityClick),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActivityItemTop(
                        model = model,
                        loggedInUserId = loggedInUserId,
                        onUserClick = onUserClick,
                        onSubscribeClick = onSubscribeClick,
                        onDelete = onDelete,
                        showActionMenu = showActionMenu
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = model.progressStatus,
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }
                ActivityItemBottom(
                    model = model,
                    onActivityClick = onActivityClick,
                    onReplyClick = onShowReplies,
                    onLikeClick = onLikeClick
                )
            }
        }
    }
}

@Composable
fun MessageActivityItem(
    model: MessageActivityModel,
    loggedInUserId: Int?,
    showActionMenu: Boolean = true,
    onActivityClick: OnClick,
    onUserClick: OnClickWithId,
    onSubscribeClick: OnClick,
    onShowReplies: OnClickWithId,
    onLikeClick: OnClick,
    onEdit: OnClick,
    onDelete: OnClick
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ActivityItemTop(
                model = model,
                loggedInUserId = loggedInUserId,
                onUserClick = onUserClick,
                onSubscribeClick = onSubscribeClick,
                onEdit = onEdit,
                onDelete = onDelete,
                showActionMenu = showActionMenu
            )
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                spanned = model.messageSpanned
            )
            ActivityItemBottom(
                model,
                onActivityClick = onActivityClick,
                onReplyClick = onShowReplies,
                onLikeClick = onLikeClick
            )
        }
    }
}


@Composable
fun TextActivityItem(
    model: TextActivityModel,
    loggedInUserId: Int?,
    showActionMenu: Boolean = true,
    onActivityClick: OnClick,
    onUserClick: OnClickWithId,
    onSubscribeClick: OnClick,
    onShowReplies: OnClickWithId,
    onLikeClick: OnClick,
    onEdit: OnClick,
    onDelete: OnClick
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ActivityItemTop(
                model = model,
                loggedInUserId = loggedInUserId,
                onUserClick = onUserClick,
                onSubscribeClick = onSubscribeClick,
                onEdit = onEdit,
                onDelete = onDelete,
                showActionMenu = showActionMenu
            )
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                spanned = model.textSpanned
            )
            ActivityItemBottom(
                model,
                onActivityClick = onActivityClick,
                onReplyClick = onShowReplies,
                onLikeClick = onLikeClick
            )
        }
    }
}


@Composable
private fun ActivityItemTop(
    model: ActivityModel,
    loggedInUserId: Int?,
    showActionMenu: Boolean,
    onUserClick: OnClickWithId,
    onSubscribeClick: OnClick,
    onEdit: OnClick? = null,
    onDelete: OnClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    model.userId?.let {
                        onUserClick(it)
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ImageAsync(
                modifier = Modifier
                    .size(38.dp)
                    .width(38.dp)
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl = model.user?.avatar?.large,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )


            MediumText(
                text = model.user?.name.naText(),
            )
        }

        if (showActionMenu) {
            Row {
                IconButton(onClick = {
                    onSubscribeClick()
                }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = if (model.isSubscribed.value) AppIcons.IcNotification else AppIcons.IcNotificationOutline,
                        contentDescription = null
                    )
                }

                OverflowMenu { expanded ->
                    loggedInUserId?.takeIf { it == model.userId }?.let {
                        onEdit?.let {
                            OverflowMenuItem(
                                textRes = anilib.i18n.R.string.edit,
                                icon = AppIcons.IcPencil,
                                onClick = {
                                    expanded.value = false
                                    it()
                                }
                            )
                        }

                        OverflowMenuItem(
                            textRes = anilib.i18n.R.string.delete,
                            icon = AppIcons.IcDelete,
                            onClick = {
                                expanded.value = false
                                onDelete()
                            }
                        )
                    }

                    model.siteUrl?.let { site ->
                        OpenInBrowserOverflowMenu(link = site) {
                            expanded.value = false
                        }
                        ShareOverflowMenu(text = site) {
                            expanded.value = false
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ActivityItemBottom(
    model: ActivityModel,
    onActivityClick: OnClick,
    onReplyClick: OnClickWithId,
    onLikeClick: OnClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onActivityClick),
        verticalAlignment = Alignment.Bottom
    ) {
        LightText(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            text = model.createdAtPrettyTime
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                onClick = {
                    onReplyClick(model.id)
                },
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = AppIcons.IcMessage,
                    contentDescription = null
                )
                MediumText(
                    modifier = Modifier.padding(start = 4.dp),
                    text = model.replyCount.prettyNumberFormat(),
                    fontSize = 14.sp
                )
            }


            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                onClick = {
                    onLikeClick()
                },
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (model.isLiked.value) AppIcons.IcHeart else AppIcons.IcHeartOutline,
                    contentDescription = null,
                    tint = if (model.isLiked.value) MaterialTheme.colorScheme.primary else LocalContentColor.current
                )
                MediumText(
                    modifier = Modifier.padding(start = 4.dp),
                    text = model.likeCount.intValue.prettyNumberFormat(),
                )
            }

        }
    }
}


@Composable
private fun ActivityDeletedItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            text = stringResource(id = anilib.i18n.R.string.activity_has_been_deleted)
        )
    }
}