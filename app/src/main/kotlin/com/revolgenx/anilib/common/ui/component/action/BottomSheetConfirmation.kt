package com.revolgenx.anilib.common.ui.component.action

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.util.OnClick
import anilib.i18n.R as I18nR


@Composable
fun BottomSheetConfirmation(
    @StringRes positiveTextRes: Int = I18nR.string.filter,
    @StringRes negativeTextRes: Int = I18nR.string.cancel,
    confirmEnabled: Boolean = true,
    onDismiss: OnClick,
    onConfirm: OnClick
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onDismiss) {
            Text(stringResource(id = negativeTextRes))
        }
        Button(
            onClick = onConfirm,
            enabled = confirmEnabled
        ) {
            Text(stringResource(id = positiveTextRes))
        }
    }
}


