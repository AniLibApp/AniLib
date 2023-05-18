package com.revolgenx.anilib.list.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.screen.ResourceScreen
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel

@Composable
fun MediaListContent(viewModel: MediaListViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    val loading = remember { mutableStateOf(false) }
    ResourceScreen(
        resourceState = viewModel.resource.value,
        loading = loading,
        refresh = { viewModel.refresh() }) {

        LazyPagingList(
            items = viewModel.mediaListCollection,
            onRefresh = {
                viewModel.refresh()
            }
        ) { mediaList ->
            mediaList ?: return@LazyPagingList
            MediaListItem(list = mediaList)
        }
    }
}

@Composable
private fun MediaListItem(list: MediaListModel) {
    val media = list.media ?: return
    Box(modifier = Modifier.padding(16.dp)){

        Text(text = media.title?.userPreferred.naText())
    }
}