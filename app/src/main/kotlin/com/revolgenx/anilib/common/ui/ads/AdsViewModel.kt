package com.revolgenx.anilib.common.ui.ads

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.common.data.constant.InterstitialAdsInterval
import com.revolgenx.anilib.common.data.constant.RewardedInterstitialAdsInterval
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.setting.data.store.BillingDataStore
import kotlinx.coroutines.delay
import timber.log.Timber
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class AdsViewModel(
    preferencesDataStore: AppPreferencesDataStore,
    billingDataStore: BillingDataStore,
) : ViewModel() {
    private val displayInterstitialAdsInterval = preferencesDataStore.displayInterstitialAdsInterval
    private val displayRewardedInterstitialAdsInterval =
        preferencesDataStore.displayRewardedInterstitialAdsInterval
    private val interstitialAdsDisplayedDateTime =
        preferencesDataStore.interstitialAdsDisplayedDateTime
    private val rewardedInterstitialAdsDisplayedDateTime =
        preferencesDataStore.rewardedInterstitialAdsDisplayedDateTime
    val isAppDevSupported = billingDataStore.isAppDevSupported

    private var mInterstitialAd: InterstitialAd? = null
        set(value) {
            field = value
            field?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdShowedFullScreenContent() {
                    val currentEpochSecond = Instant.now().epochSecond
                    launch {
                        interstitialAdsDisplayedDateTime.set(currentEpochSecond)
                        checkCanShowAds()
                    }
                }

                override fun onAdImpression() {
                    Firebase.analytics.logEvent("interstitial_ad") {
                        param("type", "impression")
                    }
                }

                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    mInterstitialAd = null
                }
            }
        }

    val isRewardedInterstitialAdsReady get() = rewardedInterstitialAd != null
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
        set(value) {
            field = value
            field?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Timber.e("Ad failed to show fullscreen content.")
                    rewardedInterstitialAd = null
                }

                override fun onAdImpression() {
                    Firebase.analytics.logEvent("rewarded_interstitial_ad") {
                        param("type", "impression")
                    }
                    Timber.d("Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    val currentEpochSecond = Instant.now().epochSecond
                    launch {
                        rewardedInterstitialAdsDisplayedDateTime.set(currentEpochSecond)
                        interstitialAdsDisplayedDateTime.set(currentEpochSecond)
                        checkCanShowAds()
                    }
                }
            }
        }


    var canShowInterstitialAd = false
    var canShowRewardedInterstitialAd = false

    init {
       if (!isAppDevSupported.get()){
           checkCanShowAds()
           val canShowAdsJob = launch {
               while (true) {
                   delay(30000)
                   checkCanShowAds()
               }
           }

           launch {
               isAppDevSupported.collect{
                   if (it){
                       canShowInterstitialAd = false
                       canShowRewardedInterstitialAd = false
                       canShowAdsJob.cancel()
                   }
               }
           }
       }

    }

    fun initAds(context: Context) {
        MobileAds.initialize(context) {
            launch {
                while (true) {
                    mInterstitialAd = null
                    InterstitialAd.load(
                        context,
                        BuildConfig.INTERSTITIAL_ADS_ID,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {
                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Timber.e(adError?.toString())
                                mInterstitialAd = null
                            }

                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                Timber.d("Interstitial Ad was loaded.")
                                mInterstitialAd = interstitialAd
                            }
                        })
                    delay(1.toDuration(DurationUnit.HOURS))
                }
            }

            launch {
                while (true) {
                    rewardedInterstitialAd = null
                    RewardedInterstitialAd.load(context,
                        BuildConfig.REWARDED_INTERSTITIAL_ADS_ID,
                        AdRequest.Builder().build(),
                        object : RewardedInterstitialAdLoadCallback() {
                            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                                Timber.d("Rewarded Interstitial Ad was loaded.")
                                rewardedInterstitialAd = ad
                            }

                            override fun onAdFailedToLoad(adError: LoadAdError) {
                                Timber.e(adError?.toString())
                                rewardedInterstitialAd = null
                            }
                        })
                    delay(1.toDuration(DurationUnit.HOURS))
                }
            }
        }
    }

    private fun checkCanShowAds() {
        if(isAppDevSupported.get()) return

        val currentEpochSecond = Instant.now().epochSecond

        launch {
            if (interstitialAdsDisplayedDateTime.get() == null) {
                interstitialAdsDisplayedDateTime.set(currentEpochSecond)
            } else {
                canShowInterstitialAd = isTimeToShowInterstitialAds(
                    InterstitialAdsInterval.fromValue(displayInterstitialAdsInterval.get()!!),
                    currentEpochSecond,
                    interstitialAdsDisplayedDateTime.get()!!
                )
            }

            if (rewardedInterstitialAdsDisplayedDateTime.get() == null) {
                rewardedInterstitialAdsDisplayedDateTime.set(currentEpochSecond)
            } else {
                canShowRewardedInterstitialAd = isTimeToShowRewardedInterstitialAds(
                    RewardedInterstitialAdsInterval.fromValue(displayRewardedInterstitialAdsInterval.get()!!),
                    currentEpochSecond,
                    rewardedInterstitialAdsDisplayedDateTime.get()!!
                )
            }
        }


    }

    fun showInterstitialAds(activity: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(activity)
        } else {
            Timber.d("The interstitial ad wasn't ready yet.")
        }
    }

    fun showRewardedInterstitialAds(activity: Activity) {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd!!.show(activity) {
                Firebase.analytics.logEvent("rewarded_interstitial_ad") {
                    param("type", "ad_completed")
                }
            }
        } else {
            Timber.d("The interstitial ad wasn't ready yet.")
        }
    }

    fun skipRewardedInterstitialAds() {
        val currentEpochSecond = Instant.now().epochSecond
        launch {
            rewardedInterstitialAdsDisplayedDateTime.set(currentEpochSecond)
            checkCanShowAds()
        }
    }

    fun showRewardedInterstitialAdsFromSupport(activity: Activity) {
        if (rewardedInterstitialAd != null) {
            Firebase.analytics.logEvent("rewarded_interstitial_ad") {
                param("event", "user")
                param("type", "support_ad_init")
            }
            rewardedInterstitialAd!!.show(activity) {
                Firebase.analytics.logEvent("rewarded_interstitial_ad") {
                    param("event", "user")
                    param("type", "support_ad_completed")
                }
            }
        } else {
            Timber.d("The rewarded interstitial ad wasn't ready yet.")
        }
    }

    private fun isTimeToShowRewardedInterstitialAds(
        rewardedInterstitialAdsInterval: RewardedInterstitialAdsInterval,
        currentEpochSecond: Long,
        adsDisplayedDateTime: Long
    ): Boolean {
        return when (rewardedInterstitialAdsInterval) {
            RewardedInterstitialAdsInterval.EVERY_OTHER_DAY->{
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 2
            }
            RewardedInterstitialAdsInterval.EVERY_FOURTH_DAY->{
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 4
            }

            RewardedInterstitialAdsInterval.EVERY_WEEK -> {
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 7
            }

        }
    }

    private fun isTimeToShowInterstitialAds(
        interstitialAdsInterval: InterstitialAdsInterval,
        currentEpochSecond: Long,
        adsDisplayedDateTime: Long
    ): Boolean {
        return when (interstitialAdsInterval) {
            InterstitialAdsInterval.EVERY_6_HOURS->{
                ChronoUnit.HOURS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 6
            }
            InterstitialAdsInterval.EVERY_DAY -> {
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 1
            }

            InterstitialAdsInterval.EVERY_OTHER_DAY -> {
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 2
            }

            InterstitialAdsInterval.EVERY_FOURTH_DAY->{
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 4
            }

            InterstitialAdsInterval.EVERY_WEEK -> {
                ChronoUnit.DAYS.between(
                    Instant.ofEpochSecond(adsDisplayedDateTime),
                    Instant.ofEpochSecond(currentEpochSecond)
                ) >= 7
            }

        }
    }
}