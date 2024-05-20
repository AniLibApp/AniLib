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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceItemModel

object SupportScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        SupportSettingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSettingScreen(modifier: Modifier = Modifier) {
    ScreenScaffold(
        title = stringResource(id = anilib.i18n.R.string.support),
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

            OutlinedCard {
                ListPreferenceItem(
                    value = 0, title = "Change Ads Interval", entries = listOf(
                        ListPreferenceEntry("Every hour", 0),
                        ListPreferenceEntry("Every 2 hours", 1),
                        ListPreferenceEntry("Every 4 hours", 2),
                        ListPreferenceEntry("Once a day", 3),
                        ListPreferenceEntry("Twice a month", 4),
                        ListPreferenceEntry("Once a month", 5),
                        ListPreferenceEntry("Never", -1),
                    )
                ) {

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