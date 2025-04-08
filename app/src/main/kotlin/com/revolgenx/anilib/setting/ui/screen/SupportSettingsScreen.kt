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
        if (billingViewModel.billingConnectionState.intValue == ConnectionState.DISCONNECTED) {
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
                        text = stringResource(R.string.purchased_message)
                    )
                }
            } else {
                OutlinedCard {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.support_message)
                    )
                }
            }

            OutlinedCard() {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    fontSize = 11.sp,
                    text = stringResource(R.string.billing_service_status).format(
                        when (billingViewModel.billingConnectionState.intValue) {
                            ConnectionState.CONNECTED -> stringResource(R.string.connected)
                            ConnectionState.DISCONNECTED -> stringResource(R.string.disconnected)
                            ConnectionState.CONNECTING -> stringResource(R.string.connecting)
                            ConnectionState.CLOSED -> stringResource(R.string.closed)
                            else -> stringResource(R.string.disconnected)
                        }
                    )
                )

                if (billingViewModel.hasPendingPurchase.value) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        text = stringResource(R.string.purchase_status).format(stringResource(R.string.pending))
                    )
                }

                if (billingViewModel.failedToPurchase.value) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        text = stringResource(R.string.purchase_status).format(stringResource(R.string.failed))
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
                    Text(text = stringResource(R.string.purchase))
                }
            }


            SupportSettingsHeader(title = stringResource(R.string.already_purchased))


            OutlinedCard {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.restore_purchase_desc)
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
                    Text(text = stringResource(R.string.check_purchase))
                }
            }



            if (!appIsSupported.value) {

                SupportSettingsHeader(title = stringResource(R.string.settings_ads))


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
