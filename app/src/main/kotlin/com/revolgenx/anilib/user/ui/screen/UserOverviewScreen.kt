package com.revolgenx.anilib.user.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.onMediaClick
import com.revolgenx.anilib.user.ui.model.UserModel

@Composable
fun UserOverviewScreen(resourceState: State<ResourceState<UserModel>?>, refresh: onMediaClick) {
    ResourceScreen(resourceState = resourceState.value, refresh = {
        refresh.invoke()
    }) {

    }
}