package com.revolgenx.anilib.studio.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.studio.ui.viewmodel.StudioViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

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
    val studio = stringResource(id = I18nR.string.studio)
    val studioName = remember { mutableStateOf<String?>(null) }
    val studioSite = remember { mutableStateOf<String?>(null) }
    val navigator = localNavigator()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    ScreenScaffold(
        title = studioName.value ?: studio,
        actions = {
            studioSite.value?.let { site ->
                OverflowMenu {
                    OpenInBrowserOverflowMenu(link = site)
                    ShareOverflowMenu(text = site)
                }
            }
        },
        scrollBehavior = scrollBehavior,
    ) {
        val mediaComponentState = rememberMediaComponentState(navigator = navigator)
        StudioPagingContent(viewModel, studioName, studioSite, mediaComponentState)
    }
}

@Composable
private fun StudioPagingContent(
    viewModel: StudioViewModel,
    studioName: MutableState<String?>,
    studioSite: MutableState<String?>,
    mediaCardState: MediaComponentState
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
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

        if (studioName.value == null && baseModel is MediaModel) {
            baseModel.studio?.let { s ->
                studioName.value = s.name
                studioSite.value = s.siteUrl
            }
        }

        when (baseModel) {
            is HeaderModel -> {
                HeaderBox(header = baseModel)
            }

            is MediaModel -> {
                MediaCard(media = baseModel, mediaComponentState = mediaCardState)
            }
        }
    }
}