package com.revolgenx.anilib.social.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.rememberCoroutineScope
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.SheetBehaviors
import com.dokar.sheets.m3.BottomSheetLayout
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActivityReplyBottomSheet(
    activityId: IntState,
    bottomSheetState: BottomSheetState,
    activityComposerBottomSheetState: BottomSheetState
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
            val viewModel: ActivityReplyViewModel = koinViewModel(
                key = "${ActivityReplyViewModel::class.java.canonicalName}:${activityId.intValue}",
                parameters = { parametersOf(activityId.intValue) }
            )
            ActivityReplyContent(
                viewModel = viewModel,
                onReplyClick = {
                    scope.launch {
                        activityComposerBottomSheetState.peek()
                    }
                },
                onUserClick = {
                    navigator.userScreen(userId = it)
                }
            )
        }
    }
}
