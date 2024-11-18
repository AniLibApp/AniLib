package com.revolgenx.anilib.setting.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.app.ui.viewmodel.MainActivityViewModel
import com.revolgenx.anilib.common.data.store.AiringScheduleFilterDataStore
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.WidgetThemeDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import kotlinx.coroutines.flow.map

class SettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore,
    private val airingScheduleFilterDataStore: AiringScheduleFilterDataStore,
    private val exploreAiringScheduleFilterDataStore: AiringScheduleFilterDataStore
) : ViewModel() {
    val bugReport = appPreferencesDataStore.crashReport
    val displayInterstitialAdsInterval = appPreferencesDataStore.displayInterstitialAdsInterval
    val displayRewardedInterstitialAdsInterval = appPreferencesDataStore.displayRewardedInterstitialAdsInterval

    fun logout(context: Context){
        launch {
            appPreferencesDataStore.logout()
            airingScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }

            exploreAiringScheduleFilterDataStore.updateData {
                it.copy(showOnlyPlanning = false, showOnlyWatching = false)
            }

            context.startActivity(Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })

            if (context is MainActivity) {
                context.finish()
            }
        }
    }
}