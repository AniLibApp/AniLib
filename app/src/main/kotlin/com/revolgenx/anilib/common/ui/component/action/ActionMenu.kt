package com.revolgenx.anilib.common.ui.component.action

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.util.OnClick


@Composable
fun ActionMenu(
    @DrawableRes iconRes: Int,
    @StringRes contentDescriptionRes: Int? = null,
    onClick: OnClick,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescriptionRes?.let { stringResource(id = contentDescriptionRes) }
        )
    }
}

@Composable
fun OverflowMenu(
    @DrawableRes overflowIconRes: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    IconButton(onClick = {
        isOpen = isOpen.not()
    }) {
        Icon(
            painter = painterResource(id = overflowIconRes ?: R.drawable.ic_more_horiz),
            contentDescription = stringResource(id = R.string.more),
        )
    }
    DropdownMenu(
        expanded = isOpen,
        offset = DpOffset(8.dp, 0.dp),
        onDismissRequest = {
            isOpen = isOpen.not()
        },
        content = content
    )
}


@Composable
fun OverflowMenuItem(
    @StringRes textRes: Int,
    @DrawableRes iconRes: Int? = null,
    @StringRes contentDescriptionRes: Int? = null,
    onClick: OnClick,
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
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
                    iconRes?.let {
                        Icon(
                            painterResource(id = it),
                            contentDescription = (contentDescriptionRes ?: textRes).let {
                                stringResource(id = it)
                            })
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