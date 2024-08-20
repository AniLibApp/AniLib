package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.revolgenx.anilib.common.data.constant.AdsInterval
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceItemModel
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object SupportScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        SupportSettingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSettingScreen(modifier: Modifier = Modifier) {
    val viewModel: SettingsViewModel = koinViewModel()
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
                    text = "\uD83D\uDCCC If you enjoy the app and would like to support its development, you can choose to watch ads at regular intervals. These ads are not intrusive and will only appear when you prefer to see them. If you'd rather not see the reminders, you have the option to adjust the frequency of the ads or turn them off entirely. Your support is greatly appreciated! \uD83D\uDE4F\uD83D\uDE4F\uD83D\uDE4F"
                )
            }

            val displayAdsInterval = viewModel.appPreferencesDataStore.displayAdsInterval
            val displayAdsIntervalState = displayAdsInterval.collectAsState{ AdsInterval.fromValue(it!!)}
            OutlinedCard {
                ListPreferenceItem(
                    value = displayAdsIntervalState.value, title = stringResource(id = R.string.change_ads_interval), entries = remember {
                        listOf(
                            ListPreferenceEntry(context.getString(R.string.ads_8_every_hour), AdsInterval.EVERY_8_HR),
                            ListPreferenceEntry(context.getString(R.string.ads_once_a_day), AdsInterval.EVERY_DAY),
                            ListPreferenceEntry(context.getString(R.string.ads_every_other_day), AdsInterval.EVERY_OTHER_DAY),
                            ListPreferenceEntry(context.getString(R.string.ads_end_of_every_week), AdsInterval.EVERY_WEEK),
                            ListPreferenceEntry(context.getString(R.string.ads_once_every_month), AdsInterval.EVERY_MONTH),
                            ListPreferenceEntry(context.getString(R.string.never), AdsInterval.NEVER),
                        )
                    }
                ) {
                    scope.launch {
                        displayAdsInterval.set(it!!.value)
                    }
                }
            }

            OutlinedCard() {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Additionally, you can donate directly via the Play Store."
                )
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text(text = "Donate 1")
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text(text = "Donate 3")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text(text = "Donate 9")
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                Text(text = "Donate 18")
            }

        }
    }
}