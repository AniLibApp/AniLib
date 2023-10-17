package com.revolgenx.anilib.common.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.util.OnClick
import anilib.i18n.R as I18nR

@Composable
fun ConfirmationDialog(
    openDialog: MutableState<Boolean>,
    title: String? = null,
    message: String,
    confirm: OnClick
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = title ?: stringResource(id = I18nR.string.confirm))
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirm()
                        openDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}