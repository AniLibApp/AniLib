package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel

@Composable
fun MediaOverviewScreen(
    viewModel: MediaViewModel,
) {
    ResourceScreen(resourceState = viewModel.resource.value, refresh = { viewModel.refresh() }) {
        MediaOverview(it)
    }
}


@Composable
private fun MediaOverview(media: MediaModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        List(100) { "Hello World!! $it" }.map {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}
