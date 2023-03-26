package com.revolgenx.anilib.home.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.screen.BaseTabScreen

object HomeScreen : BaseTabScreen(){
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.home)
            val icon = painterResource(id = R.drawable.ic_home)

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
        HomeScreenContent()
    }
}



