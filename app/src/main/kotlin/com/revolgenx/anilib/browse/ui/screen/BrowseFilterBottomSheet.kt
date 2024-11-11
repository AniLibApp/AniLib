package com.revolgenx.anilib.browse.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.ui.model.FuzzyDateIntModel
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseFilterViewModel
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ui.component.checkbox.TextCheckbox
import com.revolgenx.anilib.common.ui.component.checkbox.TextTriStateCheckbox
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectModel
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectSearchFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectType
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.RegularText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcClose
import com.revolgenx.anilib.common.ui.icons.appicon.IcHistory
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.type.MediaSource
import com.revolgenx.anilib.type.MediaStatus
import java.util.Calendar


private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1f
private const val yearGreater = 1970f
private val yearList by lazy {
    (yearLesser.toInt() downTo 1940).map { it.toString() }.toTypedArray()
}

@Composable
fun BrowseFilterBottomSheet(
    state: BottomSheetState, viewModel: BrowseFilterViewModel, onFilter: () -> Unit
) {
    BottomSheet(
        state = state,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        Column {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val browseType = viewModel.field.browseType.value
                BrowseFilterRow {
                    BrowseFilterRowItem {
                        SelectMenu(label = stringResource(id = anilib.i18n.R.string.type),
                            entries = stringArrayResource(id = R.array.browse_type_menu),
                            selectedItemPosition = viewModel.field.browseType.value.ordinal,
                            onItemSelected = {
                                viewModel.updateBrowseType(BrowseTypes.entries[it])
                            }
                        )
                    }


                    if (browseType == BrowseTypes.ANIME) {
                        BrowseFilterRowItem {
                            SelectMenu(label = stringResource(id = anilib.i18n.R.string.season),
                                entries = stringArrayResource(id = R.array.media_season),
                                noneItemText = anilib.i18n.R.string.all,
                                showNoneItem = true,
                                selectedItemPosition = viewModel.field.season?.ordinal,
                                onItemSelected = {
                                    viewModel.field.season = MediaSeason.entries.getOrNull(it)
                                })
                        }
                    }
                }


                if (browseType != BrowseTypes.ANIME && browseType != BrowseTypes.MANGA) {
                    return@Column
                }

                BrowseFilterRow {
                    BrowseFilterRowItem {
                        SelectMenu(label = stringResource(id = anilib.i18n.R.string.status),
                            entries = stringArrayResource(id = R.array.media_status),
                            showNoneItem = true,
                            selectedItemPosition = viewModel.field.status?.ordinal,
                            onItemSelected = {
                                viewModel.field.status = MediaStatus.entries.getOrNull(it)
                            })
                    }


                    BrowseFilterRowItem {

                        val selectedFormats =
                            viewModel.field.formatsIn?.map { it.ordinal }.orEmpty()
                        val formats = stringArrayResource(id = R.array.media_format)

                        MultiSelectMenu(
                            label = stringResource(id = anilib.i18n.R.string.format),
                            text = { it.second },
                            entries = formats.mapIndexed { index, s ->
                                MultiSelectModel(
                                    mutableStateOf(
                                        selectedFormats.contains(
                                            index
                                        )
                                    ), index to s
                                )
                            },
                        ) { selectedItems ->
                            viewModel.field.formatsIn =
                                selectedItems.takeIf { it.isNotEmpty() }?.mapNotNull {
                                    MediaFormat.entries.getOrNull(it.first)
                                }
                        }
                    }

                }

                BrowseFilterRow {
                    BrowseFilterRowItem {
                        val sort = viewModel.field.sort
                        var selectedSortIndex: Int? = null
                        var selectedSortOrder: SortOrder = SortOrder.NONE

                        if (sort != null) {
                            val isDesc = sort.rawValue.endsWith("_DESC")
                            val alMediaSort =
                                AlMediaSort.from(if (isDesc) sort.ordinal - 1 else sort.ordinal)
                            selectedSortIndex = alMediaSort?.ordinal
                            selectedSortOrder = if (isDesc) SortOrder.DESC else SortOrder.ASC
                        }

                        val sortMenus =
                            stringArrayResource(id = R.array.media_sort).mapIndexed { index, s ->
                                SortMenuItem(
                                    s,
                                    if (index == selectedSortIndex) selectedSortOrder else SortOrder.NONE
                                )
                            }

                        SortSelectMenu(
                            label = stringResource(id = anilib.i18n.R.string.sort),
                            entries = sortMenus,
                        ) { index, selectedItem ->
                            var mediaSort: MediaSort? = null

                            if (selectedItem != null) {
                                val alMediaSort = AlMediaSort.entries[index].sort
                                val selectedSort = if (selectedItem.order == SortOrder.DESC) {
                                    alMediaSort + 1
                                } else {
                                    alMediaSort
                                }
                                mediaSort = MediaSort.entries.toTypedArray()[selectedSort]
                            }

                            viewModel.field.sort = mediaSort
                        }
                    }

                    BrowseFilterRowItem {
                        SelectMenu(label = stringResource(id = anilib.i18n.R.string.source),
                            entries = stringArrayResource(id = R.array.browse_source_menu),
                            showNoneItem = true,
                            selectedItemPosition = viewModel.field.source?.ordinal,
                            onItemSelected = {
                                viewModel.field.source = MediaSource.entries.getOrNull(it)
                            })
                    }

                }

                BrowseFilterRow {
                    BrowseFilterRowItem {
                        SelectMenu(label = stringResource(id = anilib.i18n.R.string.year),
                            entries = yearList,
                            showNoneItem = true,
                            selectedItemPosition = yearList.indexOf(viewModel.field.year.toString()),
                            onItemSelected = {
                                viewModel.field.year = yearList.getOrNull(it)?.toInt()
                            })
                    }

                    BrowseFilterRowItem {
                        SelectMenu(label = stringResource(id = anilib.i18n.R.string.country_of_origin),
                            entries = stringArrayResource(id = R.array.browse_country_menu),
                            showNoneItem = true,
                            selectedItemPosition = viewModel.field.countryOfOrigin,
                            onItemSelected = {
                                viewModel.field.countryOfOrigin = it.takeIf { it >= 0 }
                            })
                    }
                }

                if (viewModel.isLoggedIn) {
                    Row {
                        BrowseFilterRowItem {
                            TriStateCheckboxFilter(
                                text = stringResource(id = anilib.i18n.R.string.on_list),
                                isChecked = viewModel.field.onList
                            ) { onList ->
                                viewModel.field.onList = onList
                            }
                        }


                        if (viewModel.canShowAdultContent) {
                            BrowseFilterRowItem {
                                TriStateCheckboxFilter(
                                    text = stringResource(id = anilib.i18n.R.string.hentai),
                                    isChecked = viewModel.field.isHentai
                                ) { isHentai ->
                                    viewModel.field.isHentai = isHentai
                                }
                            }
                        }
                    }
                }

                Row {
                    BrowseFilterRowItem {
                        TextCheckbox(
                            text = stringResource(id = anilib.i18n.R.string.doujin),
                            checked = viewModel.field.doujins == true
                        ) {
                            viewModel.field.doujins = it
                        }
                    }
                }

                TextHeaderContent(
                    modifier = Modifier.fillMaxWidth(),
                    heading = stringResource(id = anilib.i18n.R.string.genre)
                ) {
                    SelectFilterMenu(
                        entries = viewModel.selectGenreCollections.value,
                        onItemsSelected = {
                            viewModel.isExcludedGenreFiltered = true

                            viewModel.field.genreIn =
                                it.filter { it.selected.value == SelectType.INCLUDED }
                                    .map { it.data }
                                    .toMutableList()

                            viewModel.field.genreNotIn =
                                it.filter { it.selected.value == SelectType.EXCLUDED }
                                    .map { it.data }
                                    .toMutableList()
                        })
                }


                TextHeaderContent(
                    modifier = Modifier.fillMaxWidth(),
                    heading = stringResource(id = anilib.i18n.R.string.tags)
                ) {
                    SelectSearchFilterMenu(
                        entries = viewModel.selectMediaTagCollections.value,
                        showSearchFilter = true,
                        onItemsSelected = {
                            viewModel.isExcludedTagsFiltered = true

                            viewModel.field.tagsIn =
                                it.filter { it.selected.value == SelectType.INCLUDED }
                                    .map { it.data }
                                    .toMutableList()
                            viewModel.field.tagsNotIn =
                                it.filter { it.selected.value == SelectType.EXCLUDED }
                                    .map { it.data }
                                    .toMutableList()
                        }
                    )
                }

                ExternalLinkFilter(viewModel)
                YearRangeFilter(viewModel)
                EpisodesOrChaptersRangeFilter(viewModel)
                DurationOrVolumesRangeFilter(viewModel)

            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    viewModel.saveFilterData()
                    onFilter()
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            viewModel.reloadPrevious()
                        },
                        imageVector = AppIcons.IcHistory,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = anilib.i18n.R.string.filter)
                    )

                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable {
                                viewModel.clearFilter()
                            },
                        imageVector = AppIcons.IcClose,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun EpisodesOrChaptersRangeFilter(viewModel: BrowseFilterViewModel) {
    val field = viewModel.field
    val isAnime = field.browseType.value == BrowseTypes.ANIME

    val episodesLessThan = viewModel.episodesLessThan
    val chaptersLessThan = viewModel.chaptersLessThan

    val sliderPosition = remember(field, field.browseType.value) {
        if (isAnime) {
            mutableStateOf(
                (field.episodesGreater?.toFloat() ?: 0f)..(field.episodesLesser?.toFloat()
                    ?: episodesLessThan)
            )
        } else {
            mutableStateOf(
                (field.chaptersGreater?.toFloat() ?: 0f)..(field.chaptersLesser?.toFloat()
                    ?: chaptersLessThan)
            )
        }
    }

    val lessThan = if (isAnime) episodesLessThan else chaptersLessThan

    BrowseFilterRangeSlider(
        heading = stringResource(id = if (isAnime) anilib.i18n.R.string.episodes else anilib.i18n.R.string.chapters),
        start = 0f,
        end = lessThan,
        position = sliderPosition
    ) { start, end ->
        val newStart = start.takeIf { it != 0f }?.toInt()
        val newEnd = end.takeIf { it != lessThan }?.toInt()

        if (isAnime) {
            field.episodesGreater = newStart
            field.episodesLesser = newEnd
        } else {
            field.chaptersGreater = newStart
            field.chaptersLesser = newEnd
        }

    }
}

