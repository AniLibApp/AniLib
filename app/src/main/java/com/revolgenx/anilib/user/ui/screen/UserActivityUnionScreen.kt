package com.revolgenx.anilib.user.ui.screen

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreenContent
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserActivityUnionScreen(userId: Int) {
    val viewModel = koinViewModel<ActivityUnionViewModel>()
    viewModel.field.userId = userId
    ActivityUnionScreenContent(viewModel = viewModel)
}