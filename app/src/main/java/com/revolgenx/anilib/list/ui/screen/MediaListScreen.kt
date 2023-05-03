package com.revolgenx.anilib.list.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen

object MediaListScreen : BaseTabScreen(){
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
    }
}