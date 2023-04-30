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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.tuples.MutablePair

@Composable
fun MultiSelectDropdownMenu(
    label: String? = null,
    @StringRes labelRes: Int? = null,
    entries: List<MutablePair<Boolean, String>>,
    // pair of index and string
    onItemsSelected: (items: List<Pair<Int, String>>) -> Unit
) {
    val mutableEntries by remember {
        mutableStateOf( entries.map { mutableStateOf(it) })
    }
    val items = mutableEntries.map { it.value.second }
    fun getSelectedItems() = mutableEntries.filter { it.value.first }.map { it.value.second }
    fun getSelectedItemsWithIndex() = getSelectedItems().map { items.indexOf(it) to it }

    var expanded by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(Size.Zero) }
    var selectedItems by remember {
        mutableStateOf(getSelectedItems())
    }


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
                Text(
                    selectedItems.takeIf { it.isNotEmpty() }?.joinToString(", ")
                        ?: stringResource(id = R.string.none)
                )
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
                    items(mutableEntries) { s ->
                        DropdownMenuItem(
                            modifier = Modifier.height(menuHeight),
                            text = { Text(s.value.second) },
                            onClick = {
                                s.value = s.value.copy(first = !s.value.first)
                                selectedItems = getSelectedItems()
                                onItemsSelected.invoke(getSelectedItemsWithIndex())
                            },
                            leadingIcon = {
                                if (s.value.first) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            })
                    }

                }
            }
        }
    }
}