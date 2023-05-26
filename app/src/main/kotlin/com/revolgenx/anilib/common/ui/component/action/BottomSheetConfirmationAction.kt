package com.revolgenx.anilib.common.ui.component.action

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.revolgenx.anilib.R


@Composable
fun BottomSheetConfirmationAction(
    @StringRes positiveTextRes: Int = R.string.filter,
    @StringRes negativeTextRes: Int = R.string.cancel,
    onPositiveClicked: (() -> Unit)?,
    onNegativeClicked: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onNegativeClicked ?: {}) {
            Text(stringResource(id = negativeTextRes))
        }
        Button(onClick = onPositiveClicked ?: {}) {
            Text(stringResource(id = positiveTextRes))
        }
    }
}