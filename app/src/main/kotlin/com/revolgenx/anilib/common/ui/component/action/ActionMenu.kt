package com.revolgenx.anilib.common.ui.component.action

import IcOpenInNew
import IcShare
import android.content.Context
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.shareText
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBack
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.util.OnClick
import kotlinx.coroutines.CoroutineScope
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
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = ActionMenuContainerAlpha)
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
    tonalButton: Boolean = false,
    content: @Composable ColumnScope.(isOpen: MutableState<Boolean>) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    ActionMenu(
        icon = icon ?: AppIcons.IcMoreHoriz,
        contentDescriptionRes = I18nR.string.more,
        tonalButton = tonalButton
    ) {
        expanded.value = !expanded.value
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
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: OnClick,
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

@Composable
fun OpenInBrowserOverflowMenu(
    link: String,
    context: Context,
    scope: CoroutineScope? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    OverflowMenuItem(
        textRes = I18nR.string.open_in_browser,
        icon = AppIcons.IcOpenInNew,
        contentDescriptionRes = null,
    ) {
        context.openLink(link, scope, snackbarHostState)
    }
}

@Composable
fun ShareOverflowMenu(
    text: String,
    context: Context,
    scope: CoroutineScope? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    OverflowMenuItem(
        textRes = I18nR.string.share,
        icon = AppIcons.IcShare,
        contentDescriptionRes = null,
    ) {
        context.shareText(text, scope, snackbarHostState)
    }
}