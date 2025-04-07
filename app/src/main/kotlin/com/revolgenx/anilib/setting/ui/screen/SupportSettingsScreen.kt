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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.android.billingclient.api.BillingClient.ConnectionState
import com.revolgenx.anilib.common.data.constant.InterstitialAdsInterval
import com.revolgenx.anilib.common.data.constant.RewardedInterstitialAdsInterval
import com.revolgenx.anilib.common.ext.activityViewModel
import com.revolgenx.anilib.common.ext.componentActivity
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.ads.AdsViewModel
import com.revolgenx.anilib.common.ui.component.common.HeaderText
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcMinus
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlus
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.BillingViewModel
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
    val billingViewModel: BillingViewModel = activityViewModel()

    val context = localContext()
    val scope = rememberCoroutineScope()
    val appIsSupported = billingViewModel.isAppDevSupported.collectAsState(false)

    LaunchedEffect(billingViewModel) {
        if (billingViewModel.billingConnectionState.intValue == ConnectionState.DISCONNECTED && !billingViewModel.isAppDevSupported.get()) {
            billingViewModel.restartBillingConnection()
        }
    }
    ScreenScaffold(
        title = stringResource(id = R.string.settings_support),
        actions = {},
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { snackbar ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (appIsSupported.value) {
                OutlinedCard {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Thank you for supporting the app! Your purchase means a lot! \uD83E\uDD73 \uD83E\uDD73"
                    )
                }
            } else {
                OutlinedCard {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = "♥️ Support the app by making a one-time purchase to remove ads or by watching ads at regular intervals. Your support keeps me motivated and is greatly appreciated! \uD83D\uDE4F"
                    )
                }
            }

            OutlinedCard() {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    fontSize = 11.sp,
                    text = "Billing Service Status: ${
                        when (billingViewModel.billingConnectionState.intValue) {
                            ConnectionState.CONNECTED -> "Connected"
                            ConnectionState.DISCONNECTED -> "Disconnected"
                            ConnectionState.CONNECTING -> "Connecting"
                            ConnectionState.CLOSED -> "Closed"
                            else -> "Disconnected"
                        }
                    }"
                )

                if (billingViewModel.hasPendingPurchase.value) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        text = "Purchase Status: Pending"
                    )
                }

                if (billingViewModel.failedToPurchase.value) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        text = "Purchase Status: Failed"
                    )
                }

                val purchaseQuantity = remember {
                    mutableIntStateOf(2)
                }

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    SegmentedButton(
                        selected = false,
                        onClick = {
                            if (purchaseQuantity.intValue > 1) purchaseQuantity.intValue--
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                    ) {
                        Icon(imageVector = AppIcons.IcMinus, contentDescription = null)
                    }

                    SegmentedButton(
                        selected = true,
                        onClick = { },
                        enabled = false,
                        colors = SegmentedButtonDefaults.colors(
                            disabledActiveContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledActiveContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            disabledActiveBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                        icon = {}
                    ) {

                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            text = "$${2 * purchaseQuantity.intValue - 1}.99"
                        )
                    }

                    SegmentedButton(
                        selected = false,
                        onClick = {
                            if (purchaseQuantity.intValue < 5) purchaseQuantity.intValue++
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                    ) {
                        Icon(imageVector = AppIcons.IcPlus, contentDescription = null)
                    }
                }



                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    enabled = billingViewModel.billingConnectionState.intValue == ConnectionState.CONNECTED,
                    onClick = {
                        context.componentActivity()?.let {
                            billingViewModel.startPurchase(it, purchaseQuantity.intValue)
                        }
                    }
                ) {
                    Text(text = "Purchase")
                }
            }


            SupportSettingsHeader(title = "Already purchased?")


            OutlinedCard {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "\uD83D\uDCE2 To restore your purchases, you must use the same Google Play account that was used to make the purchase. Switching accounts may result in the inability to access purchased features. "
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    enabled = billingViewModel.billingConnectionState.intValue == ConnectionState.CONNECTED,
                    onClick = {
                        billingViewModel.queryPurchases()
                    }
                ) {
                    Text(text = "Check Purchase")
                }
            }



            if (!appIsSupported.value) {

                SupportSettingsHeader(title = "Ads")


                val displayInterstitialAdsInterval = viewModel.displayInterstitialAdsInterval
                val displayInterstitialAdsIntervalState =
                    displayInterstitialAdsInterval.collectAsState {
                        InterstitialAdsInterval.fromValue(
                            it!!
                        )
                    }

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


                val displayRewardedInterstitialAdsInterval =
                    viewModel.displayRewardedInterstitialAdsInterval
                val displayRewardedInterstitialAdsIntervalState =
                    displayRewardedInterstitialAdsInterval.collectAsState {
                        RewardedInterstitialAdsInterval.fromValue(
                            it!!
                        )
                    }

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

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        onClick = {
                            context.componentActivity()?.let {
                                adsViewModel.showRewardedInterstitialAdsFromSupport(it)
                            }
                        }) {
                        Text(text = "Show ads")
                    }
                }
            }
        }
    }
}


@Composable
private fun SupportSettingsHeader(title: String) {
    HeaderText(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp), text = title)
}
