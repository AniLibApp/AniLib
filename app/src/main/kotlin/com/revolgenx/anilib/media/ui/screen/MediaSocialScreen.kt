package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.common.ext.hideBottomSheet
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.radio.TextRadioButton
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialFollowingScreenViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialScreenType
import com.revolgenx.anilib.media.ui.viewmodel.MediaSocialScreenViewModel
import com.revolgenx.anilib.social.ui.screen.ActivityComposerBottomSheet
import com.revolgenx.anilib.social.ui.screen.ActivityReplyBottomSheet
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSocialScreen(
    activityUnionViewModel: ActivityUnionViewModel,
    mediaSocialFollowingViewModel: MediaSocialFollowingScreenViewModel,
) {
    val viewModel: MediaSocialScreenViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val activityReplyBottomSheetState = rememberBottomSheetState()

    val replyComposerBottomSheetState = rememberBottomSheetState()
    val replyComposerViewModel: ReplyComposerViewModel = koinViewModel()

    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    val mediaSocialFilterTypeBottomSheet = remember {
        mutableStateOf(false)
    }

    val showReplyListRefreshButton = remember {
        mutableStateOf(false)
    }


    ScreenScaffold(
        floatingActionButton = {
            DisappearingFAB(
                scrollState = scrollState,
                content = {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(text = stringResource(id = viewModel.screenType.toStringRes()))
                    }
                },
                onClick = {
                    mediaSocialFilterTypeBottomSheet.value = true
                }
            )
        },
        topBar = {},
        navigationIcon = {},
        actions = {},
        bottomNestedScrollConnection = bottomScrollConnection,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (viewModel.screenType) {
                MediaSocialScreenType.ACTIVITY -> {
                    MediaSocialActivityScreen(
                        viewModel = activityUnionViewModel,
                        onShowReplies = { activityId ->
                            scope.launch {
                                activityUnionViewModel.activityId = activityId
                                activityReplyBottomSheetState.peek()
                            }
                        },
                        onEditTextActivity = {
                            //Can be ignored. Only List Activity is shown here
                        },
                        onEditMessageActivity = {
                            //Can be ignored. Only List Activity is shown here
                        }
                    )
                }

                MediaSocialScreenType.FOLLOWING -> {
                    MediaSocialFollowingScreen(viewModel = mediaSocialFollowingViewModel)
                }
            }
        }
    }


    ActivityReplyBottomSheet(
        activityId = activityUnionViewModel.activityId,
        bottomSheetState = activityReplyBottomSheetState,
        onReplyCompose = {
            scope.launch {
                replyComposerViewModel.forReply(activityUnionViewModel.activityId, null, null)
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
        bottomSheetState = replyComposerBottomSheetState,
        viewModel = replyComposerViewModel,
        onSuccess = {
            showReplyListRefreshButton.value = true
        }
    )

    MediaSocialTypeBottomSheet(
        openBottomSheet = mediaSocialFilterTypeBottomSheet,
        scope = scope,
        selectedType = viewModel.screenType
    ) {
        viewModel.screenType = it
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaSocialTypeBottomSheet(
    openBottomSheet: MutableState<Boolean>,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope,
    selectedType: MediaSocialScreenType,
    onTypeSelected: OnClickWithValue<MediaSocialScreenType>
) {
    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .verticalScroll(rememberScrollState())
            ) {
                MediaSocialScreenType.entries.forEach {
                    TextRadioButton(
                        text = stringResource(id = it.toStringRes()),
                        selected = it == selectedType
                    ) {
                        scope.hideBottomSheet(bottomSheetState, openBottomSheet)
                        onTypeSelected(it)
                    }
                }
            }
        }
    }
}