@Composable
fun DurationOrVolumesRangeFilter(viewModel: BrowseFilterViewModel) {
    val field = viewModel.field
    val isAnime = field.browseType.value == BrowseTypes.ANIME

    val durationLessThan = viewModel.durationLessThan
    val volumesLessThan = viewModel.volumesLessThan

    val sliderPosition = remember(field, field.browseType.value) {
        if (isAnime) {
            mutableStateOf(
                (field.durationGreater?.toFloat() ?: 0f)..(field.durationLesser?.toFloat()
                    ?: durationLessThan)
            )
        } else {
            mutableStateOf(
                (field.volumesGreater?.toFloat() ?: 0f)..(field.volumesLesser?.toFloat()
                    ?: volumesLessThan)
            )
        }
    }

    val lessThan = if (isAnime) durationLessThan else volumesLessThan

    BrowseFilterRangeSlider(
        heading = stringResource(id = if (isAnime) anilib.i18n.R.string.duration else anilib.i18n.R.string.volumes),
        start = 0f,
        end = lessThan,
        position = sliderPosition
    ) { start, end ->
        val newStart = start.takeIf { it != 0f }?.toInt()
        val newEnd = end.takeIf { it != lessThan }?.toInt()

        if (isAnime) {
            field.durationGreater = newStart
            field.durationLesser = newEnd
        } else {
            field.volumesGreater = newStart
            field.volumesLesser = newEnd
        }

    }
}

