package com.revolgenx.anilib.list.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBook
import com.revolgenx.anilib.common.ui.icons.appicon.IcBookOutline
import com.revolgenx.anilib.list.ui.viewmodel.MangaListViewModel
import com.revolgenx.anilib.list.ui.viewmodel.MediaListViewModel

object MangaListScreen: MediaListScreen(){
    override val tabIcon: ImageVector = AppIcons.IcBookOutline
    override val selectedIcon: ImageVector =AppIcons.IcBook
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.manga)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                )
            }
        }

    @Composable
    override fun mediaListViewModel(): MediaListViewModel{
        val viewModel = activityViewModel<MangaListViewModel>()
        viewModel.field.userId = localUser().userId
        return viewModel
    }
}