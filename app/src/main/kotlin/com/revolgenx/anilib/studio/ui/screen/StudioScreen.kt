package com.revolgenx.anilib.studio.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.component.common.Header
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaItemCard
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import org.koin.androidx.compose.koinViewModel

// todo: add filters
class StudioScreen(private val studioId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        StudioScreenContent(studioId)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioScreenContent(studioId: Int, viewModel: StudioViewModel = koinViewModel()) {
    viewModel.field.studioId = studioId
    val studio = stringResource(id = R.string.studio)
    var studioName by remember { mutableStateOf<String?>(null) }
    val navigator = localNavigator()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagingItems = viewModel.collectAsLazyPagingItems()

    ScreenScaffold(
        title = studioName ?: studio,
        actions = {},
        scrollBehavior = scrollBehavior
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyPagingList(
                type = ListPagingListType.GRID,
                pagingItems = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
                span = { index ->
                    val item = pagingItems[index]
                    GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
                },
                gridOptions = GridOptions(GridCells.Adaptive(120.dp))
            ) { baseModel ->
                baseModel ?: return@LazyPagingList

                if (studioName == null && baseModel is MediaModel) {
                    studioName = baseModel.studio?.name
                }

                when (baseModel) {
                    is HeaderModel -> {
                        Header(header = baseModel)
                    }

                    is MediaModel -> {
                        MediaItemCard(media = baseModel) {id, type->
                            navigator.mediaScreen(id, type)
                        }
                    }
                }
            }
        }
    }
}