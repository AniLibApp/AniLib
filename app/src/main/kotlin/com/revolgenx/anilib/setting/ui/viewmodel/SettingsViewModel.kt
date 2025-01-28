package com.revolgenx.anilib.setting.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ext.restart
import com.revolgenx.anilib.setting.data.store.BillingDataStore

class SettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore,
    private val airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    private val exploreAiringScheduleFilterDataStore: AiringScheduleFilterDataStore
) : ViewModel() {
    val bugReport = appPreferencesDataStore.crashReport
    val displayInterstitialAdsInterval = appPreferencesDataStore.displayInterstitialAdsInterval
    val displayRewardedInterstitialAdsInterval =
        appPreferencesDataStore.displayRewardedInterstitialAdsInterval

    fun logout(context: Context) {
        launch {
            appPreferencesDataStore.logout()
            airingScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }

            exploreAiringScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }
            context.restart()
        }
    }
}