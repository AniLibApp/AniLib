package com.revolgenx.anilib.list.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.list.ui.viewmodel.AnimeListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel

object AnimeListScreen : MediaListScreen() {
    override val iconRes: Int = R.drawable.ic_media_outline
    override val selectedIconRes: Int = R.drawable.ic_media
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.anime)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                )
            }
        }

    @Composable
    override fun mediaListViewModel(): MediaListViewModel {
        val viewModel = activityViewModel<AnimeListViewModel>()
        viewModel.field.userId = localUser().userId
        return viewModel
    }
}