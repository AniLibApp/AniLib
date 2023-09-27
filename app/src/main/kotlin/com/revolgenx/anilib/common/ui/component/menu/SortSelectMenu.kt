package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDownward
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUpward
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import anilib.i18n.R as I18nR


enum class AlSortOrder {
    ASC, DESC, NONE
}

data class AlSortMenuItem(val title: String, var order: AlSortOrder = AlSortOrder.NONE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSelectMenu(
    modifier: Modifier = Modifier,
    label: String? = null,
    entries: List<AlSortMenuItem>,
    allowNone: Boolean = true,
    onItemsSelected: (index: Int, item: AlSortMenuItem?) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(entries.find { it.order != AlSortOrder.NONE }) }

    val shape = if (expanded)
        RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    else RoundedCornerShape(8.dp)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        val item = selectedItem
        if (item != null) {
            val orderIcon = if (item.order == AlSortOrder.ASC) {
                AppIcons.IcArrowUpward
            } else {
                AppIcons.IcArrowDownward
            }
            Icon(
                imageVector = orderIcon,
                contentDescription = item.title
            )
        }

        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = item?.title ?: stringResource(id = I18nR.string.none),
            onValueChange = {},
            shape = shape,
            label = { label?.let { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = onSurfaceVariant
            ),
        )


        val menuHeight = 40.dp
        val entriesSize = entries.size
        val menuItemHeight = menuHeight * if (entriesSize > 10) 10 else entriesSize

        DropdownMenu(
            modifier = modifier
                .exposedDropdownSize()
                .height(height = menuItemHeight),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            entries.forEachIndexed { index, s ->
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
                                AppIcons.IcArrowUpward
                            } else {
                                AppIcons.IcArrowDownward
                            }
                            Icon(
                                imageVector = orderIcon,
                                contentDescription = s.title
                            )
                        }
                    })
            }

        }
    }
}