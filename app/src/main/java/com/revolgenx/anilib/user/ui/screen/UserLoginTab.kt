package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.media.ui.screen.MediaScreen


object UserLoginTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.profile)
            val icon = painterResource(id = R.drawable.ic_person)

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
        val navigator = LocalMainNavigator.current
        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .clickable {
                navigator.push(MediaScreen())
            },
            text = "Hello to the new world user")
    }
}