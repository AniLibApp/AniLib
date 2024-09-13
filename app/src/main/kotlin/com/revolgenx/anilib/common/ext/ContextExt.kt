package com.revolgenx.anilib.common.ext

import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import anilib.i18n.R as I18nR


@Composable
fun localContext() = LocalContext.current

fun Context.openUri(
    url: String?,
    scope: CoroutineScope? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    var errorMsg: String? = null
    try {
        if (!url.isNullOrBlank()) {
            startActivity(Intent(Intent.ACTION_VIEW, url.trim().toUri()).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            errorMsg = this.getString(I18nR.string.invalid_url)
        }
    } catch (e: Exception) {
        Timber.d(e)
        errorMsg = this.getString(I18nR.string.no_app_found_to_handle_url)
    }
    errorMsg?.let {
        scope?.launch {
            snackbarHostState?.showSnackbar(
                message = errorMsg,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }
}

fun Context.shareText(
    text: String?,
    scope: CoroutineScope? = null,
    snackbar: SnackbarHostState? = null
) {
    var errorMsg: String? = null
    try {
        if (!text.isNullOrBlank()) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            val chooser = Intent.createChooser(intent, getString(I18nR.string.share))
            startActivity(chooser)
        } else {
            errorMsg = this.getString(I18nR.string.invalid_url)
        }
    } catch (e: Exception) {
        Timber.d(e)
        errorMsg = this.getString(I18nR.string.no_app_found_to_handle_url)
    }
    errorMsg?.let {
        scope?.launch {
            snackbar?.showSnackbar(
                message = errorMsg,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }
}