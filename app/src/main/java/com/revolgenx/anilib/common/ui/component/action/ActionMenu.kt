package com.revolgenx.anilib.common.ui.component.action

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R


@Composable
fun ActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int = items.size,
) {
    val menuItems = remember(
        key1 = items,
        key2 = maxVisibleItems,
    ) {
        splitMenuItems(items, maxVisibleItems)
    }

    menuItems.alwaysShownItems.forEach { item ->
        IconButton(onClick = item.onClick) {
            Icon(
                painter = painterResource(id = item.iconRes!!),
                contentDescription = (item.contentDescriptionRes
                    ?: item.titleRes).let { stringResource(id = it) },
            )
        }
    }

    if (menuItems.overflowItems.isNotEmpty()) {
        IconButton(onClick = onToggleOverflow) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more_horiz),
                contentDescription = "Overflow",
            )
        }
        DropdownMenu(
            expanded = isOpen,
            offset = DpOffset(8.dp, 0.dp),
            onDismissRequest = onToggleOverflow,
        ) {
            menuItems.overflowItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item.iconRes?.let {
                                    Icon(
                                        painterResource(id = it),
                                        contentDescription = (item.contentDescriptionRes
                                            ?: item.titleRes).let {
                                            stringResource(id = it)
                                        })
                                }
                                Text(stringResource(id = item.titleRes))
                            }
                            if (item is ActionMenuItem.NeverShown) {
                                item.isChecked?.let {
                                    Checkbox(checked = it, onCheckedChange = item.onCheckedChange)
                                }
                            }
                        }
                    },
                    onClick = {
                        onToggleOverflow.invoke()
                        item.onClick.invoke()
                    }
                )
            }
        }
    }
}


sealed interface ActionMenuItem {
    val titleRes: Int
    val onClick: () -> Unit
    val iconRes: Int?
    val contentDescriptionRes: Int?

    data class AlwaysShown(
        override val titleRes: Int,
        override val contentDescriptionRes: Int? = null,
        override val onClick: () -> Unit,
        override val iconRes: Int,
    ) : ActionMenuItem

    data class ShownIfRoom(
        override val titleRes: Int,
        override val contentDescriptionRes: Int?,
        override val onClick: () -> Unit,
        override val iconRes: Int? = null,
    ) : ActionMenuItem

    data class NeverShown(
        override val titleRes: Int,
        override val contentDescriptionRes: Int? = null,
        override val onClick: () -> Unit,
        override val iconRes: Int? = null,
        val isChecked: Boolean? = null,
        val onCheckedChange: ((Boolean) -> Unit)? = null
    ) : ActionMenuItem
}


private data class MenuItems(
    val alwaysShownItems: List<ActionMenuItem>,
    val overflowItems: List<ActionMenuItem>,
)

private fun splitMenuItems(
    items: List<ActionMenuItem>,
    maxVisibleItems: Int,
): MenuItems {
    val alwaysShownItems: MutableList<ActionMenuItem> =
        items.filterIsInstance<ActionMenuItem.AlwaysShown>().toMutableList()
    val ifRoomItems: MutableList<ActionMenuItem> =
        items.filterIsInstance<ActionMenuItem.ShownIfRoom>().toMutableList()
    val overflowItems = items.filterIsInstance<ActionMenuItem.NeverShown>()

    val hasOverflow = overflowItems.isNotEmpty() ||
            (alwaysShownItems.size + ifRoomItems.size - 1) > maxVisibleItems
    val usedSlots = alwaysShownItems.size + (if (hasOverflow) 1 else 0)
    val availableSlots = maxVisibleItems - usedSlots
    if (availableSlots > 0 && ifRoomItems.isNotEmpty()) {
        val visible = ifRoomItems.subList(0, availableSlots.coerceAtMost(ifRoomItems.size))
        alwaysShownItems.addAll(visible)
        ifRoomItems.removeAll(visible)
    }

    return MenuItems(
        alwaysShownItems = alwaysShownItems,
        overflowItems = ifRoomItems + overflowItems,
    )
}
