package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R as I18nR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    showNoneItem: Boolean = false,
    singleLine: Boolean = true,
    noneItemText: Int? = null,
    entries: Array<String>,
    selectedItemPosition: Int? = null,
    onItemSelected: (itemPosition: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember(selectedItemPosition) { mutableIntStateOf(selectedItemPosition ?: -1) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        val selectedItem = if (selectedIndex > -1) {
            entries[selectedIndex]
        } else {
            stringResource(id = I18nR.string.none)
        }
        TextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedItem,
            onValueChange = {},
            singleLine = singleLine,
            shape = RoundedCornerShape(8.dp),
            label = label?.let { { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        )

        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            if (showNoneItem) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = noneItemText ?: I18nR.string.none)) },
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