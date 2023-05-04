package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.revolgenx.anilib.common.data.store.appPreferencesDataStore
import com.revolgenx.anilib.common.data.store.isLoggedIn
import com.revolgenx.anilib.common.data.store.mediaCoverImageType
import com.revolgenx.anilib.common.data.store.mediaTitleType
import com.revolgenx.anilib.common.data.store.userId
import com.revolgenx.anilib.common.ext.localContext
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
    val userId = localContext().userId().collectAsState(initial = null).value
    if (userId != null) {
        content(userId)
    } else {
        orElse()
    }
}

@Composable
fun ShowIfLoggedInUser(userId: Int, content: @Composable () -> Unit) {
    val id = localContext().userId().collectAsState(initial = null).value
    if (id != null && id == userId) {
        content()
    }
}


@Composable
fun isLoggedIn() =
    localContext().appPreferencesDataStore.isLoggedIn().collectAsState(initial = false)