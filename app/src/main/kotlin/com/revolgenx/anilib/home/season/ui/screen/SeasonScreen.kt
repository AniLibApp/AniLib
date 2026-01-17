package com.revolgenx.anilib.home.season.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.orNa
import com.revolgenx.anilib.common.ui.component.bottombar.BottomBarLayout
import com.revolgenx.anilib.common.ui.component.button.BadgeIconButton
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronLeft
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronRight
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.home.season.ui.component.SeasonRowCard
import com.revolgenx.anilib.home.season.ui.viewmodel.SeasonFilterViewModel
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.filter.MediaFilterBottomSheet
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toImageVector
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.Card as MCard

@Composable
fun SeasonScreen() {
    val viewModel = koinViewModel<SeasonViewModel>()
    val seasonFilterViewModel = koinViewModel<SeasonFilterViewModel>()
    val scrollViewModel: ScrollViewModel = activityViewModel()
    val navigator = LocalMainNavigator.current
    val bottomSheetState = rememberBottomSheetState()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator)
    val scope = rememberCoroutineScope()

    val scrollState = rememberLazyListState()

    LaunchedEffect(scrollViewModel) {
        scrollViewModel.scrollEventFor(ScrollTarget.HOME).collectLatest {
            scrollState.animateScrollToItem(0)
        }
    }

    BottomBarLayout(bottomBar = {
        SeasonFilter(
            showFilterBadge = viewModel.isFiltered,
            field = viewModel.field,
            onPrevious = {
                viewModel.previousSeason()
            },
            onNext = {
                viewModel.nextSeason()
            },
            onFilter = {
                scope.launch {
                    bottomSheetState.expand()
                }
            }
        )
    }) {
        val pagingItems = viewModel.collectAsLazyPagingItems()

        LazyPagingList(
            pagingItems = pagingItems,
            scrollState = scrollState,
            onRefresh = {
                viewModel.refresh()
            }
        ) { media ->
            media ?: return@LazyPagingList
            SeasonItem(media = media, mediaComponentState = mediaComponentState)
        }
    }

    MediaFilterBottomSheet(bottomSheetState, seasonFilterViewModel)
}


@Composable
private fun SeasonItem(
    media: MediaModel,
    mediaComponentState: MediaComponentState
) {
    SeasonRowCard(media = media, mediaComponentState = mediaComponentState)
}

@Composable
private fun SeasonFilter(
    showFilterBadge: Boolean,
    field: MediaField,
    onNext: OnClick,
    onPrevious: OnClick,
    onFilter: OnClick
) {
    MCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        val iconSize = 28.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 8.dp, vertical = 2.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val season = stringResource(id = field.season?.toStringRes().orNa())
                field.season?.toImageVector()?.let {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = it,
                        contentDescription = season
                    )
                }
                Text(
                    season,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    field.seasonYear.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { onPrevious() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = AppIcons.IcChevronLeft,
                        contentDescription = "left"
                    )
                }
                IconButton(
                    onClick = { onNext() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = AppIcons.IcChevronRight,
                        contentDescription = "right"
                    )
                }
                BadgeIconButton(
                    icon = AppIcons.IcFilter,
                    showBadge = showFilterBadge,
                    onClick = { onFilter() }
                )
            }
        }
    }
}

