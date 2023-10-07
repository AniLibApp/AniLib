package com.revolgenx.anilib.home.season.ui.screen

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.toStringResourceOrNa
import com.revolgenx.anilib.common.ui.component.bottombar.BottomBarLayout
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.SmallRegularText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronLeft
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronRight
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.media.ui.filter.MediaFilterBottomSheet
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toImageVector
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.type.MediaStatus
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

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
            pagingItems = pagingItems,
            onRefresh = {
                viewModel.refresh()
            }
        ) { media ->
            media ?: return@LazyPagingList
            SeasonItem(media, navigator)
        }
    }

    MediaFilterBottomSheet(openBottomSheet, bottomSheetState)
}


@Composable
private fun SeasonItem(
    media: MediaModel = MediaModel(
        title = MediaTitleModel(romaji = "Cowboy Bebop: Tengoku no Tobira"),
        coverImage = MediaCoverImageModel(
            large = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx5-NozHwXWdNLCz.jpg",
            medium = null,
            extraLarge = null
        ),
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
                navigator.mediaScreen(media.id, media.type)
            }
        ) {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(104.dp),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                MediaTitleType { type ->
                    MediumText(
                        text = media.title?.title(type).naText(),
                        fontSize = 16.sp,
                        lineHeight = 18.sp
                    )
                }

                Row(
                    modifier = Modifier.padding(PaddingValues(vertical = 2.dp)),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    media.genres?.take(4)?.map { genre ->
                        SmallLightText(
                            text = genre,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                SmallRegularText(
                    stringResource(id = media.status.toStringRes()),
                    color = media.status.toColor(),
                )

                SmallRegularText(
                    "${media.startDate?.toString().naText()} ~ ${
                        media.endDate?.toString().naText()
                    }",
                )

                SmallRegularText(
                    stringResource(id = I18nR.string.ep_d_s).format(
                        media.episodes.naText(),
                        media.duration.naText()
                    ),
                )

                SmallRegularText(
                    stringResource(id = media.format.toStringRes()),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun SeasonFilter(
    filter: MediaField = MediaField(),
    onNext: OnClick,
    onPrevious: OnClick,
    onFilter: OnClick
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(1.dp)
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
                val season = filter.season?.toStringRes().toStringResourceOrNa()
                filter.season?.toImageVector()?.let {
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
                IconButton(
                    onClick = { onFilter() }) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = AppIcons.IcFilter,
                        contentDescription = "filter"
                    )
                }
            }
        }
    }
}

