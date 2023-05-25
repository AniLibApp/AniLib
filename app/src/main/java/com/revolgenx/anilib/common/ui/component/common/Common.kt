package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.composition.localMediaState
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.media.ui.model.MediaCoverImageType
import com.revolgenx.anilib.media.ui.model.MediaTitleType

@Composable
fun MediaTitleType(content: @Composable (type: MediaTitleType) -> Unit) {
    content(localMediaState().titleType)
}

@Composable
fun MediaCoverImageType(content: @Composable (type: MediaCoverImageType) -> Unit) {
    content(localMediaState().coverImageType)
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
