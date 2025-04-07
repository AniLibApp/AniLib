package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        Column {
            label?.let {
                SelectMenuLabel(label = label)
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedItem,
                        maxLines = 1,
                        fontSize = 14.sp
                    )
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        }

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
