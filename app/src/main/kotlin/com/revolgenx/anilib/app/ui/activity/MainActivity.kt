package com.revolgenx.anilib.app.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import com.revolgenx.anilib.app.ui.screen.MainActivityScreen
import com.revolgenx.anilib.app.ui.viewmodel.DeepLinkPath
import com.revolgenx.anilib.common.ext.activityScreen
import com.revolgenx.anilib.common.ext.airingScheduleScreen
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.notificationScreen
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.activity.BaseMainActivity
import com.revolgenx.anilib.common.ui.composition.GlobalViewModelStoreOwner
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.composition.LocalMediaState
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.screen.spoiler.SpoilerBottomSheet
import com.revolgenx.anilib.common.ui.screen.transition.SlideTransition
import com.revolgenx.anilib.common.ui.theme.AppTheme
import com.revolgenx.anilib.type.MediaType

class MainActivity : BaseMainActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Navigator(
                    screen = MainActivityScreen,
                    disposeBehavior = NavigatorDisposeBehavior(false)
                ) { navigator ->
                    this@MainActivity.navigator = navigator
                    CanShowAds()
                    CompositionLocalProvider(
                        LocalMainNavigator provides navigator,
                        LocalUserState provides viewModel.userState,
                        LocalMediaState provides viewModel.mediaState,
                        GlobalViewModelStoreOwner provides this@MainActivity
                    ) {
                        SlideTransition(navigator = navigator)
                    }
                    CheckDeepLinkNavigation(navigator)
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

    @Composable
    private fun CheckDeepLinkNavigation(navigator: Navigator) {
        LaunchedEffect(viewModel.deepLinkPath.value) {
            viewModel.deepLinkPath.value?.let { deepLinkPath ->
                val handle: Any = when (deepLinkPath.first) {
                    DeepLinkPath.HOME ->{
                        navigator.popUntilRoot()
                    }

                    DeepLinkPath.ANIME -> {
                        navigator.mediaScreen(deepLinkPath.second as Int, MediaType.ANIME)
                    }

                    DeepLinkPath.MANGA -> {
                        navigator.mediaScreen(deepLinkPath.second as Int, MediaType.MANGA)
                    }

                    DeepLinkPath.USER -> {
                        val user = deepLinkPath.second
                        navigator.userScreen(user as? Int, user as? String)
                    }

                    DeepLinkPath.CHARACTER -> {
                        navigator.characterScreen(deepLinkPath.second as Int)
                    }

                    DeepLinkPath.STAFF -> {
                        navigator.staffScreen(deepLinkPath.second as Int)
                    }

                    DeepLinkPath.STUDIO -> {
                        navigator.studioScreen(deepLinkPath.second as Int)
                    }

                    DeepLinkPath.ACTIVITY -> {
                        navigator.activityScreen(deepLinkPath.second as Int)
                    }

                    DeepLinkPath.NOTIFICATION -> {
                        navigator.notificationScreen()
                    }

                    DeepLinkPath.AIRING -> {
                        navigator.airingScheduleScreen()
                    }

                    DeepLinkPath.LIST_ENTRY_EDITOR -> {
                        navigator.mediaListEntryEditorScreen(deepLinkPath.second as Int)
                    }

                    DeepLinkPath.ANIME_LIST -> {false}
                    DeepLinkPath.MANGA_LIST -> {false}
                }
                if(handle !is Boolean){
                    viewModel.deepLinkPath.value = null
                }
            }

        }

    }

}


