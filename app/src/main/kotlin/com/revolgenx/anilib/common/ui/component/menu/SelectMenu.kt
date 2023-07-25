package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    showNoneItem: Boolean = false,
    entries: List<String>,
    selectedItemPosition: Int? = null,
    onItemSelected: (itemPosition: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(selectedItemPosition ?: -1) }


    val shape = if (expanded)
        RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    else RoundedCornerShape(8.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        val selectedItem = if (selectedIndex > -1) {
            entries[selectedIndex]
        } else {
            stringResource(id = R.string.none)
        }
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedItem,
            onValueChange = {},
            shape = shape,
            label = label?.let { { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = onSurfaceVariant
            ),
        )

        val menuHeight = 40.dp
        val entriesSize = entries.size
        val menuItemHeight = menuHeight.plus(4.dp) * if (entriesSize > 10) 10 else entriesSize


        DropdownMenu(
            modifier = modifier
                .exposedDropdownSize()
                .height(height = menuItemHeight),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            if (showNoneItem) {
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

            entries.forEachIndexed { index, s ->
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
                    }
                )
            }

        }
    }
}