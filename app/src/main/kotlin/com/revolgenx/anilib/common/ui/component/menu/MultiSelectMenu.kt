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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.model.BaseModel
import anilib.i18n.R as I18nR


data class MultiSelectModel<T>(
    var selected: MutableState<Boolean>,
    val data: T
) : BaseModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MultiSelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    text: (data: T) -> String = { it as String },
    entries: List<MultiSelectModel<T>>,
    onItemsSelected: (items: List<T>) -> Unit
) {
    fun getSelectedItems() = entries.filter { it.selected.value }.map { it.data }

    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember(entries) {
        mutableStateOf(getSelectedItems())
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        val selectedItem = selectedItems.takeIf { it.isNotEmpty() }?.joinToString(", ") {
            text(it)
        } ?: stringResource(id = I18nR.string.none)
        TextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedItem,
            onValueChange = {},
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
            onDismissRequest = { expanded = false }
        ) {

            entries.forEach { s ->
                DropdownMenuItem(
                    text = { Text(text(s.data)) },
                    onClick = {
                        s.selected.value = !s.selected.value
                        selectedItems = getSelectedItems()
                        onItemsSelected.invoke(selectedItems)
                    },
                    leadingIcon = {
                        if (s.selected.value) {
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
