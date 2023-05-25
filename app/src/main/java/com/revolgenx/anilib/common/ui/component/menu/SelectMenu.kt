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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.revolgenx.anilib.R


@Composable
fun SelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    @StringRes labelRes: Int? = null,
    showNoneItem: Boolean = false,
    entries: List<String>,
    selectedItemPosition: Int? = null,
    onItemSelected: (itemPosition: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(Size.Zero) }
    var selectedIndex by remember { mutableStateOf(selectedItemPosition ?: -1) }

    Column {
        if (label != null || labelRes != null) {
            Text(
                modifier = Modifier.padding(PaddingValues(vertical = 8.dp)),
                text = label ?: stringResource(id = labelRes!!)
            )
        }

        Card(
            modifier = modifier
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
                val selectedItem = if (selectedIndex > -1) {
                    entries[selectedIndex]
                } else {
                    stringResource(id = R.string.none)
                }
                Text(
                    text = selectedItem,
                    letterSpacing = 0.3.sp
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
            onDismissRequest = { expanded = false },
        ) {
            Box(modifier = Modifier.size(width = menuWidth, height = menuItemHeight)) {
                LazyColumn {
                    if (showNoneItem) {
                        item {
                            DropdownMenuItem(
                                modifier = Modifier.height(menuHeight),
                                text = { Text(stringResource(id = R.string.none)) },
                                onClick = {
                                    expanded = false
                                    selectedIndex = -1
                                    onItemSelected.invoke(-1)
                                },
                                leadingIcon = {
                                    if (selectedIndex == -1) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null
                                        )
                                    }
                                })
                        }
                    }

                    itemsIndexed(entries) { index, s ->
                        DropdownMenuItem(
                            modifier = Modifier.height(menuHeight),
                            text = { Text(s) },
                            onClick = {
                                expanded = false
                                selectedIndex = index
                                onItemSelected.invoke(index)
                            },
                            leadingIcon = {
                                if (index == selectedIndex) {
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


@Preview
@Composable
fun SelectableDropDownMenuPreview() {
    SelectMenu(
        entries = listOf("Winter", "Summer"),
        selectedItemPosition = 0,
        onItemSelected = {

        })
}