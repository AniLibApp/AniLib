package com.revolgenx.anilib.common.ui.component.dropdown

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.revolgenx.anilib.R


@Composable
fun SelectableDropDownMenu(
    label: String? = null,
    @StringRes labelRes: Int? = null,
    showNoneItem: Boolean = false,
    items: List<String>,
    selectedItemPosition: Int,
    onItemSelected: (itemPosition: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(Size.Zero) }


    Column() {
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
                val selectedItem = if (selectedItemPosition > -1) {
                    items[selectedItemPosition]
                } else {
                    stringResource(id = R.string.none)
                }
                Text(
                    selectedItem
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            modifier = Modifier
                .requiredSizeIn(maxHeight = 400.dp)
                .width(with(LocalDensity.current) { menuSize.width.toDp() }),
            expanded = expanded, onDismissRequest = { expanded = false }
        ) {
            if (showNoneItem) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.none)) },
                    onClick = {
                        onItemSelected.invoke(-1)
                    },
                    leadingIcon = {
                        if (selectedItemPosition == -1) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    })
            }
            items.mapIndexed { index, s ->
                DropdownMenuItem(
                    text = { Text(s) },
                    onClick = {
                        expanded = false
                        onItemSelected.invoke(index)
                    },
                    leadingIcon = {
                        if (index == selectedItemPosition) {
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


@Preview
@Composable
fun SelectableDropDownMenuPreview() {
    SelectableDropDownMenu(
        items = listOf("Winter", "Summer"),
        selectedItemPosition = 0,
        onItemSelected = {

        })
}