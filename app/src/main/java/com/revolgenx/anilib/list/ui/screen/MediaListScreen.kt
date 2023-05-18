package com.revolgenx.anilib.list.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.localUser
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel

object MediaListScreen : BaseTabScreen() {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.list)
            val icon = painterResource(id = R.drawable.ic_list)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        MediaListScreenContent()
    }
}

private typealias MediaListScreenPage = PagerScreen<MediaListScreenPageType>

private enum class MediaListScreenPageType {
    ANIME,
    MANGA,
}

private val pages = listOf(
    MediaListScreenPage(MediaListScreenPageType.ANIME, R.string.anime),
    MediaListScreenPage(MediaListScreenPageType.MANGA, R.string.manga),
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MediaListScreenContent() {
    PagerScreenScaffold(
        pages = pages,
        navigationIcon = {},
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val userId = localUser().userId
            when (pages[page].type) {
                MediaListScreenPageType.ANIME -> AnimeListScreen(userId)
                MediaListScreenPageType.MANGA -> MangaListScreen(userId)
            }
        }
    }
}


@Composable
private fun AnimeListScreen(userId: Int?) {
    val viewModel = activityViewModel<AnimeListViewModel>()
    viewModel.field.userId = userId
    MediaListContent(viewModel)
}


@Composable
private fun MangaListScreen(userId: Int?) {
    val viewModel = activityViewModel<MangaListViewModel>()
    viewModel.field.userId = userId
    MediaListContent(viewModel)
}



