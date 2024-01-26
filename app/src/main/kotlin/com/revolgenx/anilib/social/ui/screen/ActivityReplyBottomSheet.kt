package com.revolgenx.anilib.social.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.IntState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.SheetBehaviors
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetLayout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityComposerScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.SemiBoldText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAutorenew
import com.revolgenx.anilib.common.ui.icons.appicon.IcCreate
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityReplyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ActivityReplyBottomSheet(
    activityId: IntState,
    bottomSheetState: BottomSheetState
) {
    val navigator = localNavigator()
    val scope = rememberCoroutineScope()

    if (bottomSheetState.visible) {
        BackHandler(enabled = bottomSheetState.visible) {
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

                },
                onUserClick = {
                    navigator.userScreen(userId = it)
                }
            )
        }
    }
}
