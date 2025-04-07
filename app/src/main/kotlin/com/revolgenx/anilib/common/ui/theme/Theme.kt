package com.revolgenx.anilib.common.ui.theme

import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.ext.componentActivity
import org.koin.compose.koinInject

@Composable
fun AppTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val themeDataStore: ThemeDataStore = koinInject()
    val themeData = themeDataStore.collectAsState().value
    val isSystemInDarkMode = isSystemInDarkTheme()
    val darkTheme = themeData.darkMode ?: isSystemInDarkMode
    val colorScheme = themeData.colorScheme(darkTheme)

//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    val activity = componentActivity()
    LaunchedEffect(darkTheme) {
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ) { darkTheme },
            navigationBarStyle = if (darkTheme) {
                SystemBarStyle.dark(
                    android.graphics.Color.TRANSPARENT,
                )
            } else {
                SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                )
            },
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            Surface {
                content()
            }
        }
    )
}

