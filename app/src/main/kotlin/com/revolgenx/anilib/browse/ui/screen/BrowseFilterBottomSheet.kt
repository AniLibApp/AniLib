package com.revolgenx.anilib.browse.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseFilterViewModel
import com.revolgenx.anilib.common.data.constant.AlMediaSort
import com.revolgenx.anilib.common.data.tuples.to
import com.revolgenx.anilib.common.ui.component.menu.MultiSelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectSearchFilterMenu
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.menu.SortMenuItem
import com.revolgenx.anilib.common.ui.component.menu.SortOrder
import com.revolgenx.anilib.common.ui.component.menu.SortSelectMenu
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.type.MediaSort
import java.util.Calendar


private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 2
private const val yearGreater = 1940
private val yearList by lazy {
    (yearLesser downTo yearGreater).map { it.toString() }.toTypedArray()
}

@Composable
fun BrowseFilterBottomSheet(
    state: BottomSheetState,
    viewModel: BrowseFilterViewModel,
    onFilter: () -> Unit
) {
    BottomSheet(state = state, skipPeeked = true) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BrowseFilterRow {
                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.type),
                        entries = stringArrayResource(id = R.array.browse_type_menu),
                        onItemSelected = {

                        })
                }
                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.season),
                        entries = stringArrayResource(id = R.array.media_season),
                        noneItemText = anilib.i18n.R.string.all,
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }
            }

            BrowseFilterRow {
                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.status),
                        entries = stringArrayResource(id = R.array.media_status),
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }

                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.format),
                        entries = stringArrayResource(id = R.array.media_format),
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }

            }

            BrowseFilterRow {
                BrowseFilterRowItem {
                    val sort = MediaSort.TITLE_ENGLISH/*field.sort*/
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

//                        field.sort = mediaSort
                    }
                }

                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.source),
                        entries = stringArrayResource(id = R.array.browse_source_menu),
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }

            }

            BrowseFilterRow {
                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.year),
                        entries = yearList,
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }

                BrowseFilterRowItem {
                    SelectMenu(
                        label = stringResource(id = anilib.i18n.R.string.country_of_origin),
                        entries = stringArrayResource(id = R.array.browse_country_menu),
                        showNoneItem = true,
                        onItemSelected = {

                        })
                }
            }

            TextHeaderContent(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(id = anilib.i18n.R.string.genre)
            ) {
                SelectFilterMenu(
                    entries = stringArrayResource(id = R.array.media_genre).map { null to it },
                    onItemsSelected = {

                    }
                )
            }


            TextHeaderContent(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(id = anilib.i18n.R.string.tags)
            ) {
                SelectSearchFilterMenu(
                    entries = viewModel.mediaTagCollections.value,
                    showSearchFilter = true,
                    onItemsSelected = {

                    }
                )
            }



            TextHeaderContent(
                modifier = Modifier.fillMaxWidth(),
                heading = stringResource(id = anilib.i18n.R.string.streaming_on)
            ) {
                MultiSelectMenu(
                    entries = viewModel.streamingOnCollections.value,
                    onItemsSelected = {

                    }
                )
            }
        }
    }
}


@Composable
private fun BrowseFilterRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun RowScope.BrowseFilterRowItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.weight(1f)
    ) {
        content()
    }
}

@Composable
private fun TextHeaderContent(
    modifier: Modifier = Modifier,
    heading: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MediumText(
            modifier = Modifier.padding(start = 5.dp, bottom = 3.dp),
            text = heading,
            maxLines = 1,
            fontSize = 16.sp,
            lineHeight = 20.sp,
        )
        content()
    }
}

