package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.revolgenx.anilib.app.preference.mediaTitleType
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.media.ui.model.MediaTitleType

@Composable
fun MediaTitleType(content: @Composable (type: MediaTitleType) -> Unit) =
    localContext().mediaTitleType().collectAsState(initial = null).value?.let {
        content(it)
    }