package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.SemiBoldText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAutorenew
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPencil
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivityReplyContent(
    viewModel: ActivityReplyViewModel,
    onReplyCompose: OnClick,
    onReplyEdit: (model: ActivityReplyModel) -> Unit,
    onUserClick: OnClickWithId,
    refreshButton: @Composable BoxScope.()-> Unit
) {
    val activityReplyServiceViewModel: ActivityReplyServiceViewModel = koinViewModel()
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val snackbarHostState = localSnackbarHostState()
    val context = localContext()
    val user = localUser()

    LaunchedEffect(activityReplyServiceViewModel.showToggleError) {
        if (activityReplyServiceViewModel.showToggleError) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.operation_failed),
                withDismissAction = true
            )
            activityReplyServiceViewModel.showToggleError = false
        }
    }

    LaunchedEffect(activityReplyServiceViewModel.showDeleteError) {
        if (activityReplyServiceViewModel.showDeleteError) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.failed_to_delete),
                withDismissAction = true
            )
            activityReplyServiceViewModel.showDeleteError = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SemiBoldText(text = stringResource(id = R.string.replies))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.refresh()
                }) {
                    Icon(imageVector = AppIcons.IcAutorenew, contentDescription = null)
                }
                FilledTonalButton(
                    onClick = onReplyCompose
                ) {
                    Text(text = stringResource(id = R.string.reply))
                    Spacer(modifier = Modifier.size(3.dp))
                    Icon(imageVector = AppIcons.IcCreate, contentDescription = null)
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyPagingList(
                pagingItems = pagingItems,
                onPullRefresh = false,
            ) { replyModel ->
                replyModel ?: return@LazyPagingList

                if (replyModel.isDeleted.value) {
                    ReplyDeletedItem()
                    return@LazyPagingList
                }

                ActivityReplyItem(
                    model = replyModel,
                    loggedInUserId = user.userId,
                    onUserClick = onUserClick,
                    onLikeClick = {
                        activityReplyServiceViewModel.toggleLike(model = replyModel)
                    },
                    onReplyEdit = onReplyEdit,
                    onDelete = {
                        activityReplyServiceViewModel.delete(model = replyModel)
                    }
                )
            }

            refreshButton()

        }
    }
}


@Composable
fun ActivityReplyItem(
    model: ActivityReplyModel,
    loggedInUserId: Int?,
    onUserClick: OnClickWithId,
    onLikeClick: OnClick,
    onReplyEdit: (model: ActivityReplyModel) -> Unit,
    onDelete: OnClick
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    .size(36.dp)
                    .width(36.dp)
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl = model.user?.avatar?.large,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = com.revolgenx.anilib.R.drawable.bleach
            )



            Column {
                MediumText(
                    text = model.user?.name.naText(),
                    fontSize = 12.sp
                )

                LightText(
                    text = model.createdAtPrettyTime,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            MediumText(
                modifier = Modifier.padding(start = 2.dp),
                text = model.likeCount.intValue.prettyNumberFormat(),
            )

            IconToggleButton(
                modifier = Modifier.size(24.dp),
                checked = model.isLiked.value,
                onCheckedChange = { onLikeClick() }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = if (model.isLiked.value) AppIcons.IcHeart else AppIcons.IcHeartOutline,
                    contentDescription = null
                )
            }

            loggedInUserId?.takeIf { it == model.userId }?.let {
                OverflowMenu {
                    OverflowMenuItem(
                        textRes = anilib.i18n.R.string.edit,
                        icon = AppIcons.IcPencil,
                        onClick = {
                            it.value = false
                            onReplyEdit(model)
                        }
                    )

                    OverflowMenuItem(
                        textRes = R.string.delete,
                        icon = AppIcons.IcDelete,
                        onClick = {
                            it.value = false
                            onDelete()
                        }
                    )
                }
            }
        }

        MarkdownText(
            modifier = Modifier.fillMaxWidth(),
            spanned = model.textSpanned,
        )

    }
}

@Composable
fun ReplyDeletedItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            text = stringResource(id = R.string.reply_has_been_deleted)
        )
    }
}
