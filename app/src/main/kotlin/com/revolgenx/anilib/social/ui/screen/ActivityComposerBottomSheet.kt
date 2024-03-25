package com.revolgenx.anilib.social.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
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
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSend
import com.revolgenx.anilib.social.ui.component.MarkdownEditor
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
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
    viewModel: ActivityComposerViewModel
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


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ActivityComposerScreenContent(
    viewModel: ActivityComposerViewModel
) {
    val pagerState = rememberPagerState() { pages.size }

    PagerScreenScaffold(
        navigationIcon = {},
        actions = {
            ActionMenu(
                icon = AppIcons.IcSend
            ) {

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
                    viewModel,
                    pagerState.currentPage
                )

                ActivityComposerScreenPages.PREVIEW -> ActivityComposerPreviewScreen(
                    viewModel,
                    pagerState.currentPage
                )
            }
        }
    }
}


@Composable
fun ActivityComposerStatusScreen(viewModel: ActivityComposerViewModel, currentPage: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        MarkdownEditor {
            viewModel.appendString(it)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.textFieldValue.value,
            onValueChange = { viewModel.textFieldValue.value = it },
            label = { Text(text = stringResource(id = I18nR.string.write_a_status)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.CenterEnd),
                text = stringResource(id = I18nR.string.activity_posting_guidelines)
            )
        }
    }
}

@Composable
fun ActivityComposerPreviewScreen(viewModel: ActivityComposerViewModel, currentPage: Int) {
    LaunchedEffect(viewModel.textFieldValue.value) {
        viewModel.toMarkdown()
    }

    MarkdownText(
        modifier = Modifier.fillMaxWidth(),
        spanned = viewModel.spannedText.value
    )

}