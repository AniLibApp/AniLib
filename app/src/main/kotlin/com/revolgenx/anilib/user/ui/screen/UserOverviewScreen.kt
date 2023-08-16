package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.user.ui.viewmodel.UserViewModel

@Composable
fun UserOverviewScreen(viewModel: UserViewModel) {
    ResourceScreen(viewModel = viewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            (1..50).forEach {
                Text(modifier = Modifier.fillMaxWidth(), text = "$it")
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}