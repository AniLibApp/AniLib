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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.data.tuples.MutablePair
import anilib.i18n.R as I18nR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    entries: List<MutablePair<Boolean, String>>,
    onItemsSelected: (items: List<Pair<Int, String>>) -> Unit
) {
    val mutableEntries by remember {
        mutableStateOf(entries.map { mutableStateOf(it) })
    }

    fun getSelectedItems() = mutableEntries.filter { it.value.first }
        .mapIndexed { index, mutableState -> index to mutableState.value.second }

    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember {
        mutableStateOf(getSelectedItems())
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        val selectedItem = selectedItems.takeIf { it.isNotEmpty() }?.joinToString(", ") {
            it.second
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

            mutableEntries.forEach { s ->
                DropdownMenuItem(
                    text = { Text(s.value.second) },
                    onClick = {
                        s.value = s.value.copy(first = !s.value.first)
                        selectedItems = getSelectedItems()
                        onItemsSelected.invoke(selectedItems)
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
