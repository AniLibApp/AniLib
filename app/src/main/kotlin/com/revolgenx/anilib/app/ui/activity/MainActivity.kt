package com.revolgenx.anilib.app.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import com.revolgenx.anilib.app.ui.screen.MainActivityScreen
import com.revolgenx.anilib.common.ui.activity.BaseMainActivity
import com.revolgenx.anilib.common.ui.composition.GlobalViewModelStoreOwner
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.composition.LocalMediaState
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.screen.spoiler.SpoilerBottomSheet
import com.revolgenx.anilib.common.ui.screen.transition.SlideTransition
import com.revolgenx.anilib.common.ui.theme.AppTheme

/*
* todo: handle customtab cancel result
*
*
* todo: get user setting of media title type
* */
class MainActivity : BaseMainActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Navigator(
                    screen = MainActivityScreen,
                    disposeBehavior = NavigatorDisposeBehavior(false)
                ) { navigator ->
                    this@MainActivity.navigator = navigator
                    CompositionLocalProvider(
                        LocalMainNavigator provides navigator,
                        LocalUserState provides viewModel.userState,
                        LocalMediaState provides viewModel.mediaState,
                        GlobalViewModelStoreOwner provides this@MainActivity
                    ) {
                        SlideTransition(navigator = navigator)
                    }
                }

                val bottomSheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )

                SpoilerBottomSheet(
                    openBottomSheet = viewModel.openSpoilerBottomSheet,
                    bottomSheetState = bottomSheetState,
                    spanned = viewModel.spoilerSpanned
                )
            }
        }
    }
}


