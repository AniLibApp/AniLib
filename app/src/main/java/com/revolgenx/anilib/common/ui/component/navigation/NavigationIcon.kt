package com.revolgenx.anilib.common.ui.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator


@Composable
fun NavigationIcon(@DrawableRes icon: Int? = null, override: (() -> Unit)? = null) {
    val navigator = LocalMainNavigator.current
    IconButton(onClick = {
        override?.invoke() ?: navigator.pop()
    }) {
        Icon(
            painter = painterResource(id = icon ?: R.drawable.ic_back), null
        )
    }
}