package com.revolgenx.anilib.season.ui.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naDrawableRes
import com.revolgenx.anilib.common.ext.naStringRes
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.bottombar.BottomBarLayout
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.title
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toDrawableRes
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.screen.MediaFilterBottomSheetScreen
import com.revolgenx.anilib.media.ui.screen.MediaFilterModalBottomSheet
import com.revolgenx.anilib.media.ui.screen.MediaScreen
import com.revolgenx.anilib.type.MediaStatus
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonScreen() {
    val viewModel = koinViewModel<SeasonViewModel>()
    val navigator = LocalMainNavigator.current
    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    BottomBarLayout(bottomBar = {
        val filter = viewModel.field
        SeasonFilter(
            filter = filter,
            onPrevious = {
                viewModel.previousSeason()
            },
            onNext = {
                viewModel.nextSeason()
            },
            onFilter = {
                openBottomSheet.value = true
            }
        )
    }) {
        val pagingItems = viewModel.collectAsLazyPagingItems()

        LazyPagingList(
            items = pagingItems,
            onRefresh = {
                viewModel.refresh()
            }
        ) { media ->
            media ?: return@LazyPagingList
            SeasonItem(media, navigator)
        }
    }

    MediaFilterModalBottomSheet(openBottomSheet, bottomSheetState)
}


@Composable
private fun SeasonItem(
    media: MediaModel = MediaModel(
        title = MediaTitleModel(romaji = "Cowboy Bebop: Tengoku no Tobira"),
        coverImage = MediaCoverImageModel(large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx5-NozHwXWdNLCz.jpg"),
        genres = listOf("Action", "Comedy"),
        status = MediaStatus.FINISHED,
        startDate = FuzzyDateModel(1, 12, 2022),
        episodes = 33,
        duration = 24,
    ),
    navigator: Navigator
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.clickable {
                navigator.push(MediaScreen(media.id, media.type))
            }
        ) {
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
                MediaTitleType { type ->
                    Text(
                        media.title?.title(type).naText(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

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
    filter: MediaField = MediaField(),
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFilter: () -> Unit
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
                    stringResource(id = filter.season?.toStringRes().naStringRes())
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(
                        id = filter.season?.toDrawableRes().naDrawableRes()
                    ), contentDescription = season
                )
                Text(
                    season,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    filter.seasonYear.toString(),
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
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = "left"
                    )
                }
                IconButton(
                    onClick = { onNext() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "right"
                    )
                }
                IconButton(
                    onClick = { onFilter() }) {
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

