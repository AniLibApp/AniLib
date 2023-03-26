package com.revolgenx.anilib.season.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.koinViewModel
import com.revolgenx.anilib.common.ext.naDrawableRes
import com.revolgenx.anilib.common.ext.naStringRes
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.bottombar.BottomBarLayout
import com.revolgenx.anilib.common.ui.compose.paging.PagingLazyColumn
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.screen.LoadingScreen
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.common.ui.state.InitializationState
import com.revolgenx.anilib.common.ui.theme.AppTheme
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toDrawableRes
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.screen.MediaFilterBottomSheetScreen
import com.revolgenx.anilib.media.ui.screen.MediaFilterBottomSheetViewModel
import com.revolgenx.anilib.season.ui.model.SeasonFilterUiModel
import com.revolgenx.anilib.type.MediaStatus
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SeasonScreen() {
    val viewModel = koinViewModel<SeasonViewModel>()
    when (viewModel.initializationState.value) {
        InitializationState.Completed -> {
            BottomBarLayout(bottomBar = {
                val seasonFilterModel = viewModel.seasonFilterUiModel
                val bottomSheetNavigator = LocalBottomSheetNavigator.current

                SeasonFilter(
                    filterModel = seasonFilterModel.value,
                    previous = {
                        viewModel.previousSeason()
                    },
                    next = {
                        viewModel.nextSeason()
                    },
                    filter = {
                        val mediaFilterBottomSheetScreen = MediaFilterBottomSheetScreen()
                        val mediaFilterBottomSheetViewModel = mediaFilterBottomSheetScreen.getLifecycleOwner().koinViewModel<MediaFilterBottomSheetViewModel>()
                        bottomSheetNavigator.show(mediaFilterBottomSheetScreen)
                    }
                )
            }) {
                val pagingItems = viewModel.collectAsLazyPagingItems()
                PagingLazyColumn(
                    items = pagingItems,
                    onRefresh = {
                        viewModel.refresh()
                    }
                ) { media ->
                    media ?: return@PagingLazyColumn
                    SeasonItem(media)
                }
            }
        }

        InitializationState.Initializing -> {
            LoadingScreen()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonItem(
    media: MediaModel = MediaModel(
        title = MediaTitleModel(romaji = "Cowboy Bebop: Tengoku no Tobira"),
        coverImage = MediaCoverImageModel(large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx5-NozHwXWdNLCz.jpg"),
        genres = listOf("Action", "Comedy"),
        status = MediaStatus.FINISHED,
        startDate = FuzzyDateModel(1, 12, 2022),
        episodes = 33,
        duration = 24
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
    ) {
        Row {
            FrescoImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(104.dp),
                imageUrl = media.coverImage?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp)),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    media.title?.title.naText(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = Modifier.padding(PaddingValues(vertical = 2.dp)),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    media.genres?.take(4)?.map { genre ->
                        Text(
                            genre,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Text(
                    stringResource(id = media.status.toStringRes()),
                    color = media.status.toColor(),
                    fontSize = 12.sp
                )

                Text(
                    "${media.startDate?.toString().naText()} ~ ${
                        media.endDate?.toString().naText()
                    }",
                    fontSize = 12.sp
                )
                Text(
                    stringResource(id = R.string.ep_d_s).format(
                        media.episodes.naText(),
                        media.duration.naText()
                    ),
                    fontSize = 12.sp
                )
                Text(
                    stringResource(id = media.format.toStringRes()),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun SeasonFilter(
    filterModel: SeasonFilterUiModel = SeasonFilterUiModel(),
    next: () -> Unit,
    previous: () -> Unit,
    filter: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                val season =
                    stringResource(id = filterModel.season?.toStringRes().naStringRes())
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(
                        id = filterModel.season?.toDrawableRes().naDrawableRes()
                    ), contentDescription = season
                )
                Text(
                    season,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    filterModel.seasonYear.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { previous() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = "left"
                    )
                }
                IconButton(
                    onClick = { next() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "right"
                    )
                }
                IconButton(
                    onClick = { filter() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "filter"
                    )
                }
            }
        }
    }
}

