package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ui.screen.ErrorSection
import com.revolgenx.anilib.common.ui.screen.LoadingSection
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaOverviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaOverviewScreen(
    mediaId: Int,
    mediaViewModel: MediaViewModel = koinViewModel(),
    viewModel: MediaOverviewViewModel = koinViewModel()
) {
    LaunchedEffect(mediaId) {
        viewModel.field.mediaId = mediaId
        viewModel.getResource()
    }

    when (val resource = viewModel.resource.value) {
        is ResourceState.Error -> ErrorSection(error = resource.message) {
            viewModel.refresh()
        }

        is ResourceState.Loading -> LoadingSection()

        is ResourceState.Success -> {
            val media = resource.data ?: return
            mediaViewModel.media.value = media
            MediaOverview(media)
        }

        else -> {}
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
