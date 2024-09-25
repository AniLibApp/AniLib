package com.revolgenx.anilib.common.ui.component.action

import IcOpenInNew
import IcShare
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ext.shareText
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBack
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.util.OnClick
import anilib.i18n.R as I18nR

const val ActionMenuContainerAlpha = 0.17f

@Composable
fun ActionMenu(
    icon: ImageVector? = null,
    @StringRes contentDescriptionRes: Int? = null,
    tonalButton: Boolean = false,
    enabled: Boolean = true,
    onClick: OnClick
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
        IconButton(onClick = onClick, enabled = enabled) {
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

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
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

}

@Composable
fun OverflowRadioMenuItem(text: String, selected: Boolean = false, onClick: OnClick) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 16.dp)
        )

        RadioButton(
            selected = selected,
            onClick = null // null recommended for accessibility with screenreaders
        )
    }
}


@Composable
fun OverflowMenuItem(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    icon: ImageVector? = null,
    iconSpace: Boolean = false,
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
                    } ?: let{
                        if(iconSpace){
                            Spacer(modifier = Modifier.size(24.dp))
                        }
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
fun OpenInBrowserOverflowMenu(link: String, onClick: OnClick) {
    val context = localContext()
    val snackbarHostState = localSnackbarHostState()
    val scope = rememberCoroutineScope()
    OverflowMenuItem(
        textRes = I18nR.string.open_in_browser,
        icon = AppIcons.IcOpenInNew,
        contentDescriptionRes = null,
    ) {
        onClick()
        context.openUri(link, scope, snackbarHostState)
    }
}

@Composable
fun ShareOverflowMenu(text: String, onClick: OnClick) {
    val context = localContext()
    val snackbarHostState = localSnackbarHostState()
    val scope = rememberCoroutineScope()
    OverflowMenuItem(
        textRes = I18nR.string.share,
        icon = AppIcons.IcShare,
        contentDescriptionRes = null,
    ) {
        context.shareText(text, scope, snackbarHostState)
    }
}

@Preview(showBackground = true)
@Composable
private fun OverflowRadioMenuItemPreview() {
    OverflowRadioMenuItem(text = "hello") {

    }
}