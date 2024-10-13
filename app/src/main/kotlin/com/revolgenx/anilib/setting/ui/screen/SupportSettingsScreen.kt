package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.data.constant.InterstitialAdsInterval
import com.revolgenx.anilib.common.data.constant.RewardedInterstitialAdsInterval
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ui.ads.AdsViewModel
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object SupportSettingsScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        SupportSettingsScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSettingsScreenContent() {
    val viewModel: SettingsViewModel = koinViewModel()
    val adsViewModel: AdsViewModel = activityViewModel()
    val context = localContext()
    val scope = rememberCoroutineScope()
    ScreenScaffold(
        title = stringResource(id = R.string.support),
        actions = {},
        contentWindowInsets = horizontalBottomWindowInsets()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedCard {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "\uD83D\uDCCC To support the development of the app, ads are shown at regular intervals. You can choose to watch ads at different intervals. Your support is greatly appreciated! \uD83D\uDE4F\uD83D\uDE4F\uD83D\uDE4F"
                )
            }

            val displayInterstitialAdsInterval = viewModel.displayInterstitialAdsInterval
            val displayInterstitialAdsIntervalState =
                displayInterstitialAdsInterval.collectAsState { InterstitialAdsInterval.fromValue(it!!) }

            OutlinedCard {
                ListPreferenceItem(
                    value = displayInterstitialAdsIntervalState.value,
                    title = stringResource(id = R.string.change_ads_interval),
                    entries = remember {
                        listOf(
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_6_hours),
                                InterstitialAdsInterval.EVERY_6_HOURS
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_once_a_day),
                                InterstitialAdsInterval.EVERY_DAY
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_other_day),
                                InterstitialAdsInterval.EVERY_OTHER_DAY
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_fourth_day),
                                InterstitialAdsInterval.EVERY_FOURTH_DAY
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_week),
                                InterstitialAdsInterval.EVERY_WEEK
                            )
                        )
                    }
                ) {
                    scope.launch {
                        displayInterstitialAdsInterval.set(it!!.ordinal)
                    }
                }
            }


            val displayRewardedInterstitialAdsInterval = viewModel.displayRewardedInterstitialAdsInterval
            val displayRewardedInterstitialAdsIntervalState =
                displayRewardedInterstitialAdsInterval.collectAsState { RewardedInterstitialAdsInterval.fromValue(it!!) }

            OutlinedCard {
                ListPreferenceItem(
                    value = displayRewardedInterstitialAdsIntervalState.value,
                    title = stringResource(id = R.string.change_long_ads_interval),
                    entries = remember {
                        listOf(
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_other_day),
                                RewardedInterstitialAdsInterval.EVERY_OTHER_DAY
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_fourth_day),
                                RewardedInterstitialAdsInterval.EVERY_FOURTH_DAY
                            ),
                            ListPreferenceEntry(
                                context.getString(R.string.ads_every_week),
                                RewardedInterstitialAdsInterval.EVERY_WEEK
                            ),
                        )
                    }
                ) {
                    scope.launch {
                        displayRewardedInterstitialAdsInterval.set(it!!.ordinal)
                    }
                }
            }

            Spacer(modifier = Modifier.size(4.dp))
            OutlinedCard() {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.ads_support_button_message)
                )
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                context.componentActivity()?.let {
                    adsViewModel.showRewardedInterstitialAdsFromSupport(it)
                }
            }) {
                Text(text = "Show ads")
            }

        }
    }
}