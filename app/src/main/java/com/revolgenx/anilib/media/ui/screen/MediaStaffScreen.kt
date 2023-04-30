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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.revolgenx.anilib.staff.ui.screen.StaffScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
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
        items = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { staffEdgeModel ->
        staffEdgeModel ?: return@LazyPagingList
        MediaStaffItem(staffEdgeModel) { staffId ->
            navigator.push(StaffScreen(staffId))
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
    Card(
        modifier = Modifier
            .size(height = 236.dp, width = 120.dp)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick.invoke(staff.id)
            }) {
            FrescoImage(
                modifier = Modifier
                    .height(165.dp)
                    .fillMaxWidth(),
                imageUrl = staff.image?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 2.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    staff.name?.full.naText(),
                    maxLines = 2,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
                staffRole?.let {
                    Text(
                        it,
                        maxLines = 1,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
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
    ){}
}