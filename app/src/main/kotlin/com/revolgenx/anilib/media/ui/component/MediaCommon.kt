package com.revolgenx.anilib.media.ui.component

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.composition.localMediaState

@Composable
fun MediaTitleType(content: @Composable (type: Int) -> Unit) {
    content(localMediaState().titleType)
}

@Composable
fun MediaCoverImageType(content: @Composable (type: Int) -> Unit) {
    content(localMediaState().coverImageType)
}