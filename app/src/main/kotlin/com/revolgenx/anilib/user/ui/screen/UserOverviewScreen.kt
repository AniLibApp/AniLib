package com.revolgenx.anilib.user.ui.screen

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel

@Composable
fun UserOverviewScreen(viewModel: UserViewModel) {
    ResourceScreen(resourceState = viewModel.resource.value, refresh = { viewModel.refresh() }) {

    }
}