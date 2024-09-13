package com.revolgenx.anilib.setting.ui.screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.m3.HtmlText
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.util.htmlReadyLicenseContent
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcPublic

@Composable
fun AppLibrariesScreen() {
    val openDialog = remember { mutableStateOf<Library?>(null) }
    val context = localContext()

    LibrariesContainer(
        modifier = Modifier.fillMaxSize(),
    ){ library ->
        openDialog.value = library
    }

    val library = openDialog.value
    if (library != null) {
        val license = library.licenses.firstOrNull()
        AlertDialog(
            onDismissRequest = {
                openDialog.value = null
            },
            title = {
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(text = library.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    library.artifactVersion?.let {
                        LightText(text = it)
                    }
                }
            },
            text = license?.htmlReadyLicenseContent?.takeIf { it.isNotBlank() }?.let {
                @Composable{
                    Box(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        HtmlText(html = it)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = null
                    }
                ) {
                    Text(stringResource(id = anilib.i18n.R.string.ok))
                }
            },

            dismissButton = license?.url?.let {url->
                @Composable{
                    IconButton(
                        onClick = {
                            context.openUri(url)
                            openDialog.value = null
                        }
                    ) {
                        Icon(imageVector = AppIcons.IcPublic, contentDescription = null)
                    }
                }
            }
        )
    }
}