@Composable
private fun TriStateCheckboxFilter(
    text: String,
    isChecked: Boolean?,
    onCheckChange: (checked: Boolean?) -> Unit
) {
    val currentToggleState = remember(isChecked) {
        mutableStateOf(
            when (isChecked) {
                true -> ToggleableState.On
                false -> ToggleableState.Indeterminate
                null -> ToggleableState.Off
            }
        )
    }
    TextTriStateCheckbox(
        text = text,
        toggleState = currentToggleState.value,
        onToggleStateChange = {
            currentToggleState.value = it
            onCheckChange(
                when (it) {
                    ToggleableState.On -> true
                    ToggleableState.Indeterminate -> false
                    ToggleableState.Off -> null
                }
            )
        }
    )
}


@Composable
private fun YearRangeFilter(viewModel: BrowseFilterViewModel) {
    val field = viewModel.field

    val sliderPosition = remember(field) {
        mutableStateOf(
            (field.yearGreater?.year?.toFloat() ?: yearGreater)..(field.yearLesser?.year?.toFloat()
                ?: yearLesser)
        )
    }

    BrowseFilterRangeSlider(
        heading = stringResource(id = anilib.i18n.R.string.year_range),
        start = yearGreater,
        end = yearLesser,
        position = sliderPosition
    ) { start, end ->
        field.yearGreater =
            start.takeIf { it != yearGreater }?.let { FuzzyDateIntModel(it.toInt(), 0, 0) }
        field.yearLesser =
            end.takeIf { it != yearLesser }?.let { FuzzyDateIntModel(it.toInt(), 0, 0) }
    }
}

