package com.revolgenx.anilib.common.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.util.OnClick
import anilib.i18n.R as I18nR

@Composable
fun ConfirmationDialog(
    openDialog: MutableState<Boolean>,
    title: String = stringResource(id = I18nR.string.confirm),
    message: String? = null,
    text: @Composable (() -> Unit)? = null,
    positiveText: String = stringResource(id = I18nR.string.ok),
    negativeText: String = stringResource(id = I18nR.string.cancel),
    onDismiss: OnClick? = null,
    onConfirm: OnClick
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss?.invoke()
            },
            title = {
                Text(text = title)
            },
            text = text ?: {
                Text(text = message.orEmpty())
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        openDialog.value = false
                    }
                ) {
                    Text(positiveText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDismiss?.invoke()
                    }
                ) {
                    Text(negativeText)
                }
            }
        )
    }
}