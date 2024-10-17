package com.revolgenx.anilib.app.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.store.theme.ThemeDataStore
import com.revolgenx.anilib.common.data.store.theme.isDarkMode
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAnilib
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val themeDataStore: ThemeDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val themeData = themeDataStore.get()

        val darkTheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            this.isDarkMode
        } else {
            val isSystemInDarkMode = this.isDarkMode
            themeData.darkMode ?: isSystemInDarkMode
        }
        enableEdgeToEdge(
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
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (darkTheme) Color(0xFF121212) else Color(0xFFF2F2F2)),
            ) {
                Icon(
                    modifier = Modifier
                        .size(288.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        delay(300)
                    }
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}