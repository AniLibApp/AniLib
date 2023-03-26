package com.revolgenx.anilib.user.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.R


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
        Text("Hello to the new world user")
    }
}