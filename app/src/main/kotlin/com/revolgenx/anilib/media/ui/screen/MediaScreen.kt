package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaScreenPageType
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.type.MediaType
import com.skydoves.landscapist.ImageOptions
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel


class MediaScreen(
    private val id: Int,
    private val mediaType: MediaType? = null
) : AndroidScreen() {
    @Composable
    override fun Content() {
        MediaScreenContent(id, mediaType)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MediaScreenContent(
    mediaId: Int,
    mediaType: MediaType?,
    viewModel: MediaViewModel = koinViewModel()
) {
    val collapsingToolbarState = rememberCollapsingToolbarScaffoldState()
    val pages = viewModel.pages
    val visiblePages by remember {
        derivedStateOf { pages.filter { it.isVisible.value } }
    }
    val pagerState = rememberPagerState() { visiblePages.size }


    LaunchedEffect(viewModel) {
        viewModel.field.mediaId = mediaId
        viewModel.getResource()
    }

    val media = remember {
        derivedStateOf {
            val resourceState = viewModel.resource.value
            if (resourceState is ResourceState.Success)
                resourceState.data
            else null
        }
    }

    if(media.value?.streamingEpisodes?.isNotEmpty() == true){
        viewModel.showWatchPage()
    }

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingToolbarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbarModifier = Modifier.background(MaterialTheme.colorScheme.surface),
        enabled = true,
        toolbar = {
            MediaScreenTopAppBar(media = media.value, collapsingToolbarState = collapsingToolbarState)
        }
    ) {
        PagerScreenScaffold(
            pages = visiblePages,
            pagerState = pagerState,
            navigationIcon = {},
            actions = {},
            contentWindowInsets = NavigationBarDefaults.windowInsets,
            windowInsets = WindowInsets(0)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (visiblePages[page].type) {
                    MediaScreenPageType.OVERVIEW -> MediaOverviewScreen(viewModel)
                    MediaScreenPageType.WATCH -> MediaWatchScreen(viewModel)
                    MediaScreenPageType.CHARACTER -> MediaCharacterScreen(mediaId, mediaType)
                    MediaScreenPageType.STAFF -> MediaStaffScreen(mediaId)
                    MediaScreenPageType.REVIEW -> MediaReviewScreen(mediaId)
                    MediaScreenPageType.STATS -> MediaStatsScreen(mediaId)
                    MediaScreenPageType.SOCIAL -> MediaSocialScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsingToolbarScope.MediaScreenTopAppBar(
    media: MediaModel? = null,
    collapsingToolbarState: CollapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
) {
    val progress = collapsingToolbarState.toolbarState.progress
    val imageAlpha = if (progress <= 0.2) 0.2f else progress

    AsyncImage(
        modifier = Modifier
            .parallax(0.5f)
            .height(300.dp)
            .fillMaxWidth()
            .graphicsLayer {
                alpha = imageAlpha
            },
        imageUrl = media?.bannerImage,
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        ),
        previewPlaceholder = R.drawable.bleach,
        failure = {}
    )


    AppBarLayout(
        colors = AppBarLayoutColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
    ) {
        AppBar(
            colors = AppBarDefaults.appBarColors(
                containerColor = Color.Transparent,
            ),
            title = {
                MediaTitleType {
                    Text(
                        text = media?.title?.title(it) ?: stringResource(id = R.string.media),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            },
            navigationIcon = {
                NavigationIcon()
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, null)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Notifications, null)
                }
            }
        )
    }


}
