package com.revolgenx.anilib.setting.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore

class SettingsViewModel(
    val appPreferencesDataStore: AppPreferencesDataStore,
) : ViewModel() {
    val bugReport = appPreferencesDataStore.crashReport
    val displayInterstitialAdsInterval = appPreferencesDataStore.displayInterstitialAdsInterval
    val displayRewardedInterstitialAdsInterval = appPreferencesDataStore.displayRewardedInterstitialAdsInterval
}