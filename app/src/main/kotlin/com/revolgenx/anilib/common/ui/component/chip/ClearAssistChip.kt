package com.revolgenx.anilib.common.ui.component.chip

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.util.OnClick

@Composable
fun ClearAssistChip(
    modifier: Modifier = Modifier,
    text: String,
    onClear: OnClick,
    onClick: OnClick
) {
    AssistChip(
        modifier = modifier,
        onClick = onClick,
        label = { Text(text = text) },
        colors = AssistChipDefaults.assistChipColors(leadingIconContentColor = MaterialTheme.colorScheme.onSurface),
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onClear),
                imageVector = AppIcons.IcCancel,
                contentDescription = stringResource(id = R.string.clear)
            )
        })
}