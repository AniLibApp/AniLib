package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.social.ui.viewmodel.ActivityComposerViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class ActivityComposerScreen(val activityId: Int? = null) : AndroidScreen() {
    @Composable
    override fun Content() {
        ActivityComposerScreenContent()
    }
}


private typealias ActivityComposerScreenPage = PagerScreen<ActivityComposerScreenPages>

private enum class ActivityComposerScreenPages {
    STATUS,
    PREVIEW,
}

private val pages = listOf(
    ActivityComposerScreenPage(ActivityComposerScreenPages.STATUS, I18nR.string.status),
    ActivityComposerScreenPage(ActivityComposerScreenPages.PREVIEW, I18nR.string.preview),
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ActivityComposerScreenContent() {
    val pagerState = rememberPagerState() { pages.size }
    val viewModel = koinViewModel<ActivityComposerViewModel>()

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (ActivityComposerScreenPages.entries[page]) {
                ActivityComposerScreenPages.STATUS -> ActivityComposerStatusScreen(
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
    if (currentPage != 0) return

    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.text.value,
            onValueChange = { viewModel.text.value = it },
            label = { Text(text = stringResource(id = I18nR.string.write_a_status)) }
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
    if (currentPage != 1) return

    LaunchedEffect(viewModel.text.value) {
        viewModel.toMarkdown()
    }

    MarkdownText(
        modifier = Modifier.fillMaxWidth(),
        spanned = viewModel.spannedText.value
    )

}