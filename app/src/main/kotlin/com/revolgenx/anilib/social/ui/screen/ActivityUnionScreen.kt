package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.common.ext.topWindowInsets
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.button.BadgeIconButton
import com.revolgenx.anilib.common.ui.component.button.RefreshButton
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcForum
import com.revolgenx.anilib.common.ui.icons.appicon.IcForumOutline
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionFilterViewModel
import com.revolgenx.anilib.social.ui.viewmodel.MainActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ReplyComposerViewModel
import com.revolgenx.anilib.type.ActivityType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object ActivityUnionScreen : BaseTabScreen() {

    override val tabIcon: ImageVector = AppIcons.IcForumOutline
    override val selectedIcon: ImageVector = AppIcons.IcForum

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(I18nR.string.activity)

            return remember {
                TabOptions(
                    index = 0u, title = title
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: MainActivityUnionViewModel = koinViewModel()
        val filterViewModel: ActivityUnionFilterViewModel = koinViewModel()

        val filterBottomSheetState = rememberBottomSheetState()
        val scope = rememberCoroutineScope()

        val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
        val bottomNestedScrollConnection =
            remember { BottomNestedScrollConnection(state = scrollState) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        val activityReplyBottomSheetState = rememberBottomSheetState()

        val activityComposerBottomSheetState = rememberBottomSheetState()
        val replyComposerBottomSheetState = rememberBottomSheetState()

        val activityComposerViewModel: ActivityComposerViewModel = koinViewModel()
        val replyComposerViewModel: ReplyComposerViewModel = koinViewModel()

        val showActivityRefreshButton = remember {
            mutableStateOf(false)
        }

        val showReplyListRefreshButton = remember {
            mutableStateOf(false)
        }

        ScreenScaffold(
            topBar = {
                AppBarLayout(
                    scrollBehavior = scrollBehavior,
                    windowInsets = topWindowInsets()
                ) {
                    AppBar(
                        title = {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = stringResource(id = I18nR.string.activity)
                            )
                        },
                        actions = {
                            BadgeIconButton(icon = AppIcons.IcFilter, showBadge = viewModel.isFiltered) {
                                scope.launch {
                                    filterViewModel.field = viewModel.field.copy()
                                    filterBottomSheetState.expand()
                                }
                            }
                        }
                    )
                }
            },
            navigationIcon = {},
            floatingActionButton = {
                DisappearingFAB(
                    scrollState = scrollState,
                    icon = AppIcons.IcCreate
                ) {
                    scope.launch {
                        activityComposerViewModel.forText(null, null)
                        activityComposerBottomSheetState.peek()
                    }
                }
            },
            scrollBehavior = scrollBehavior,
            bottomNestedScrollConnection = bottomNestedScrollConnection,
        ) {
            ActivityUnionScreenContent(
                viewModel = viewModel,
                scrollTarget = ScrollTarget.ACTIVITY,
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
                onEditMessageActivity = {
                    //Can be ignored. There won't be any message activity in this screen
                })


            RefreshButton(visible = showActivityRefreshButton.value) {
                showActivityRefreshButton.value = false
                viewModel.refresh()
            }
            ActivityUnionFilterBottomSheet(
                bottomSheetState = filterBottomSheetState,
                viewModel = filterViewModel
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
            bottomSheetState = replyComposerBottomSheetState,
            viewModel = replyComposerViewModel,
            onSuccess = {
                showReplyListRefreshButton.value = true
            }
        )

    }
}


@Composable
fun ActivityUnionFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: ActivityUnionFilterViewModel
) {
    val scope = rememberCoroutineScope()

    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        ActivityUnionFilterBottomSheetContent(
            field = viewModel.field,
            onPositiveClicked = {
                viewModel.updateFilter()
            }) {
            scope.launch {
                bottomSheetState.collapse()
            }
        }
    }
}

@Composable
private fun ActivityUnionFilterBottomSheetContent(
    field: ActivityUnionField,
    onNegativeClicked: (() -> Unit)? = null,
    onPositiveClicked: (() -> Unit)? = null,
    dismiss: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            onConfirm = {
                onPositiveClicked?.invoke()
                dismiss?.invoke()
            },
            onDismiss = {
                onNegativeClicked?.invoke()
                dismiss?.invoke()
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val selectedItemPosition =
                when (field.type) {
                    ActivityType.TEXT -> 1
                    ActivityType.MEDIA_LIST -> 2
                    else -> 0
                }

            SelectMenu(
                label = stringResource(id = I18nR.string.activity_type),
                entries = stringArrayResource(id = R.array.activity_type_menu),
                selectedItemPosition = selectedItemPosition,
            ) { selectedItem ->
                field.type = when (selectedItem) {
                    1 -> ActivityType.TEXT
                    2 -> ActivityType.MEDIA_LIST
                    else -> null
                }
            }

            TextSwitch(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = anilib.i18n.R.string.following),
                checked = field.isFollowing!!,
                onCheckedChanged = {
                    field.isFollowing = it
                })
        }
    }
}