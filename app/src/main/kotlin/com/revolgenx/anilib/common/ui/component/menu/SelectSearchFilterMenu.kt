package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.theme.excluded_color

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
)
@Composable
fun SelectSearchFilterMenu(
    modifier: Modifier = Modifier,
    entries: List<MutablePair<Boolean?, String>>,
    showSearchFilter: Boolean = false,
    onItemsSelected: (items: List<MutablePair<Boolean, String>>) -> Unit
) {
    val mutableEntries by remember {
        mutableStateOf(entries.map { mutableStateOf(it) })
    }

    var anchorWidth by remember { mutableIntStateOf(0) }

    fun getSelectedItems() = mutableEntries.map { it.value }.filter { it.first != null }

    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember {
        mutableStateOf(getSelectedItems())
    }

    var search by remember {
        mutableStateOf("")
    }

    val searchFilteredItems = remember {
        derivedStateOf {
            mutableEntries.filter {
                it.value.second.contains(
                    search, ignoreCase = true
                )
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { },
    ) {
        Card(modifier = modifier
            .menuAnchor()
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                anchorWidth = coordinates.size.width
            }
            .clickable(indication = null, interactionSource = remember {
                MutableInteractionSource()
            }) {
                expanded = true
            }) {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                if (selectedItems.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource(id = R.string.none)
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    selectedItems.forEach {
                        AssistChip(modifier = Modifier
                            .height(32.dp)
                            .clickable(indication = null, interactionSource = remember {
                                MutableInteractionSource()
                            }) {},
                            onClick = {
                                expanded = true
                            },
                            label = { Text(text = it.second) },
                            border = AssistChipDefaults.assistChipBorder(
                                true,
                                borderColor = if (it.first == false) excluded_color else MaterialTheme.colorScheme.outline
                            ),
                            colors = AssistChipDefaults.assistChipColors(
                                leadingIconContentColor = if (it.first == false) excluded_color else MaterialTheme.colorScheme.onSurface
                            ),
                            trailingIcon = {
                                Box(modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    }) {
                                    it.first = null
                                    selectedItems = getSelectedItems()
                                    @Suppress("UNCHECKED_CAST") onItemsSelected.invoke(
                                        selectedItems as List<MutablePair<Boolean, String>>
                                    )
                                }) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = AppIcons.IcCancel,
                                        contentDescription = stringResource(id = R.string.clear)
                                    )
                                }
                            })
                    }
                }
            }

            val menuWidth = with(LocalDensity.current) { anchorWidth.toDp() }
            val menuHeight = 40.dp
            val entriesSize = searchFilteredItems.value.size
            val menuItemHeight = menuHeight * when {
                entriesSize > 10 -> 10
                entriesSize < 1 -> 1
                else -> entriesSize
            } + 72.dp


            DropdownMenu(expanded = expanded, onDismissRequest = {
                search = ""
                expanded = false
            }) {
                Box(modifier = Modifier.size(width = menuWidth, height = menuItemHeight)) {
                    LazyColumn {
                        if (showSearchFilter) {
                            stickyHeader {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    value = search,
                                    shape = RoundedCornerShape(8.dp),
                                    onValueChange = {
                                        search = it
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = AppIcons.IcSearch,
                                            contentDescription = null
                                        )
                                    },
                                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        if (searchFilteredItems.value.isNotEmpty()) {
                            items(searchFilteredItems.value) { s ->
                                DropdownMenuItem(text = { Text(s.value.second) }, onClick = {
                                    val nextValue = when (s.value.first) {
                                        true -> false
                                        false -> null
                                        null -> true
                                    }
                                    s.value = s.value.copy(first = nextValue)
                                    selectedItems = getSelectedItems()
                                    @Suppress("UNCHECKED_CAST") onItemsSelected.invoke(
                                        selectedItems as List<MutablePair<Boolean, String>>
                                    )
                                }, leadingIcon = {
                                    when (s.value.first) {
                                        true -> {
                                            Icon(
                                                Icons.Default.Check, contentDescription = null
                                            )
                                        }

                                        false -> {
                                            Icon(
                                                Icons.Default.Clear, contentDescription = null
                                            )
                                        }

                                        else -> {}
                                    }
                                })
                            }
                        }


                        if (searchFilteredItems.value.isEmpty()) {
                            item {
                                DropdownMenuItem(text = {
                                    Text(
                                        text = stringResource(id = R.string.no_result)
                                    )
                                }, onClick = {})
                            }
                        }
                    }
                }
            }
        }
    }
}