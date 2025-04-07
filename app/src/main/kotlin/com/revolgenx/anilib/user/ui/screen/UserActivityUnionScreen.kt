package com.revolgenx.anilib.user.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheetLayout
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.button.RefreshButton
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.social.ui.screen.ActivityComposerBottomSheet
import com.revolgenx.anilib.social.ui.screen.ActivityReplyBottomSheet
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreenContent
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.user.ui.viewmodel.UserActivityFilterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserActivityUnionScreen(viewModel: ActivityUnionViewModel) {
    viewModel.field.isFollowing = null
    val filterBottomSheetState = rememberBottomSheetState()
    val activityReplyBottomSheetState = rememberBottomSheetState()
    val activityComposerBottomSheetState = rememberBottomSheetState()
    val messageComposerBottomSheetState = rememberBottomSheetState()
    val replyComposerBottomSheetState = rememberBottomSheetState()

    val scope = rememberCoroutineScope()
    val filterViewModel: UserActivityFilterViewModel = koinViewModel()
    val activityComposerViewModel: ActivityComposerViewModel = koinViewModel()
    val replyComposerViewModel: ReplyComposerViewModel = koinViewModel()
    val messageComposerViewModel: MessageComposerViewModel = koinViewModel()

    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }


    val showActivityRefreshButton = remember {
        mutableStateOf(false)
    }

    val showReplyListRefreshButton = remember {
        mutableStateOf(false)
    }

    val user = localUser()

    val isLoggedInUser = user.userId == viewModel.field.userId

    ScreenScaffold(
        floatingActionButton = {
            DisappearingFAB(
                scrollState = scrollState,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ActionMenu(icon = AppIcons.IcFilter) {
                            scope.launch {
                                filterViewModel.activityType = viewModel.field.type
                                filterBottomSheetState.expand()
                            }
                        }

                        VerticalDivider(
                            modifier = Modifier.height(20.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        ActionMenu(icon = AppIcons.IcCreate) {
                            scope.launch {
                                if(isLoggedInUser){
                                    viewModel.field.userId?.let {
                                        activityComposerViewModel.forText(null, null)
                                        activityComposerBottomSheetState.peek()
                                    }
                                }else{
                                    viewModel.field.userId?.let {
                                        messageComposerViewModel.forMessage(it, null, null, false)
                                        messageComposerBottomSheetState.peek()
                                    }
                                }
                            }
                        }
                    }
                })
        },
        topBar = {},
        navigationIcon = {},
        bottomNestedScrollConnection = bottomScrollConnection,
    ) {
        ActivityUnionScreenContent(
            viewModel = viewModel,
            scrollTarget = ScrollTarget.USER,
            onShowReplies = {
                scope.launch {
                    viewModel.activityId = it
                    activityReplyBottomSheetState.peek()
                }
            },
            onEditTextActivity = { textModel ->
                scope.launch {
                    activityComposerViewModel.forText(
                        activityId = textModel.id,
                        text = textModel.text
                    )
                    activityComposerBottomSheetState.peek()
                }
            },
            onEditMessageActivity = { messageModel ->
                scope.launch {
                    messageComposerViewModel.forMessage(
                        activityId = messageModel.id,
                        recipientId = messageModel.recipientId!!,
                        message = messageModel.message,
                        private = messageModel.isPrivate
                    )
                    messageComposerBottomSheetState.peek()
                }
            },

        )


        RefreshButton(visible = showActivityRefreshButton.value) {
            showActivityRefreshButton.value = false
            viewModel.refresh()
        }
    }



    UserActivityFilterBottomSheet(
        bottomSheetState = filterBottomSheetState,
        viewModel = filterViewModel,
        onDismiss = {
            scope.launch {
                filterBottomSheetState.collapse()
            }
        }
    ) {
        viewModel.field.type = it
        viewModel.refresh()
    }


    ActivityReplyBottomSheet(
        activityId = viewModel.activityId,
        bottomSheetState = activityReplyBottomSheetState,
        onReplyCompose = {
            scope.launch {
                replyComposerViewModel.forReply(viewModel.activityId, null, null)
                replyComposerBottomSheetState.peek()
            }
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
        showRefreshButton = showReplyListRefreshButton
    )


    ActivityComposerBottomSheet(
        bottomSheetState = activityComposerBottomSheetState,
        viewModel = activityComposerViewModel,
        onSuccess = {
            showActivityRefreshButton.value = true
        }
    )

    ActivityComposerBottomSheet(
        bottomSheetState = messageComposerBottomSheetState,
        viewModel = messageComposerViewModel,
        onSuccess = {
            showActivityRefreshButton.value = true
        }
    )


    ActivityComposerBottomSheet(
        bottomSheetState = replyComposerBottomSheetState,
        viewModel = replyComposerViewModel,
        onSuccess = {
            showReplyListRefreshButton.value = true
        }
    )
}

@Composable
private fun UserActivityFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: UserActivityFilterViewModel,
    onDismiss: OnClick,
    onConfirm: (activityType: ActivityType?) -> Unit
) {
    val scope = rememberCoroutineScope()
    if (bottomSheetState.visible) {
        BackHandler {
            scope.launch {
                bottomSheetState.collapse()
            }
        }

        BottomSheetLayout(
            state = bottomSheetState,
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            BottomSheetConfirmation(
                onConfirm = {
                    onConfirm(viewModel.activityType)
                    onDismiss()
                },
                onDismiss = {
                    onDismiss()
                }
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val selectedItemPosition =
                    when (viewModel.activityType) {
                        ActivityType.TEXT -> 1
                        ActivityType.MESSAGE -> 2
                        ActivityType.MEDIA_LIST -> 3
                        else -> 0
                    }

                SelectMenu(
                    label = stringResource(id = R.string.activity_type),
                    entries = stringArrayResource(id = com.revolgenx.anilib.R.array.user_activity_type_menu),
                    selectedItemPosition = selectedItemPosition,
                ) { selectedItem ->
                    viewModel.activityType = when (selectedItem) {
                        1 -> ActivityType.TEXT
                        2 -> ActivityType.MESSAGE
                        3 -> ActivityType.MEDIA_LIST
                        else -> null
                    }
                }
            }

        }
    }
}