@Composable
fun BrowseFilterRangeSlider(
    heading: String,
    start: Float,
    end: Float,
    position: MutableState<ClosedFloatingPointRange<Float>>,
    onValueChanged: (start: Float, end: Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BrowseFilterHeader(heading = heading)
        if (position.value.start != start || position.value.endInclusive != end) {
            RegularText(
                text = stringResource(id = anilib.i18n.R.string.s_dash_s).format(
                    position.value.start.toInt(), position.value.endInclusive.toInt()
                )
            )
            Icon(
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        position.value = start..end
                        onValueChanged(start, end)
                    }, imageVector = AppIcons.IcClose, contentDescription = null
            )
        }
    }
    val steps = (end - start - 1).toInt()

    RangeSlider(
        value = position.value,
        steps = steps,
        onValueChange = { range -> position.value = range },
        valueRange = start..end,
        onValueChangeFinished = {
            onValueChanged(position.value.start, position.value.endInclusive)
        },
    )
}


@Composable
private fun ExternalLinkFilter(viewModel: BrowseFilterViewModel) {
    val field = viewModel.field
    val isAnime = field.browseType.value == BrowseTypes.ANIME
    TextHeaderContent(
        modifier = Modifier.fillMaxWidth(), heading = stringResource(
            id = if (isAnime) anilib.i18n.R.string.streaming_on
            else anilib.i18n.R.string.readable_on
        )
    ) {
        MultiSelectMenu(entries = if (isAnime) viewModel.selectStreamingOnCollections.value else viewModel.selectReadableOnCollections.value,
            text = { it.site.orEmpty() },
            onItemsSelected = {
                if (isAnime) {
                    field.streamingOn = it.map { it.id }
                } else {
                    field.readableOn = it.map { it.id }
                }
            })
    }
}


@Composable
private fun BrowseFilterRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun RowScope.BrowseFilterRowItem(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.weight(1f)
    ) {
        content()
    }
}

@Composable
private fun TextHeaderContent(
    modifier: Modifier = Modifier, heading: String, content: @Composable () -> Unit
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        BrowseFilterHeader(heading = heading)
        content()
    }
}

@Composable
private fun BrowseFilterHeader(heading: String) {
    MediumText(
        modifier = Modifier.padding(start = 5.dp, bottom = 3.dp),
        text = heading,
        maxLines = 1,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
}
