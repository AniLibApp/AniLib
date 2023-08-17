package com.revolgenx.anilib.common.ui.component.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.theme.surfaceContainer


@Composable
fun NavigationIcon(
    @DrawableRes icon: Int? = null,
    tonalButton: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val navigator = LocalMainNavigator.current
    if (tonalButton) {
        FilledTonalIconButton(
            onClick = { onClick?.invoke() ?: navigator.pop() },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = surfaceContainer.copy(alpha = 0.12f)
            )
        ) {
            Icon(
                painter = painterResource(id = icon ?: R.drawable.ic_back), null
            )
        }
    } else {
        IconButton(onClick = {
            onClick?.invoke() ?: navigator.pop()
        }) {
            Icon(
                painter = painterResource(id = icon ?: R.drawable.ic_back), null
            )
        }
    }
}