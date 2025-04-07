package com.revolgenx.anilib.common.ui.component.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDownward
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUpward
import anilib.i18n.R as I18nR

enum class SortOrder {
    ASC, DESC, NONE
}

data class SortMenuItem(val title: String, var order: SortOrder = SortOrder.NONE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSelectMenu(
    label: String? = null,
    entries: List<SortMenuItem>,
    allowNone: Boolean = true,
    onItemsSelected: (index: Int, item: SortMenuItem?) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember(entries) { mutableStateOf(entries.find { it.order != SortOrder.NONE }) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        val item = selectedItem

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item?.let {
                            val orderIcon = if (item.order == SortOrder.ASC) {
                                AppIcons.IcArrowUpward
                            } else {
                                AppIcons.IcArrowDownward
                            }
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(horizontal = 2.dp),
                                imageVector = orderIcon,
                                contentDescription = item.title
                            )
                        }
                        Text(
                            item?.title ?: stringResource(id = I18nR.string.none),
                            maxLines = 1,
                            fontSize = 14.sp
                        )
                    }
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        }

//        TextField(
//            modifier = modifier
//                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
//                .fillMaxWidth(),
//            readOnly = true,
//            value = item?.title ?: stringResource(id = I18nR.string.none),
//            onValueChange = {},
//            shape = RoundedCornerShape(8.dp),
//            singleLine = singleLine,
//            prefix = item?.let {
//                @Composable {
//                    val orderIcon = if (item.order == SortOrder.ASC) {
//                        AppIcons.IcArrowUpward
//                    } else {
//                        AppIcons.IcArrowDownward
//                    }
//                    Icon(
//                        modifier = Modifier
//                            .size(16.dp)
//                            .padding(horizontal = 2.dp),
//                        imageVector = orderIcon,
//                        contentDescription = item.title
//                    )
//                }
//            },
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
//                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
//            ),
//        )

        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            entries.forEachIndexed { index, s ->
                DropdownMenuItem(
                    text = { Text(s.title) },
                    onClick = {
                        if (selectedItem != s) {
                            selectedItem?.order = SortOrder.NONE
                        }
                        s.order = when (s.order) {
                            SortOrder.ASC -> SortOrder.DESC
                            SortOrder.DESC -> if (allowNone) SortOrder.NONE else SortOrder.ASC
                            SortOrder.NONE -> SortOrder.ASC
                        }
                        selectedItem = null
                        selectedItem = s.takeIf { it.order != SortOrder.NONE }
                        onItemsSelected.invoke(index, selectedItem)
                    },
                    leadingIcon = {
                        if (selectedItem == s) {
                            val orderIcon = if (s.order == SortOrder.ASC) {
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