package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.staff.ui.component.StaffCard
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaStaffScreen(
    mediaId: Int,
    viewModel: MediaStaffViewModel = koinViewModel()
) {
    val navigator = LocalMainNavigator.current
    LaunchedEffect(mediaId) {
        viewModel.field.mediaId = mediaId
    }
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        type = ListPagingListType.GRID,
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { staffEdgeModel ->
        staffEdgeModel ?: return@LazyPagingList
        MediaStaffItem(staffEdgeModel) { staffId ->
            navigator.staffScreen(staffId)
        }
    }
}


@Composable
private fun MediaStaffItem(
    staffEdgeModel: StaffEdgeModel,
    onClick: (staffId: Int) -> Unit
) {
    val staff = staffEdgeModel.node ?: return
    val staffRole = staffEdgeModel.role
    StaffCard(staff = staff, role = staffRole, onClick = onClick)
}


@Preview
@Composable
fun MediaStaffItemPreview() {
    MediaStaffItem(
        StaffEdgeModel(
            node = StaffModel(
                name = StaffNameModel("Aya Takaha Aya Takaha Aya Takaha")
            ),
            role = "Original Creator"
        )
    ) {}
}