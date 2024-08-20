package com.revolgenx.anilib.common.ui.ads

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel

@Composable
fun ShowAds(viewModel: MainActivityViewModel) {
    val showAdsDialog = viewModel.showAdsDialog

    if (showAdsDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showAdsDialog.value = false
            },
            title = {
                Text(text = "❤\uFE0F Support with Ads")
            },
            text = {
                Text(text = "Play ads for the app development and support \uD83D\uDE4F\uD83D\uDE4F. Thanks!")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAdsDialog.value = false
                    }
                ) {
                    Text("Sure")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAdsDialog.value = false
                    }
                ) {
                    Text("Nope")
                }
            }
        )
    }
}