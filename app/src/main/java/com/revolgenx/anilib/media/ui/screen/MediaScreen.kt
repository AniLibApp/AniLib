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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AlAppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutColors
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.title
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.type.MediaType
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.androidx.compose.koinViewModel

enum class MediaScreenPageType {
    OVERVIEW,
    WATCH,
    CHARACTER,
    STAFF,
    REVIEW,
}

private typealias MediaScreenPage = PagerScreen<MediaScreenPageType>

private val watchPage = MediaScreenPage(
    MediaScreenPageType.WATCH,
    R.string.watch,
    R.drawable.ic_watch,
    true
)

private val pages = listOf(
    MediaScreenPage(MediaScreenPageType.OVERVIEW, R.string.overview, R.drawable.ic_fire),
    watchPage,
    MediaScreenPage(MediaScreenPageType.CHARACTER, R.string.character, R.drawable.ic_character),
    MediaScreenPage(MediaScreenPageType.STAFF, R.string.staff, R.drawable.ic_staff),
    MediaScreenPage(MediaScreenPageType.REVIEW, R.string.review, R.drawable.ic_star)
)

class MediaScreen(
    private val id: Int = 145139,
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
    id: Int,
    mediaType: MediaType?,
    viewModel: MediaViewModel = koinViewModel()
) {
    val pagerState = rememberPagerState()
    val collapsingToolbarState = rememberCollapsingToolbarScaffoldState()
    var visiblePages by remember { mutableStateOf(pages) }
    val media = viewModel.media.value

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapsingToolbarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbarModifier = Modifier.background(MaterialTheme.colorScheme.surface),
        enabled = true,
        toolbar = {
            MediaScreenTopAppBar(media = media, collapsingToolbarState = collapsingToolbarState)
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
                    MediaScreenPageType.OVERVIEW -> MediaOverviewScreen(id, viewModel)
                    MediaScreenPageType.WATCH -> MediaWatchScreen()
                    MediaScreenPageType.CHARACTER -> MediaCharacterScreen(id, mediaType)
                    MediaScreenPageType.STAFF -> MediaStaffScreen(id)
                    MediaScreenPageType.REVIEW -> MediaReviewScreen(id)
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

    FrescoImage(
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
    )


    AppBarLayout(
        colors = AppBarLayoutColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
    ) {
        AppBar(
            colors = AlAppBarDefaults.appBarColors(
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
