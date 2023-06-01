package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.component.action.DisappearingFloatingButton
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.common.Header
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.component.MediaItemRowContent
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffMediaRoleScreen(viewModel: StaffMediaRoleViewModel) {
    val navigator = localNavigator()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFloatingButton(scrollState = scrollState, iconRes = R.drawable.ic_filter) {
                //todo filter
            }
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomScrollConnection)
        ) {
            LazyPagingList(
                type = ListPagingListType.GRID,
                gridOptions = GridOptions(GridCells.Adaptive(168.dp)),
                pagingItems = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
                span = { index ->
                    val item = pagingItems[index]
                    GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
                }
            ) { model ->
                when (model) {
                    is HeaderModel -> {
                        Header(header = model)
                    }

                    is MediaModel -> {
                        StaffMediaRoleItem(model = model) {
                            navigator.mediaScreen(model.id)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun StaffMediaRoleItem(model: MediaModel, onClick: OnClick) {
    Card(
        modifier = Modifier
            .width(168.dp)
            .height(124.dp)
            .padding(6.dp)
    ) {
        MediaItemRowContent(
            media = model,
            content = {
                Box(modifier = Modifier.fillMaxHeight()) {
                    model.staffRole?.let {
                        LightText(
                            modifier = Modifier.align(Alignment.BottomStart),
                            text = it,
                            maxLines = 2
                        )
                    }
                }
            },
            onClick = onClick
        )
    }
}
