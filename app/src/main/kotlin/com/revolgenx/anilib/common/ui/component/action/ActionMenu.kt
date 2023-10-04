package com.revolgenx.anilib.common.ui.component.action

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBack
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.common.util.OnClick
import anilib.i18n.R as I18nR

const val ActionMenuContainerAlpha = 0.17f

@Composable
fun ActionMenu(
    icon: ImageVector? = null,
    @StringRes contentDescriptionRes: Int? = null,
    tonalButton: Boolean = false,
    onClick: OnClick,
) {
    if (tonalButton) {
        FilledTonalIconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = surfaceContainer.copy(alpha = ActionMenuContainerAlpha)
            )
        ) {
            ActionMenuIcon(
                icon,
                contentDescriptionRes,
            )
        }
    } else {
        IconButton(onClick = onClick) {
            ActionMenuIcon(
                icon,
                contentDescriptionRes,
            )
        }
    }
}


@Composable
private fun ActionMenuIcon(
    imageVector: ImageVector?,
    @StringRes contentDescriptionRes: Int?,
) {
    if (imageVector != null) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescriptionRes?.let { stringResource(id = contentDescriptionRes) }
        )
    }
}

@Composable
fun OverflowMenu(
    icon: ImageVector? = null,
    content: @Composable ColumnScope.(isOpen: MutableState<Boolean>) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded.value = !expanded.value
    }) {
        Icon(
            imageVector = icon ?: AppIcons.IcMoreHoriz,
            contentDescription = stringResource(id = I18nR.string.more),
        )
    }
    DropdownMenu(
        expanded = expanded.value,
        offset = DpOffset(8.dp, 0.dp),
        onDismissRequest = {
            expanded.value = expanded.value.not()
        },
        content = {
            content(expanded)
        }
    )
}


@Composable
fun OverflowMenuItem(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    icon: ImageVector? = null,
    @StringRes contentDescriptionRes: Int? = null,
    isChecked: Boolean = false,
    onClick: OnClick,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
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
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = stringResource(
                                id = (contentDescriptionRes ?: textRes)
                            )
                        )
                    }
                    Text(stringResource(id = textRes))
                }
                if (onCheckedChange != null) {
                    Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
                }
            }
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun NavigationIcon(
    icon: ImageVector? = null,
    tonalButton: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val navigator = LocalMainNavigator.current
    ActionMenu(icon = icon ?: AppIcons.IcBack, tonalButton = tonalButton) {
        onClick?.invoke() ?: navigator.pop()
    }
}