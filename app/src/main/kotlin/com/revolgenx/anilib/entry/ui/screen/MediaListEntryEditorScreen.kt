package com.revolgenx.anilib.entry.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.checkbox.TextCheckbox
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.date.CalendarBottomSheet
import com.revolgenx.anilib.common.ui.component.dialog.ConfirmationDialog
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalendar
import com.revolgenx.anilib.common.ui.icons.appicon.IcClose
import com.revolgenx.anilib.common.ui.icons.appicon.IcDelete
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.entry.ui.component.CountEditor
import com.revolgenx.anilib.entry.ui.component.DoubleCountEditor
import com.revolgenx.anilib.entry.ui.component.MediaListEntryScore
import com.revolgenx.anilib.entry.ui.viewmodel.MediaListEntryEditorViewModel
import com.revolgenx.anilib.list.ui.model.getAlMediaListStatusForType
import com.revolgenx.anilib.list.ui.model.getStatusFromAlMediaListStatus
import com.revolgenx.anilib.list.ui.model.toAlMediaListStatus
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.type.ScoreFormat
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import anilib.i18n.R as I18nR

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

    val editTitle = stringResource(id = I18nR.string.edit)
    var title by remember { mutableStateOf(editTitle) }
    val openConfirmDialog = remember { mutableStateOf(false) }

    ScreenScaffold(
        title = title,
        actions = {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = if (viewModel.isFavourite) AppIcons.IcHeart else AppIcons.IcHeartOutline,
                    contentDescription = stringResource(id = I18nR.string.favourite)
                )
            }
            if (viewModel.userHasMediaListEntry) {
                IconButton(onClick = { openConfirmDialog.value = true }) {
                    Icon(
                        imageVector = AppIcons.IcDelete,
                        contentDescription = stringResource(id = I18nR.string.delete)
                    )
                }
            }

            IconButton(onClick = {
                viewModel.save()
            }) {
                Icon(
                    imageVector = AppIcons.IcSave,
                    contentDescription = stringResource(id = I18nR.string.save)
                )
            }
        },
    ) {

        val snackbar = localSnackbarHostState()
        when (viewModel.deleteResource.value) {
            is ResourceState.Error -> {
                val failedToDelete = stringResource(id = I18nR.string.failed_to_delete)
                val retry = stringResource(id = I18nR.string.retry)
                LaunchedEffect(viewModel) {
                    when (snackbar.showSnackbar(
                        failedToDelete, retry, duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.deleteResource.value = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.delete()
                        }
                    }
                }
            }
            is ResourceState.Success -> {
                localNavigator().pop()
            }
            else -> {}
        }

        when (viewModel.saveResource.value) {
            is ResourceState.Error -> {
                val failedToSave = stringResource(id = I18nR.string.failed_to_save)
                val retry = stringResource(id = I18nR.string.retry)
                LaunchedEffect(viewModel) {
                    when (snackbar.showSnackbar(
                        failedToSave, retry, duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.saveResource.value = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.save()
                        }
                    }
                }
            }

            is ResourceState.Success -> {
                localNavigator().pop()
            }

            else -> {}
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            ResourceScreen(viewModel = viewModel) { userMedia ->
                val media = userMedia.media ?: return@ResourceScreen
                val user = userMedia.user ?: return@ResourceScreen
                val entryField = viewModel.saveField
                val isAnime = media.isAnime

                MediaTitleType {
                    val mediaTitle = media.title?.title(it)
                    title = mediaTitle.orEmpty()
                    ConfirmationDialog(openDialog = openConfirmDialog,
                        message = stringResource(id = I18nR.string.do_you_really_want_to_delete_the_entry_s).format(
                            mediaTitle?.let { " $it" } ?: "")) {
                        viewModel.delete()
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(I18nR.string.status),
                            fixedCardHeight = false
                        ) {
                            val mediaListStatusEntries =
                                getAlMediaListStatusForType(type = media.type).toList()
                            val mediaListStatus = entryField.status
                            SelectMenu(
                                entries = mediaListStatusEntries,
                                selectedItemPosition = mediaListStatus.toAlMediaListStatus()
                            ) { newStatus ->
                                entryField.status = getStatusFromAlMediaListStatus(newStatus)
                            }
                        }

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(I18nR.string.score)
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
                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(if (isAnime) I18nR.string.episode_progress else I18nR.string.chapter_progress)
                        ) {
                            CountEditor(
                                count = entryField.progress ?: 0,
                                max = if (isAnime) media.episodes else media.chapters
                            ) { count ->
                                entryField.progress = count
                            }
                        }


                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(if (isAnime) I18nR.string.total_rewatches else I18nR.string.total_rereads)
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
                            heading = stringResource(I18nR.string.start_date)
                        ) {
                            val openCalendar = remember { mutableStateOf(false) }
                            val startedAt = entryField.startedAt?.toZoneDateTime()
                            CalendarPicker(openCalendar = openCalendar,
                                selectedDateMillis = startedAt?.toInstant()?.toEpochMilli(),
                                text = entryField.startedAt?.toString() ?: "",
                                onClear = {
                                    entryField.startedAt = null
                                },
                                onDateSelected = { selectedDateMillis ->
                                    val newStartedAt = ZonedDateTime.ofInstant(
                                        Instant.ofEpochMilli(selectedDateMillis),
                                        ZoneId.systemDefault()
                                    )
                                    entryField.startedAt = FuzzyDateModel.from(newStartedAt)
                                })

                        }

                        CardHeaderContent(
                            modifier = Modifier.weight(1f),
                            heading = stringResource(I18nR.string.end_date)
                        ) {
                            val openCalendar = remember { mutableStateOf(false) }
                            val completedAt = entryField.completedAt?.toZoneDateTime()

                            CalendarPicker(openCalendar = openCalendar,
                                selectedDateMillis = completedAt?.toInstant()?.toEpochMilli(),
                                text = entryField.completedAt?.toString() ?: "",
                                onClear = {
                                    entryField.completedAt = null
                                },
                                onDateSelected = { selectedDateMillis ->
                                    val newCompletedAt = ZonedDateTime.ofInstant(
                                        Instant.ofEpochMilli(selectedDateMillis),
                                        ZoneId.systemDefault()
                                    )
                                    entryField.completedAt = FuzzyDateModel.from(newCompletedAt)
                                })
                        }
                    }

                    if (!isAnime) {
                        Row(
                            modifier = Modifier.padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CardHeaderContent(
                                modifier = Modifier.weight(1f),
                                heading = stringResource(I18nR.string.volume_progress)
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

                    CardHeaderContent(
                        heading = stringResource(I18nR.string.notes), fixedCardHeight = false
                    ) {
                        BasicTextField(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 14.dp),
                            value = entryField.notes ?: "",
                            textStyle = LocalTextStyle.current,
                            onValueChange = {
                                entryField.notes = it
                            })
                    }


                    val hasCustomList = entryField.customLists.isNullOrEmpty().not()

                    TextCheckbox(
                        text = stringResource(id = I18nR.string._private),
                        checked = entryField.private
                    ) {
                        entryField.private = it
                    }

                    if (hasCustomList) {

                        TextCheckbox(
                            text = stringResource(id = I18nR.string.hide_from_status_lists),
                            checked = entryField.hiddenFromStatusLists ?: false
                        ) {
                            entryField.hiddenFromStatusLists = it
                        }

                    }


                    HorizontalDivider(modifier = Modifier.padding(4.dp))

                    TextHeaderContent(
                        heading = stringResource(I18nR.string.custom_lists)
                    ) {
                        if (hasCustomList) {
                            entryField.customLists!!.map { cList ->
                                TextCheckbox(
                                    text = cList.first, checked = cList.second
                                ) {
                                    cList.second = it
                                }
                            }
                        } else {
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = stringResource(id = I18nR.string.no_custom_lists)
                            )
                        }
                    }

                    user.mediaListOptions?.takeIf { if (media.isAnime) it.isAnimeAdvancedScoreEnabled else it.isMangaAdvancedScoreEnabled }
                        ?.let {

                            HorizontalDivider(modifier = Modifier.padding(4.dp))
                            TextHeaderContent(
                                heading = stringResource(id = I18nR.string.advanced_scores),
                            ) {
                                entryField.advancedScores?.let {
                                    Grid(
                                        modifier = Modifier.fillMaxWidth(), items = it,
                                        rowSpacing = 8.dp,
                                        columnSpacing = 8.dp
                                    ) { scoreModel ->
                                        CardHeaderContent(
                                            heading = scoreModel.heading
                                        ) {
                                            DoubleCountEditor(
                                                count = scoreModel.score.value,
                                                max = 100.0,
                                                incrementBy = 1.0
                                            ) { count ->
                                                scoreModel.score.value = count
                                                viewModel.onAdvancedScoreChange(
                                                    user.mediaListOptions.scoreFormat
                                                        ?: ScoreFormat.POINT_100
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                }


            }
        }
    }
}

private val FilterContentHeight = 56.dp

@Composable
private fun CardHeaderContent(
    modifier: Modifier = Modifier,
    heading: String,
    fixedCardHeight: Boolean = true,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = heading,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
            Box(modifier = Modifier.let {
                if (fixedCardHeight) {
                    it.height(FilterContentHeight)
                } else it
            }) {
                content()
            }
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
        MediumText(
            modifier = Modifier.padding(start = 5.dp, bottom = 3.dp),
            text = heading,
            fontSize = 16.sp,
            maxLines = 1,
            lineHeight = TextUnit.Unspecified
        )
        content()
    }
}


@Composable
private fun CalendarPicker(
    openCalendar: MutableState<Boolean> = mutableStateOf(false),
    selectedDateMillis: Long?,
    text: String,
    onClear: OnClick,
    onDateSelected: (selectedDateMillis: Long) -> Unit
) {
    CalendarBottomSheet(
        openBottomSheet = openCalendar,
        initialSelectedDateMillis = selectedDateMillis,
        onDateSelected = onDateSelected
    )
    TextButton(
        modifier = Modifier.fillMaxSize(),
        onClick = { openCalendar.value = true },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Icon(
            imageVector = AppIcons.IcCalendar,
            contentDescription = "calendar"
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 2.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 13.sp
        )
        selectedDateMillis?.let {
            Icon(
                modifier = Modifier.clickable {
                    onClear()
                },
                imageVector = AppIcons.IcClose,
                contentDescription = "clear"
            )
        }
    }
}


@Preview
@Composable
fun HeaderContentPreview() {

}