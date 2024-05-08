package com.revolgenx.anilib.review.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheetLayout
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcSend
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.entry.ui.component.CountEditor
import com.revolgenx.anilib.review.ui.viewmodel.ReviewComposerViewModel
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.component.TitleFontSize
import com.revolgenx.anilib.social.ui.component.MarkdownEditor
import kotlinx.coroutines.launch


private typealias ReviewComposerScreenPage = PagerScreen<ReviewComposerScreenPages>

private enum class ReviewComposerScreenPages {
    COMPOSE,
    PREVIEW,
}

private val pages = listOf(
    ReviewComposerScreenPage(ReviewComposerScreenPages.COMPOSE, R.string.compose),
    ReviewComposerScreenPage(ReviewComposerScreenPages.PREVIEW, R.string.preview),
)

@Composable
fun ReviewComposerBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: ReviewComposerViewModel
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
            ReviewComposerContent(viewModel = viewModel) {
                scope.launch {
                    bottomSheetState.collapse()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReviewComposerContent(
    viewModel: ReviewComposerViewModel,
    dismiss: () -> Unit
) {
    val context = localContext()
    val pagerState = rememberPagerState() { pages.size }

    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    PagerScreenScaffold(
        navigationIcon = {},
        actions = {
            if (viewModel.reviewId != -1) {
                ActionMenu(
                    icon = AppIcons.IcDelete
                ) {
                    viewModel.delete()
                }
            }
            ActionMenu(
                icon = AppIcons.IcSend,
                enabled = viewModel.canSend.value
            ) {
                viewModel.save()
            }
        },
        userScrollEnabled = false,
        pages = pages,
        pagerState = pagerState,
        windowInsets = emptyWindowInsets()
    ) { page ->
        val snackbar = localSnackbarHostState()
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (ReviewComposerScreenPages.entries[page]) {
                ReviewComposerScreenPages.COMPOSE -> ReviewComposerScreen(
                    viewModel
                )

                ReviewComposerScreenPages.PREVIEW -> ReviewComposerPreviewScreen(
                    viewModel
                )
            }
        }

        LaunchedEffect(viewModel.saveResource) {
            when (viewModel.saveResource) {
                is ResourceState.Error -> {
                    val failedToSave = context.getString(R.string.failed_to_save)
                    val retry = context.getString(R.string.retry)
                    when (snackbar.showSnackbar(
                        failedToSave,
                        retry,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.saveResource = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.save()
                        }
                    }
                }

                else -> {

                }
            }
        }

        LaunchedEffect(viewModel.deleteResource) {
            when (viewModel.deleteResource) {
                is ResourceState.Error -> {
                    val failedToDelete = context.getString(R.string.failed_to_delete)
                    val retry = context.getString(R.string.retry)
                    when (snackbar.showSnackbar(
                        failedToDelete,
                        retry,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.deleteResource = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.delete()
                        }
                    }
                }

                is ResourceState.Success -> {
                    viewModel.deleteResource = null
                    dismiss()
                }

                else -> {}
            }
        }
    }
}


@Composable
private fun ReviewComposerScreen(viewModel: ReviewComposerViewModel) {
    ResourceScreen(viewModel = viewModel) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.body,
                onValueChange = { viewModel.body = it },
                label = { Text(text = stringResource(id = R.string.write_review)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                supportingText = {
                    Row {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            text = stringResource(id = R.string.write_review_valid_desc)
                        )
                        Text(text = viewModel.body.text.length.toString())
                    }
                }
            )

            MarkdownEditor {
                viewModel.appendString(it)
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.summary,
                onValueChange = { viewModel.summary = it },
                label = { Text(text = stringResource(id = R.string.review_summary)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                supportingText = {
                    Row {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            text = stringResource(id = R.string.write_review_summary_valid_desc)
                        )
                        Text(text = viewModel.summary.length.toString())
                    }
                }
            )


            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string.score),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                    )
                    CountEditor(
                        count = viewModel.score,
                        max = 100
                    ) {
                        viewModel.score = it
                    }
                }

                VerticalDivider(modifier = Modifier.fillMaxHeight())

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(id = R.string._private),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                    )

                    Switch(
                        checked = viewModel.private,
                        onCheckedChange = {
                            viewModel.private = it
                        },
                        modifier = Modifier.padding(start = 16.dp),
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())


        }
    }

}

@Composable
fun ReviewComposerPreviewScreen(viewModel: ReviewComposerViewModel) {
    LaunchedEffect(viewModel.body) {
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
            spanned = viewModel.spannedBody
        )
    }

}