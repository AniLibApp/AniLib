package com.revolgenx.anilib.entry.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.ResourceScreen
import com.revolgenx.anilib.entry.ui.component.CountEditor
import com.revolgenx.anilib.entry.ui.component.DoubleCountEditor
import com.revolgenx.anilib.entry.ui.component.MediaListEntryScore
import com.revolgenx.anilib.entry.ui.viewmodel.MediaListEntryEditorViewModel
import com.revolgenx.anilib.list.ui.model.getAlMediaListStatusForType
import com.revolgenx.anilib.list.ui.model.getStatusFromAlMediaListStatus
import com.revolgenx.anilib.list.ui.model.toAlMediaListStatus
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.type.ScoreFormat
import org.koin.androidx.compose.koinViewModel

class MediaListEntryEditorScreen(private val mediaId: Int, private val userId: Int) :
    AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: MediaListEntryEditorViewModel = koinViewModel()
        viewModel.field.also {
            it.mediaId = mediaId
            it.userId = userId
        }
        MediaListEditScreenContent(viewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaListEditScreenContent(
    viewModel: MediaListEntryEditorViewModel,
) {

    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    val editTitle = stringResource(id = R.string.edit)
    var title by remember { mutableStateOf(editTitle) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    ScreenScaffold(
        title = title,
        actions = {},
        scrollBehavior = scrollBehavior
    ) {
        Column(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        ) {
            ResourceScreen(
                resourceState = viewModel.resource.value,
                refresh = { viewModel.refresh() }
            ) { userMedia ->
                val media = userMedia.media ?: return@ResourceScreen
                val user = userMedia.user ?: return@ResourceScreen
                val entryField = viewModel.saveField

                val isAnime = media.isAnime

                MediaTitleType {
                    media.title?.title(it)?.let {
                        title = it
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(id = R.string.status)
                        ) {
                            val mediaListStatusEntries =
                                getAlMediaListStatusForType(type = media.type).toList()
                            val mediaListStatus = entryField.status
                            SelectMenu(
                                modifier = Modifier.height(FilterContentHeight),
                                entries = mediaListStatusEntries,
                                selectedItemPosition = mediaListStatus.toAlMediaListStatus()
                            ) { newStatus ->
                                entryField.status = getStatusFromAlMediaListStatus(newStatus)
                            }
                        }

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(id = R.string.score)
                        ) {
                            MediaListEntryScore(
                                score = entryField.score,
                                scoreFormat = user.mediaListOptions?.scoreFormat
                                    ?: ScoreFormat.POINT_100
                            ) { score ->
                                entryField.score = score
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val progressHeading =
                            stringResource(id = if (isAnime) R.string.episode_progress else R.string.chapter_progress)
                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = progressHeading
                        ) {
                            CountEditor(
                                count = entryField.progress ?: 0,
                                max = if (isAnime) media.episodes else media.chapters
                            ) { count ->
                                entryField.progress = count
                            }
                        }

                        val repeatHeading =
                            stringResource(id = if (isAnime) R.string.total_rewatches else R.string.total_rereads)

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = repeatHeading
                        ) {
                            CountEditor(
                                count = entryField.repeat ?: 0,
                            ) { count ->
                                entryField.repeat = count
                            }
                        }

                    }

                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(id = R.string.start_date)
                        ) {
                            CountEditor(
                                count = entryField.progressVolumes ?: 0,
                            ) { count ->
                                entryField.progressVolumes = count
                            }
                        }

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(id = R.string.end_date)
                        ) {
                            CountEditor(
                                count = entryField.progressVolumes ?: 0,
                            ) { count ->
                                entryField.progressVolumes = count
                            }
                        }
                    }

                    if (!isAnime) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CardHeaderContent(
                                modifier = Modifier.weight(1f),
                                heading = stringResource(id = R.string.volume_progress)
                            ) {
                                CountEditor(
                                    count = entryField.progressVolumes ?: 0,
                                ) { count ->
                                    entryField.progressVolumes = count
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }


            }
        }
    }
}

private val FilterContentHeight = 50.dp

@Composable
private fun CardHeaderContent(
    modifier: Modifier = Modifier,
    heading: String,
    content: @Composable () -> Unit
) {
    TextHeaderContent(modifier = modifier, heading = heading) {
        Card(
            modifier = Modifier
                .height(FilterContentHeight)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
private fun TextHeaderContent(
    modifier: Modifier = Modifier,
    heading: String,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            modifier = Modifier.padding(start = 5.dp, bottom = 3.dp),
            text = heading,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            letterSpacing = 0.3.sp
        )
        content()
    }
}

@Preview
@Composable
fun HeaderContentPreview() {

}