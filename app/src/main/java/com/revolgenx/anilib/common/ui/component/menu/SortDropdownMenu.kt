package com.revolgenx.anilib.common.ui.component.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.revolgenx.anilib.R


enum class AlSortOrder {
    ASC, DESC, NONE
}

data class AlSortMenuItem(val title: String, var order: AlSortOrder = AlSortOrder.NONE)

@Composable
fun SortDropdownMenu(
    label: String? = null,
    @StringRes labelRes: Int? = null,
    entries: List<AlSortMenuItem>,
    allowNone: Boolean = true,
    onItemsSelected: (index: Int, item: AlSortMenuItem?) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(Size.Zero) }
    var selectedItem by remember { mutableStateOf(entries.find { it.order != AlSortOrder.NONE }) }

    Column {
        if (label != null || labelRes != null) {
            Text(
                label ?: stringResource(id = labelRes!!),
                modifier = Modifier.padding(PaddingValues(vertical = 8.dp))
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                }
                .onGloballyPositioned { coordinates ->
                    menuSize = coordinates.size.toSize()
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(horizontal = 8.dp, vertical = 12.dp)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row() {
                    val item = selectedItem
                    if (item != null) {
                        val orderIcon = if (item.order == AlSortOrder.ASC) {
                            R.drawable.round_arrow_upward
                        } else {
                            R.drawable.round_arrow_downward
                        }
                        Icon(
                            painter = painterResource(id = orderIcon),
                            contentDescription = item.title
                        )
                    }

                    Text(
                        item?.title ?: stringResource(id = R.string.none)
                    )
                }
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }


        val menuWidth = with(LocalDensity.current) { menuSize.width.toDp() }
        val menuHeight = 40.dp
        val entriesSize = entries.size
        val menuItemHeight = menuHeight * if (entriesSize > 10) 10 else entriesSize

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Box(modifier = Modifier.size(width = menuWidth, height = menuItemHeight)) {
                LazyColumn {
                    itemsIndexed(entries) { index, s ->
                        DropdownMenuItem(
                            modifier = Modifier.height(menuHeight),
                            text = { Text(s.title) },
                            onClick = {
                                if (selectedItem != s) {
                                    selectedItem?.order = AlSortOrder.NONE
                                }
                                s.order = when (s.order) {
                                    AlSortOrder.ASC -> AlSortOrder.DESC
                                    AlSortOrder.DESC -> if (allowNone) AlSortOrder.NONE else AlSortOrder.ASC
                                    AlSortOrder.NONE -> AlSortOrder.ASC
                                }
                                selectedItem = null
                                selectedItem = s.takeIf { it.order != AlSortOrder.NONE }
                                onItemsSelected.invoke(index, selectedItem)
                            },
                            leadingIcon = {
                                if (selectedItem == s) {
                                    val orderIcon = if (s.order == AlSortOrder.ASC) {
                                        R.drawable.round_arrow_upward
                                    } else {
                                        R.drawable.round_arrow_downward
                                    }
                                    Icon(
                                        painter = painterResource(id = orderIcon),
                                        contentDescription = s.title
                                    )
                                }
                            })
                    }

                }
            }
        }
    }
}