package com.revolgenx.anilib.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.revolgenx.anilib.airing.ui.screen.AiringScheduleScreen
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                BottomSheetNavigator(
                    sheetShape = MaterialTheme.shapes.extraLarge,
                    sheetBackgroundColor = MaterialTheme.colorScheme.background,
                    scrimColor = MaterialTheme.colorScheme.scrim
                ) {
                    Navigator(
                        screen = AiringScheduleScreen()/*MainActivityScreen()*/,
                        disposeBehavior = NavigatorDisposeBehavior(false, false)
                    ) { navigator ->
                        CompositionLocalProvider(
                            LocalMainNavigator provides navigator
                        ) {
                            CurrentScreen()
                        }
                    }
                }
            }
        }
    }
}
