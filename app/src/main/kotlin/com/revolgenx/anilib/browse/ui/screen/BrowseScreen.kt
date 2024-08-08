package com.revolgenx.anilib.browse.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import cafe.adriel.voyager.navigator.Navigator
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseFilterViewModel
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseViewModel
import com.revolgenx.anilib.character.ui.component.CharacterCard
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.media.ui.component.MediaItemColumnCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.component.StaffCard
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.studio.ui.component.StudioItem
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class BrowseScreen(
    private var browseFilterData: BrowseFilterData? = null
) : AndroidScreen() {
    @Composable
    override fun Content() {
        BrowseScreenContent(browseFilterData)
        browseFilterData = null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowseScreenContent(browseFilterData: BrowseFilterData?) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val viewModel = koinViewModel<BrowseViewModel>()
    browseFilterData?.let { filter ->
        viewModel.updateFromBrowseFilterData(filter)
    }
    val browseFilterViewModel: BrowseFilterViewModel = koinViewModel()
    val navigator = localNavigator()
    val browseFilterBottomSheetState = rememberBottomSheetState()
    val scope = rememberCoroutineScope()
    val mediaComponentState = rememberMediaComponentState(navigator = navigator)

    ScreenScaffold(
        topBar = {
            BrowseScreenTopAppbar(scrollBehavior = scrollBehavior,
                viewModel = viewModel,
                openFilterBottomSheet = {
                    scope.launch {
                        browseFilterViewModel.updateField(viewModel.field.copy())
                        browseFilterBottomSheetState.expand()
                    }
                })
        },
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        BrowsePagingContent(viewModel, mediaComponentState, navigator)
        BrowseFilterBottomSheet(state = browseFilterBottomSheetState,
            viewModel = browseFilterViewModel,
            onFilter = {
                scope.launch {
                    viewModel.field = browseFilterViewModel.field.copy()
                    viewModel.refresh()
                    browseFilterBottomSheetState.collapse()
                }
            })
    }
}


@Composable
private fun BrowsePagingContent(
    viewModel: BrowseViewModel, mediaComponentState: MediaComponentState, navigator: Navigator
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()

    LazyPagingList(
        pagingItems = pagingItems,
        type = viewModel.listType.value,
        pullRefresh = true,
        onRefresh = {
            viewModel.refresh()
        },
        gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
    ) { browseModel ->
        when (browseModel) {
            is MediaModel -> {
                MediaItemColumnCard(
                    media = browseModel, mediaComponentState = mediaComponentState
                )
            }

            is CharacterModel -> {
                CharacterCard(browseModel) {
                    navigator.characterScreen(it)
                }
            }

            is StaffModel -> {
                StaffCard(browseModel) {
                    navigator.staffScreen(it)
                }
            }

            is StudioModel -> {
                StudioItem(browseModel) {
                    navigator.studioScreen(it)
                }
            }

            is UserModel -> {
                BrowseUserItem(browseModel) {
                    navigator.userScreen(it)
                }
            }
        }
    }
}

@Composable
private fun BrowseUserItem(user: UserModel, onClick: OnClickWithId) {
    Column(
        modifier = Modifier
            .height(110.dp)
            .fillMaxWidth()
            .clickable {
                onClick(user.id)
            },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageAsync(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            imageUrl = user.avatar?.image,
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop, alignment = Alignment.Center
            ),
            previewPlaceholder = R.drawable.bleach
        )

        MediumText(text = user.name.naText())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreenTopAppbar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    viewModel: BrowseViewModel,
    openFilterBottomSheet: OnClick
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val appbarHeight = if (active) 166.dp else 110.dp
    val appbarAnimation by animateDpAsState(
        targetValue = appbarHeight, animationSpec = tween(durationMillis = 300), label = ""
    )
    AppBarLayout(
        scrollBehavior = scrollBehavior, colors = AppBarLayoutDefaults.appBarLayoutColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            scrolledContainerColor = Color.Transparent
        ), containerHeight = appbarAnimation
    ) {
        Column(
            modifier = Modifier
                .height(appbarAnimation)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                RowDockedSearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp),
                    query = viewModel.query,
                    onQueryChange = { viewModel.searchQuery = it },
                    onSearch = {
                        active = false
                        viewModel.updateSearchHistory()
                        viewModel.search()
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = stringResource(id = I18nR.string.search))
                            Icon(
                                modifier = Modifier.padding(horizontal = 4.dp).size(16.dp),
                                imageVector = AppIcons.IcSearch,
                                contentDescription = stringResource(id = I18nR.string.search)
                            )
                        }
                    },
                    leadingIcon = {
                        NavigationIcon()
                    },
                    trailingIcon = {
                        Row {
                            if (viewModel.query.isNotEmpty()) {
                                ActionMenu(
                                    icon = AppIcons.IcCancel,
                                    contentDescriptionRes = I18nR.string.clear
                                ) {
                                    viewModel.searchQuery = ""
                                    viewModel.refresh()
                                }
                            }

                            ActionMenu(
                                icon = AppIcons.IcFilter,
                                contentDescriptionRes = I18nR.string.filter
                            ) {
                                openFilterBottomSheet()
                            }
                        }
                    }) {
                    val searchHistory = viewModel.searchHistory.value
                    if (searchHistory.isEmpty()) {
                        AssistChip(onClick = {}, label = {
                            Text(text = stringResource(id = anilib.i18n.R.string.empty))
                        })
                    } else {
                        searchHistory.forEach { search ->
                            AssistChip(onClick = {
                                viewModel.searchQuery = search
                                viewModel.search()
                            },
                                label = { Text(text = search) },
                                colors = AssistChipDefaults.assistChipColors(leadingIconContentColor = MaterialTheme.colorScheme.onSurface),
                                trailingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                viewModel.deleteSearchHistory(search)
                                            },
                                        imageVector = AppIcons.IcCancel,
                                        contentDescription = stringResource(id = I18nR.string.clear)
                                    )
                                })
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrowseTypes.entries.forEach { browseType ->
                    FilterChip(colors = FilterChipDefaults.filterChipColors(),
                        selected = viewModel.field.browseType.value == browseType,
                        onClick = {
                            viewModel.field.browseType.value = browseType
                            viewModel.refresh()
                        },
                        label = {
                            Text(text = stringResource(id = browseType.title))
                        })
                }
            }

        }
    }
}