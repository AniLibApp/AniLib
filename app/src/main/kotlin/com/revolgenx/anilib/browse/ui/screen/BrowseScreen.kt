package com.revolgenx.anilib.browse.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.browse.data.field.BrowseTypes
import com.revolgenx.anilib.browse.ui.viewmodel.BrowseViewModel
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.app.CharacterOrStaffCard
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.RowDockedSearchBar
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.model.HeaderModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.media.ui.component.MediaItemCard
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.user.ui.model.UserModel
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

class BrowseScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        BrowseScreenContent()
    }
}

private typealias OnClick = (id: Int) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowseScreenContent() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel = koinViewModel<BrowseViewModel>()
    val navigator = localNavigator()

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
                when (browseModel) {
                    is MediaModel -> {
                        MediaItemCard(browseModel) {id, type->
                            navigator.mediaScreen(id, type)
                        }
                    }

                    is CharacterModel -> {
                        with(browseModel) {
                            CharacterOrStaffCard(
                                title = name?.full.naText(),
                                imageUrl = image?.image
                            ) {
                                navigator.staffScreen(id)
                                navigator.characterScreen(id)

                            }
                        }
                    }

                    is StaffModel -> {
                        with(browseModel) {
                            CharacterOrStaffCard(
                                title = name?.full.naText(),
                                imageUrl = image?.image
                            ) {
                                navigator.staffScreen(id)
                            }
                        }
                    }

                    is StudioModel -> {
                        BrowseStudioItem(browseModel, onMediaClick = { id, type ->
                            navigator.mediaScreen(id, type)
                        }, onClick = {
                            navigator.studioScreen(it)
                        })
                    }

                    is UserModel -> {
                        BrowseUserItem(browseModel) {
                            navigator.userScreen(it)
                        }
                    }
                }
            }

        }

    }
}

@Composable
private fun BrowseUserItem(user: UserModel, onClick: OnClick) {
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
        AsyncImage(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            imageUrl = user.avatar?.image,
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            previewPlaceholder = R.drawable.bleach
        )

        MediumText(text = user.name.naText())
    }
}

@Composable
private fun BrowseStudioItem(studio: StudioModel, onMediaClick: OnMediaClick, onClick: OnClick) {
    val medias = studio.media?.nodes ?: emptyList()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onClick(studio.id)
                    },
                text = studio.name.naText(),
                fontSize = 15.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier
                    .clickable {
                        onClick(studio.id)
                    },
                text = stringResource(id = R.string.view_all),
                color = colorScheme().onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        LazyRow {
            items(items = medias) {
                MediaItemCard(media = it, width = 120.dp, onMediaClick = onMediaClick)
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
    val appbarHeight = if (active) 166.dp else 110.dp
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