package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
import com.revolgenx.anilib.common.data.constant.ExploreSectionOrder
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.browseScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.button.RefreshButton
import com.revolgenx.anilib.common.ui.component.common.HeaderText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreAiringScheduleViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreWatchingViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreMediaViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreNewlyAddedFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExplorePopularFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreReadingViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreTrendingFilterViewModel
import com.revolgenx.anilib.home.explore.ui.viewmodel.ExploreViewModel
import com.revolgenx.anilib.media.ui.filter.MediaFilterBottomSheet
import com.revolgenx.anilib.media.ui.viewmodel.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.type.MediaSort
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen() {
    val navigator = localNavigator()
    val bottomSheetState = rememberBottomSheetState()
    val scope = rememberCoroutineScope()

    val viewModel: ExploreViewModel = koinViewModel()
    val localUser = localUser()

    val exploreTrendingFilterViewModel: ExploreTrendingFilterViewModel = koinViewModel()
    val explorePopularFilterViewModel: ExplorePopularFilterViewModel = koinViewModel()
    val exploreNewlyAddedFilterViewModel: ExploreNewlyAddedFilterViewModel = koinViewModel()

    val exploreAiringScheduleViewModel: ExploreAiringScheduleViewModel = koinViewModel()
    val exploreTrendingViewModel: ExploreMediaViewModel.ExploreTrendingViewModel = koinViewModel()
    val explorePopularViewModel: ExploreMediaViewModel.ExplorePopularViewModel = koinViewModel()
    val exploreNewlyAddedViewModel: ExploreMediaViewModel.ExploreNewlyAddedViewModel =
        koinViewModel()
    val exploreWatchingViewModel: ExploreWatchingViewModel = koinViewModel()
    val exploreReadingViewModel: ExploreReadingViewModel = koinViewModel()

    val scrollViewModel: ScrollViewModel = activityViewModel()

    localUser.userId?.let {
        exploreReadingViewModel.field.userId = it
        exploreWatchingViewModel.field.userId = it
    }

    val filterViewModel: MutableState<MediaFilterBottomSheetViewModel> = remember {
        mutableStateOf(exploreTrendingFilterViewModel)
    }

    var isRefreshing by remember {
        mutableStateOf(false)
    }

    if (isRefreshing) {
        SideEffect {
            isRefreshing = false
        }
    }

    val refresh = {
        viewModel.exploreSectionContentOrderData.forEach {
            if (!it.isEnabled) return@forEach
            when (it.value) {
                ExploreSectionOrder.AIRING -> {
                    exploreAiringScheduleViewModel.refreshWeekDaysFromToday()
                    exploreAiringScheduleViewModel.refresh()
                }

                ExploreSectionOrder.TRENDING -> exploreTrendingViewModel.refresh()
                ExploreSectionOrder.POPULAR -> explorePopularViewModel.refresh()
                ExploreSectionOrder.NEWLY_ADDED -> exploreNewlyAddedViewModel.refresh()
                ExploreSectionOrder.WATCHING -> exploreWatchingViewModel.refresh()
                ExploreSectionOrder.READING -> exploreReadingViewModel.refresh()
            }
        }
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(scrollViewModel){
        scrollViewModel.scrollEventFor(ScrollTarget.HOME).collectLatest {
            scrollState.animateScrollTo(0)
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            refresh()
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {

            viewModel.exploreSectionContentOrderData.forEach {
                if (!it.isEnabled) return@forEach

                when (it.value) {
                    ExploreSectionOrder.AIRING -> {
                        ExploreAiringScheduleSection(exploreAiringScheduleViewModel)
                    }

                    ExploreSectionOrder.TRENDING -> {
                        ExploreMediaSection(viewModel = exploreTrendingViewModel, onFilter = {
                            filterViewModel.value = exploreTrendingFilterViewModel
                            scope.launch {
                                bottomSheetState.expand()
                            }
                        }, onMore = {
                            navigator.browseScreen(
                                BrowseFilterData(
                                    sort = MediaSort.TRENDING_DESC
                                )
                            )
                        })
                    }

                    ExploreSectionOrder.POPULAR -> {
                        ExploreMediaSection(viewModel = explorePopularViewModel, onFilter = {
                            filterViewModel.value = explorePopularFilterViewModel
                            scope.launch {
                                bottomSheetState.expand()
                            }
                        }, onMore = {
                            navigator.browseScreen(
                                BrowseFilterData(
                                    sort = MediaSort.POPULARITY_DESC
                                )
                            )
                        })
                    }

                    ExploreSectionOrder.NEWLY_ADDED -> {
                        ExploreMediaSection(viewModel = exploreNewlyAddedViewModel, onFilter = {
                            filterViewModel.value = exploreNewlyAddedFilterViewModel
                            scope.launch {
                                bottomSheetState.expand()
                            }
                        }, onMore = {
                            navigator.browseScreen(
                                BrowseFilterData(
                                    sort = MediaSort.ID_DESC
                                )
                            )
                        })
                    }

                    ExploreSectionOrder.WATCHING -> {
                        if (!localUser.isLoggedIn) return@forEach
                        ExploreMediaListSection(viewModel = exploreWatchingViewModel)
                    }

                    ExploreSectionOrder.READING -> {
                        if (!localUser.isLoggedIn) return@forEach
                        ExploreMediaListSection(viewModel = exploreReadingViewModel)
                    }
                }
            }

            ExploreFilterBottomSheet(bottomSheetState, filterViewModel)
        }

        RefreshButton(visible = viewModel.showRefreshButton.value) {
            viewModel.showRefreshButton.value = false
            refresh()
        }
    }

}

@Composable
private fun ExploreFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    filterViewModel: MutableState<MediaFilterBottomSheetViewModel>
) {
    MediaFilterBottomSheet(bottomSheetState, filterViewModel.value)
}

@Composable
internal fun ExploreScreenHeader(
    text: String,
    icon: ImageVector,
    onFilter: OnClick? = null,
    onMore: OnClick? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExploreHeaderText(text, icon)
        ExploreHeaderFilterButton(onFilter, onMore)
    }
}


@Composable
private fun ExploreHeaderText(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(modifier = Modifier.padding(start = 4.dp), imageVector = icon, contentDescription = text)
        HeaderText(
            text = text,
        )
    }
}

@Composable
private fun ExploreHeaderFilterButton(filter: OnClick?, more: OnClick?) {
    Row {
        filter?.let {
            ActionMenu(icon = AppIcons.IcFilter, onClick = filter)
        }
        more?.let {
            ActionMenu(icon = AppIcons.IcMoreHoriz, onClick = more)
        }
    }
}


