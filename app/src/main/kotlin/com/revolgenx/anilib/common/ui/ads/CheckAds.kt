package com.revolgenx.anilib.common.ui.ads

import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.app.ui.screen.MainActivityScreen
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.delay


@Composable
fun CheckAds(
    navigator: Navigator?,
    activity: Activity,
    viewModel: MainActivityViewModel,
    adsViewModel: AdsViewModel
) {
    navigator ?: return
    val currentScreen = navigator.lastItem

    var showRewardedDialog by remember {
        mutableStateOf(false)
    }

    if (viewModel.previousScreen != null && viewModel.previousScreen != MainActivityScreen && currentScreen == MainActivityScreen) {
        if (adsViewModel.canShowRewardedInterstitialAd && adsViewModel.isRewardedInterstitialAdsReady) {
            showRewardedDialog = true
        } else if (adsViewModel.canShowInterstitialAd) {
            adsViewModel.showInterstitialAds(activity)
        }
    }
    viewModel.previousScreen = currentScreen

    if (showRewardedDialog) {
        var waitTime by remember {
            mutableIntStateOf(5)
        }

        LaunchedEffect(Unit) {
            while (waitTime > 0) {
                delay(1000)
                waitTime--
            }
            showRewardedDialog = false
            adsViewModel.showRewardedInterstitialAds(activity)
        }

        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRewardedDialog = false
                        adsViewModel.showRewardedInterstitialAds(activity)
                    }
                ) {
                    Text(text = "($waitTime)")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRewardedDialog = false
                        adsViewModel.skipRewardedInterstitialAds()
                    }
                ) {
                    Text(text = stringResource(id = anilib.i18n.R.string.skip))
                }
            },
            title = {
                Text(text = stringResource(id = anilib.i18n.R.string.ads_support_the_app))
            },
            text = {
                Text(text = stringResource(id = anilib.i18n.R.string.ads_play_support_message))
            },
        )
    }

}