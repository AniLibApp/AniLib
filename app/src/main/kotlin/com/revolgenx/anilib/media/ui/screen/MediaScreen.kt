package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.emptyText
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarHeight
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.appbar.CollapsibleAppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.CollapsingAppbar
import com.revolgenx.anilib.common.ui.component.appbar.collapse
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.theme.primary
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.list.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.viewmodel.MediaCharacterViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaScreenPageType
import com.revolgenx.anilib.media.ui.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaStatsViewModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ReviewRating
import com.skydoves.landscapist.ImageOptions
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel


class MediaScreen(
    private val id: Int,
    private val mediaType: MediaType?
) : AndroidScreen() {
    @Composable
    override fun Content() {
        MediaScreenContent(id, mediaType ?: MediaType.ANIME)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaScreenContent(
    mediaId: Int,
    mediaType: MediaType,
) {
    val viewModel: MediaViewModel = koinViewModel()
    val statsViewModel: MediaStatsViewModel = koinViewModel()
    val activityUnionViewModel: ActivityUnionViewModel = koinViewModel()
    val reviewViewModel: MediaReviewViewModel = koinViewModel()
    val staffViewModel: MediaStaffViewModel = koinViewModel()
    val characterViewModel: MediaCharacterViewModel = koinViewModel()

    val collapsingToolbarState = rememberCollapsingToolbarScaffoldState()
    val visiblePages by remember {
        derivedStateOf { viewModel.pages.filter { it.isVisible.value } }
    }
    val pagerState = rememberPagerState() { visiblePages.size }


    LaunchedEffect(viewModel) {
        viewModel.field.mediaId = mediaId
        statsViewModel.field.mediaId = mediaId
        reviewViewModel.field.mediaId = mediaId
        staffViewModel.field.mediaId = mediaId
        characterViewModel.field.mediaId = mediaId
        activityUnionViewModel.field.also {
            it.mediaId = mediaId
            it.type = ActivityType.MEDIA_LIST
        }
        viewModel.getResource()
    }


    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState(), canScroll = {
            pagerState.currentPage == MediaScreenPageType.OVERVIEW.ordinal
        })
    ScreenScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MediaScreenTopAppBar(mediaType, viewModel, scrollBehavior)
        },
        contentWindowInsets = emptyWindowInsets()
    ) {
        PagerScreenScaffold(
            pages = visiblePages,
            pagerState = pagerState,
            navigationIcon = {},
            actions = {},
            contentWindowInsets = NavigationBarDefaults.windowInsets,
            windowInsets = emptyWindowInsets()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                when (visiblePages[page].type) {
                    MediaScreenPageType.OVERVIEW -> MediaOverviewScreen(viewModel)
                    MediaScreenPageType.WATCH -> MediaWatchScreen(viewModel)
                    MediaScreenPageType.CHARACTER -> MediaCharacterScreen(
                        characterViewModel,
                        mediaType
                    )

                    MediaScreenPageType.STAFF -> MediaStaffScreen(staffViewModel)
                    MediaScreenPageType.REVIEW -> MediaReviewScreen(reviewViewModel)
                    MediaScreenPageType.STATS -> MediaStatsScreen(statsViewModel, mediaType)
                    MediaScreenPageType.SOCIAL -> MediaActivityUnionScreen(activityUnionViewModel)
                }

                when (visiblePages[page].type) {
                    MediaScreenPageType.OVERVIEW -> {}
                    else -> {
                        LaunchedEffect(scrollBehavior) {
                            scrollBehavior.state.collapse(scrollBehavior.snapAnimationSpec)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaScreenTopAppBar(
    mediaType: MediaType,
    viewModel: MediaViewModel,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val media = viewModel.resource.value?.stateValue
    val containerHeight = 280.dp
    CollapsingAppbar(
        scrollBehavior = scrollBehavior,
        containerHeight = containerHeight,
        containerContent = {
            AsyncImage(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth(),
                imageUrl = media?.bannerImage,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach,
            )

            Box(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, surfaceContainer.copy(0.8f)
                            )
                        )
                    )
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(start = 20.dp)
                    ) {
                        MediaCoverImageType {
                            AsyncImage(
                                modifier = Modifier
                                    .size(width = 126.dp, height = 190.dp)
                                    .align(Alignment.Bottom),
                                imageUrl = media?.coverImage?.image(it),
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                ),
                                previewPlaceholder = R.drawable.bleach,
                            )
                        }

                        MediaTitleType {
                            MediumText(
                                text = media?.title?.title(it).naText(),
                                fontSize = 16.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Surface(color = primary) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { /*TODO*/ }) {
                                Text(
                                    text = media?.mediaListEntry?.status?.toStringRes(mediaType = mediaType)
                                        ?.let {
                                            stringResource(
                                                id = it
                                            )
                                        }.emptyText()
                                )
                            }

                            ActionMenu(iconRes = R.drawable.ic_arrow_down) {

                            }

                            ActionMenu(iconRes = R.drawable.ic_review) {

                            }

                            IconToggleButton(
                                checked = media?.isFavourite == true,
                                onCheckedChange = {
                                    //todo mutation
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = if (media?.isFavourite == true) R.drawable.ic_heart else R.drawable.ic_heart_outline),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

        },
        title = { isCollapsed ->
            if (isCollapsed) {
                MediaTitleType {
                    Text(
                        text = media?.title?.title(it) ?: stringResource(id = R.string.media),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        },
        actions = { isCollapsed ->
            ActionMenu(imageVector = Icons.Filled.Search, tonalButton = !isCollapsed, onClick = {})
        }
    )
}
