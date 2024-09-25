package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenuItem
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.button.RefreshButton
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotificationOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcPencil
import com.revolgenx.anilib.common.ui.icons.appicon.IcReply
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.social.ui.model.ListActivityModel
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionServiceViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ActivityDetailScreen(private val activityId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        ActivityDetailScreenContent(activityId = activityId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityDetailScreenContent(activityId: Int) {
    val viewModel: ActivityInfoViewModel = koinViewModel(parameters = { parametersOf(activityId) })
    val activityReplyServiceViewModel: ActivityReplyServiceViewModel = koinViewModel()
    val activityUnionServiceViewModel: ActivityUnionServiceViewModel = koinViewModel()


    val activityComposerBottomSheetState = rememberBottomSheetState()
    val messageComposerBottomSheetState = rememberBottomSheetState()
    val replyComposerBottomSheetState = rememberBottomSheetState()

    val messageComposerViewModel: MessageComposerViewModel = koinViewModel()
    val activityComposerViewModel: ActivityComposerViewModel = koinViewModel()
    val replyComposerViewModel: ReplyComposerViewModel = koinViewModel()

    viewModel.getResource()

    val navigator = localNavigator()
    val user = localUser()
    val context = localContext()
    val scope = rememberCoroutineScope()

    val showRefreshButton = remember {
        mutableStateOf(false)
    }

    ScreenScaffold(
        title = stringResource(id = R.string.activity),
        actions = {
            val activityModel = viewModel.getData() ?: return@ScreenScaffold
            ActionMenu(icon = AppIcons.IcReply) {
                scope.launch {
                    replyComposerViewModel.forReply(viewModel.activityId, null, null)
                    replyComposerBottomSheetState.peek()
                }
            }
            ActionMenu(icon = if (activityModel.isSubscribed.value) AppIcons.IcNotification else AppIcons.IcNotificationOutline) {
                activityUnionServiceViewModel.toggleSubscription(activityModel)
            }

            OverflowMenu { expanded ->
                user.userId?.takeIf { it == viewModel.getData()?.userId }?.let {
                    if (activityModel !is ListActivityModel) {
                        OverflowMenuItem(
                            textRes = R.string.edit,
                            icon = AppIcons.IcPencil,
                            onClick = {
                                expanded.value = false
                                when (activityModel) {
                                    is MessageActivityModel -> {
                                        scope.launch {
                                            messageComposerViewModel.forMessage(
                                                activityId = activityModel.id,
                                                recipientId = activityModel.recipientId!!,
                                                message = activityModel.message,
                                                private = activityModel.isPrivate
                                            )
                                            messageComposerBottomSheetState.peek()
                                        }
                                    }

                                    is TextActivityModel -> {
                                        scope.launch {
                                            activityComposerViewModel.forText(
                                                activityId = activityModel.id,
                                                text = activityModel.text
                                            )
                                            activityComposerBottomSheetState.peek()
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        )
                    }

                    OverflowMenuItem(
                        textRes = R.string.delete,
                        icon = AppIcons.IcDelete,
                        onClick = {
                            expanded.value = false
                            viewModel.getData()?.let {
                                activityUnionServiceViewModel.delete(it)
                            }
                        }
                    )
                }

                activityModel.siteUrl?.let { site ->
                    OpenInBrowserOverflowMenu(link = site) {
                        expanded.value = false
                    }
                    ShareOverflowMenu(text = site) {
                        expanded.value = false
                    }
                }
            }
        },
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { snackbarHostState ->
        ResourceScreen(viewModel = viewModel) { activityModel ->
            LazyColumn(
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                item {
                    when (activityModel) {
                        is ListActivityModel -> {
                            ListActivityItem(
                                model = activityModel,
                                loggedInUserId = user.userId,
                                onMediaClick = {
                                    activityModel.media?.let {
                                        navigator.mediaScreen(it.id, it.type)
                                    }
                                },
                                onUserClick = {
                                    navigator.userScreen(it)
                                },
                                onLikeClick = {
                                    activityUnionServiceViewModel.toggleLike(activityModel)
                                },
                                onSubscribeClick = {},
                                onShowReplies = {},
                                onDelete = {},
                                onActivityClick = {},
                                showActionMenu = false
                            )
                        }

                        is MessageActivityModel -> {
                            MessageActivityItem(
                                model = activityModel,
                                loggedInUserId = user.userId,
                                onUserClick = {
                                    navigator.userScreen(it)
                                },
                                onLikeClick = {
                                    activityUnionServiceViewModel.toggleLike(activityModel)
                                },
                                onActivityClick = {},
                                onDelete = {},
                                onSubscribeClick = {},
                                onShowReplies = {},
                                onEdit = {},
                                showActionMenu = false
                            )
                        }

                        is TextActivityModel -> {
                            TextActivityItem(
                                model = activityModel,
                                loggedInUserId = user.userId,
                                onUserClick = {
                                    navigator.userScreen(it)
                                },
                                onLikeClick = {
                                    activityUnionServiceViewModel.toggleLike(activityModel)
                                },
                                onActivityClick = {},
                                onSubscribeClick = {},
                                onShowReplies = {},
                                onEdit = {},
                                onDelete = {},
                                showActionMenu = false
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = stringResource(id = R.string.replies_s).format(activityModel.replyCount))
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }

                items(items = activityModel.replies.orEmpty()) { activityReplyModel ->
                    if (activityReplyModel.isDeleted.value) {
                        ReplyDeletedItem()
                        return@items
                    }

                    ActivityReplyItem(
                        model = activityReplyModel,
                        loggedInUserId = user.userId,
                        onUserClick = {
                            navigator.userScreen(it)
                        },
                        onLikeClick = {
                            activityReplyServiceViewModel.toggleLike(activityReplyModel)
                        },
                        onReplyEdit = { replyModel ->
                            scope.launch {
                                replyModel.activityId?.let {
                                    replyComposerViewModel.forReply(
                                        replyModel.activityId,
                                        replyModel.id,
                                        replyModel.text
                                    )
                                    replyComposerBottomSheetState.peek()
                                }
                            }
                        },
                        onDelete = {
                            activityReplyServiceViewModel.delete(activityReplyModel)
                        }
                    )
                }
            }
        }


        RefreshButton(visible = showRefreshButton.value) {
            showRefreshButton.value = false
            viewModel.refresh()
        }

        LaunchedEffect(activityUnionServiceViewModel.showToggleError) {
            if (activityUnionServiceViewModel.showToggleError) {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.operation_failed),
                    withDismissAction = true
                )
                activityUnionServiceViewModel.showToggleError = false
            }
        }

        LaunchedEffect(activityUnionServiceViewModel.showDeleteError) {
            if (activityUnionServiceViewModel.showDeleteError) {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.failed_to_delete),
                    withDismissAction = true
                )
                activityUnionServiceViewModel.showDeleteError = false
            }
        }

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
        ActivityComposerBottomSheet(
            bottomSheetState = activityComposerBottomSheetState,
            viewModel = activityComposerViewModel,
            onSuccess = {
                showRefreshButton.value = true
            }
        )

        ActivityComposerBottomSheet(
            bottomSheetState = messageComposerBottomSheetState,
            viewModel = messageComposerViewModel,
            onSuccess = {
                showRefreshButton.value = true
            }
        )


        ActivityComposerBottomSheet(
            bottomSheetState = replyComposerBottomSheetState,
            viewModel = replyComposerViewModel,
            onSuccess = {
                showRefreshButton.value = true
            }
        )
    }
}

