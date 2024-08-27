package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.screen.ActivityComposerBottomSheet
import com.revolgenx.anilib.social.ui.screen.ActivityReplyBottomSheet
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreenContent
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserActivityUnionScreen(
    viewModel: ActivityUnionViewModel,
) {

    val activityReplyBottomSheetState = rememberBottomSheetState()
    val activityComposerBottomSheetState = rememberBottomSheetState()
    val messageComposerBottomSheetState = rememberBottomSheetState()
    val replyComposerBottomSheetState = rememberBottomSheetState()

    val scope = rememberCoroutineScope()
    val activityComposerViewModel: ActivityComposerViewModel = koinViewModel()
    val replyComposerViewModel: ReplyComposerViewModel = koinViewModel()
    val messageComposerViewModel: MessageComposerViewModel = koinViewModel()

    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

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
            }
        )
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
        }
    )

    ActivityComposerBottomSheet(
        bottomSheetState = activityComposerBottomSheetState,
        viewModel = activityComposerViewModel
    )

    ActivityComposerBottomSheet(
        bottomSheetState = messageComposerBottomSheetState,
        viewModel = messageComposerViewModel
    )


    ActivityComposerBottomSheet(
        bottomSheetState = replyComposerBottomSheetState,
        viewModel = replyComposerViewModel
    )
}