package com.revolgenx.anilib.browse.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseViewModel
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

class BrowseScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        BrowseScreenContent()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowseScreenContent() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel = koinViewModel<BrowseViewModel>()


    ScreenScaffold(
        topBar = {
            BrowseScreenTopAppbar(
                scrollBehavior = scrollBehavior,
                viewModel = viewModel
            )
        }
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            LazyPagingList(
                pagingItems = pagingItems,
                type = viewModel.listType.value,
                onRefresh = {
                    viewModel.refresh()
                },
                gridOptions = GridOptions(GridCells.Adaptive(120.dp)),
                span = { index ->
                    val item = pagingItems[index]
                    GridItemSpan(if (item is HeaderModel) maxLineSpan else 1)
                }
            ) { browseModel ->
                browseModel ?: return@LazyPagingList
                when (browseModel) {
                    is MediaModel -> {
                        BrowseMediaTypeItem(browseModel)
                    }
                }
            }

        }

    }
}


@Composable
private fun BrowseMediaTypeItem(mediaModel: MediaModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .padding(4.dp)
    ) {
        Column {
            MediaCoverImageType { type ->
                AsyncImage(
                    modifier = Modifier
                        .height(165.dp)
                        .fillMaxWidth(),
                    imageUrl = mediaModel.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 2.dp, horizontal = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                MediaTitleType { type ->
                    Text(
                        mediaModel.title?.title(type).naText(),
                        maxLines = 2,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                mediaModel.format?.let {
                    val formats = stringArrayResource(id = R.array.media_format)
                    Text(
                        formats[it.ordinal],
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreenTopAppbar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    viewModel: BrowseViewModel
) {
    var active by rememberSaveable { mutableStateOf(false) }
    val appbarHeight = if(active) 166.dp else 110.dp
    val appbarAnimation by animateDpAsState(
        targetValue = appbarHeight,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )
    AppBarLayout(
        scrollBehavior = scrollBehavior,
        colors = AppBarLayoutDefaults.appBarLayoutColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        containerHeight = appbarAnimation
    ) {
        Column(
            modifier = Modifier
                .height(appbarAnimation)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RowDockedSearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(8.dp),
                    query = viewModel.query,
                    onQueryChange = { viewModel.search = it },
                    onSearch = {
                        active = false
                        viewModel.refresh()
                    },
                    active = active,
                    height = 50.dp,
                    onActiveChange = {
                        active = if (it && true/*viewModel.searchHistory.isNotEmpty()*/) {
                            it
                        } else {
                            false
                        }
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.search))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(id = R.string.search)
                        )
                    },
                    trailingIcon = {
                        Row {
                            if (viewModel.query.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.search = ""
                                    viewModel.refresh()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cancel),
                                        contentDescription = stringResource(id = R.string.clear)
                                    )
                                }
                            }
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_filter),
                                    contentDescription = stringResource(id = R.string.filter)
                                )
                            }
                        }
                    }
                ) {
                    AssistChip(
                        onClick = {
                            viewModel.search = "hello"
                        },
                        label = { Text(text = "hello") },
                        colors = AssistChipDefaults.assistChipColors(leadingIconContentColor = MaterialTheme.colorScheme.onSurface),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                    },
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = stringResource(id = R.string.clear)
                            )
                        })
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 6.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BrowseTypes.values().forEach { browseType ->
                    FilterChip(
                        selected = viewModel.field.browseType.value == browseType,
                        onClick = {
                            viewModel.field.browseType.value = browseType
                            viewModel.refresh()
                        },
                        label = {
                            Text(text = stringResource(id = browseType.title))
                        }
                    )
                }
            }

        }
    }
}