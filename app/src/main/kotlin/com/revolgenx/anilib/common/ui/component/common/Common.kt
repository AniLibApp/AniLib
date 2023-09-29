package com.revolgenx.anilib.common.ui.component.common

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.composition.localUser

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
fun ShowIfNotLoggedIn(
    content: @Composable () -> Unit
) {
    val userId = localUser().userId
    if (userId == null) {
        content()
    }
}


@Composable
fun IsLoggedIn(content: @Composable (loggedIn: Boolean, userId: Int?) -> Unit) {
    val id = localUser().userId
    content(id != null, id)
}