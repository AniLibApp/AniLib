package com.revolgenx.anilib.social.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.SheetBehaviors
import com.dokar.sheets.m3.BottomSheetLayout
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.button.RefreshButton
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActivityReplyBottomSheet(
    activityId: Int,
    bottomSheetState: BottomSheetState,
    showRefreshButton: MutableState<Boolean>,
    onReplyCompose: OnClick,
    onReplyEdit: (model: ActivityReplyModel) -> Unit
) {
    val navigator = localNavigator()
    val scope = rememberCoroutineScope()

    if (bottomSheetState.visible) {
        BackHandler {
            scope.launch {
                bottomSheetState.collapse()
            }
        }

        BottomSheetLayout(
            state = bottomSheetState,
            peekHeight = PeekHeight.fraction(0.7f),
            behaviors = SheetBehaviors(extendsIntoNavigationBar = true)
        ) {
            Box {
                val viewModel: ActivityReplyViewModel = koinViewModel(
                    key = "${ActivityReplyViewModel::class.java.canonicalName}:${activityId}",
                    parameters = { parametersOf(activityId) }
                )
                ActivityReplyContent(
                    viewModel = viewModel,
                    onReplyCompose = onReplyCompose,
                    onUserClick = {
                        navigator.userScreen(userId = it)
                    },
                    onReplyEdit = onReplyEdit,
                    refreshButton = {
                        RefreshButton(visible = showRefreshButton.value) {
                            showRefreshButton.value = false
                            viewModel.refresh()
                        }
                    }
                )

            }

        }
    }
}
