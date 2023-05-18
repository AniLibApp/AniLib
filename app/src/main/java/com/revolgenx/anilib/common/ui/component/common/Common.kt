package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.revolgenx.anilib.common.data.store.mediaCoverImageType
import com.revolgenx.anilib.common.data.store.mediaTitleType
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localUser
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType

@Composable
fun MediaTitleType(content: @Composable (type: MediaTitleType) -> Unit) =
    localContext().mediaTitleType().collectAsState(initial = null).value?.let {
        content(it)
    }

@Composable
fun MediaCoverImageType(content: @Composable (type: MediaCoverImageType) -> Unit) =
    localContext().mediaCoverImageType().collectAsState(initial = null).value?.let {
        content(it)
    }

@Composable
fun ShowIfLoggedIn(
    orElse: @Composable (() -> Unit) = {},
    content: @Composable (userId: Int) -> Unit
) {
    val userId = localUser().userId
    if (userId != null) {
        content(userId)
    } else {
        orElse()
    }
}

@Composable
fun ShowIfAuthUser(userId: Int, content: @Composable () -> Unit) {
    val id = localUser().userId
    if (id != null && id == userId) {
        content()
    }
}


@Composable
fun isLoggedIn() = localUser().isLoggedIn