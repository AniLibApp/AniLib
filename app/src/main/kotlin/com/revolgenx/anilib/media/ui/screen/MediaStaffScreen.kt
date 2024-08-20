package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.staff.ui.component.StaffRowCard
import com.revolgenx.anilib.staff.ui.model.StaffEdgeModel
import kotlinx.coroutines.launch

@Composable
fun MediaStaffScreen(
    viewModel: MediaStaffViewModel
) {
    val snackbar = localSnackbarHostState()
    val scope = rememberCoroutineScope()
    val navigator = LocalMainNavigator.current
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        type = ListPagingListType.GRID,
        gridOptions = GridOptions(GridCells.Adaptive(168.dp)),
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { staffEdgeModel ->
        staffEdgeModel ?: return@LazyPagingList
        MediaStaffItem(
            staffEdgeModel = staffEdgeModel,
            onRoleClick = {
                scope.launch {
                    snackbar.showSnackbar(
                        it,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }) { staffId ->
            navigator.staffScreen(staffId)
        }
    }
}


@Composable
private fun MediaStaffItem(
    staffEdgeModel: StaffEdgeModel,
    onRoleClick: OnClickWithValue<String>,
    onClick: (staffId: Int) -> Unit
) {
    val staff = staffEdgeModel.node ?: return
    val staffRole = staffEdgeModel.role
    StaffRowCard(
        staff = staff,
        role = staffRole,
        onRoleClick = onRoleClick,
        onClick = onClick
    )
}
