package com.revolgenx.anilib.social.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheetLayout
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSend
import com.revolgenx.anilib.social.ui.component.MarkdownEditor
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.BaseActivityComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityType
import kotlinx.coroutines.launch
import anilib.i18n.R as I18nR

private typealias ActivityComposerScreenPage = PagerScreen<ActivityComposerScreenPages>

private enum class ActivityComposerScreenPages {
    COMPOSE,
    PREVIEW,
}

private val pages = listOf(
    ActivityComposerScreenPage(ActivityComposerScreenPages.COMPOSE, I18nR.string.compose),
    ActivityComposerScreenPage(ActivityComposerScreenPages.PREVIEW, I18nR.string.preview),
)

@Composable
fun ActivityComposerBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: BaseActivityComposerViewModel
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
            peekHeight = PeekHeight.fraction(0.8f),
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            ActivityComposerScreenContent(viewModel = viewModel)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityComposerScreenContent(
    viewModel: BaseActivityComposerViewModel
) {
    val pagerState = rememberPagerState { pages.size }

    PagerScreenScaffold(
        navigationIcon = {},
        actions = {
            ActionMenu(
                icon = AppIcons.IcSend
            ) {
                viewModel.save()
            }
        },
        userScrollEnabled = false,
        pages = pages,
        pagerState = pagerState,
        windowInsets = emptyWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (ActivityComposerScreenPages.entries[page]) {
                ActivityComposerScreenPages.COMPOSE -> ActivityComposerStatusScreen(
                    viewModel
                )

                ActivityComposerScreenPages.PREVIEW -> ActivityComposerPreviewScreen(
                    viewModel
                )
            }
        }
    }
}


@Composable
private fun ActivityComposerStatusScreen(viewModel: BaseActivityComposerViewModel) {
    Box(modifier = Modifier) {
        Column(
            modifier = Modifier.padding(top = 8.dp).imePadding().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if(viewModel.activityType == ActivityType.MESSAGE){
                TextSwitch(text = stringResource(I18nR.string._private), checked = viewModel.private) {
                    viewModel.private = it
                }
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.textFieldValue,
                onValueChange = {
                    if(viewModel.isError){
                        viewModel.isError = viewModel.textFieldValue.text.isBlank()
                    }
                    viewModel.textFieldValue = it
                },
                supportingText = {
                    if (viewModel.isError) {
                        Text(text = stringResource(id = I18nR.string.text_field_required))
                    }
                },
                label = { Text(text = stringResource(id = I18nR.string.write_a_status)) },
                isError = viewModel.isError,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            MarkdownEditor {
                viewModel.appendString(it)
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = stringResource(id = I18nR.string.activity_posting_guidelines)
                )
            }
        }
    }
}

@Composable
private fun ActivityComposerPreviewScreen(viewModel: BaseActivityComposerViewModel) {
    LaunchedEffect(viewModel.textFieldValue) {
        viewModel.toMarkdown()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        MarkdownText(
            modifier = Modifier.fillMaxWidth(),
            spanned = viewModel.spannedText
        )
    }

}