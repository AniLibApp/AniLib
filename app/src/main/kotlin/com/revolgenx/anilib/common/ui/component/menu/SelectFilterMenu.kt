package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import anilib.i18n.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.theme.excluded_color

enum class SelectType {
    INCLUDED,
    EXCLUDED,
    NONE
}

data class SelectFilterModel<T>(
    var selected: MutableState<SelectType>,
    val data: T
) : BaseModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun <T> SelectFilterMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    text: (data: T) -> String = { it as String },
    entries: List<SelectFilterModel<T>>,
    onItemsSelected: (items: List<SelectFilterModel<T>>) -> Unit
) {

    fun getSelectedItems() = entries.filter { it.selected.value != SelectType.NONE }

    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember(entries) {
        mutableStateOf(getSelectedItems())
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { },
    ) {
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
                        .padding(8.dp)
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                        .clickable(indication = null, interactionSource = remember {
                            MutableInteractionSource()
                        }) {
                            expanded = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedItems.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = stringResource(id = R.string.none)
                        )
                    } else {
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
                                    label = { Text(text = text(it.data)) },
                                    border = AssistChipDefaults.assistChipBorder(
                                        true,
                                        borderColor = if (it.selected.value == SelectType.EXCLUDED) excluded_color else MaterialTheme.colorScheme.outline
                                    ),
                                    colors = AssistChipDefaults.assistChipColors(
                                        leadingIconContentColor = if (it.selected.value == SelectType.EXCLUDED) excluded_color else MaterialTheme.colorScheme.onSurface
                                    ),
                                    trailingIcon = {
                                        Box(modifier = Modifier.clickable(indication = null,
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }) {
                                            it.selected.value = SelectType.NONE
                                            selectedItems = getSelectedItems()
                                            onItemsSelected.invoke(
                                                selectedItems
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

                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }


                DropdownMenu(modifier = modifier.exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {

                    entries.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(text(s.data)) }, onClick = {
                                val nextValue = when (s.selected.value) {
                                    SelectType.INCLUDED -> SelectType.EXCLUDED
                                    SelectType.EXCLUDED -> SelectType.NONE
                                    SelectType.NONE -> SelectType.INCLUDED
                                }
                                s.selected.value = nextValue
                                selectedItems = getSelectedItems()
                                onItemsSelected.invoke(selectedItems)
                            },
                            leadingIcon = {
                                when (val included = s.selected.value) {
                                    SelectType.NONE -> {}
                                    else -> {
                                        Icon(
                                            if (included == SelectType.INCLUDED) Icons.Default.Check else Icons.Default.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            })
                    }

                }
            }
        }
    }
}