package com.revolgenx.anilib.setting.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ui.bottomsheet.WhatsNewBottomSheet
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAnilib
import com.revolgenx.anilib.common.ui.icons.appicon.IcAssignment
import com.revolgenx.anilib.common.ui.icons.appicon.IcBugReport
import com.revolgenx.anilib.common.ui.icons.appicon.IcInfo
import com.revolgenx.anilib.common.ui.icons.appicon.IcList
import com.revolgenx.anilib.common.ui.icons.appicon.IcPrivacyPolicy
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

data class InfoData(
    val title: String,
    val subTitle: String,
    val imageVector: ImageVector,
    val link: String,
)

@Composable
fun AppAboutScreen() {
    AboutScreenContent()
}

@Composable
private fun AboutScreenContent() {
    val context = localContext()
    val scope = rememberCoroutineScope()
    val viewModel: SettingsViewModel = koinViewModel()
    val bugReport = viewModel.bugReport

    val whatsNewBottomSheetState = rememberBottomSheetState()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.ic_main),
                    contentDescription = null
                )

                Column() {
                    MediumText(
                        text = stringResource(id = I18nR.string.app_name),
                        fontSize = 16.sp
                    )
                    MediumText(
                        text = stringResource(id = I18nR.string.app_subtitle),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = stringResource(id = I18nR.string.app_desc),
                        fontSize = 13.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val appInfo = remember {
                listOf(
                    InfoData(
                        title = context.getString(I18nR.string.anilib_site),
                        subTitle = context.getString(I18nR.string.visit_site_desc),
                        imageVector = AppIcons.IcAnilib,
                        link = context.getString(R.string.anilib_website_link)
                    ),
                    InfoData(
                        title = context.getString(I18nR.string.license_apache_2),
                        subTitle = context.getString(I18nR.string.license_apache_desc),
                        imageVector = AppIcons.IcList,
                        link = context.getString(R.string.anilib_license)
                    )
                )
            }

            Grid(
                modifier = Modifier.padding(20.dp),
                items = appInfo,
                columns = 2,
                columnSpacing = 16.dp,
                rowSpacing = 16.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            context.openUri(it.link)
                        }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = it.imageVector,
                        contentDescription = null
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        MediumText(text = it.title)
                        Text(
                            text = it.subTitle,
                            fontSize = 13.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }


            TextPreferenceItem(
                title = stringResource(id = I18nR.string.privacy_policy),
                subtitle = stringResource(id = I18nR.string.privacy_policy_desc),
                icon = AppIcons.IcPrivacyPolicy
            ) {
                context.openUri(context.getString(R.string.privacy_policy_url))
            }


            TextPreferenceItem(
                title = stringResource(id = I18nR.string.terms_and_condition),
                subtitle = stringResource(id = I18nR.string.terms_and_condition_desc),
                icon = AppIcons.IcAssignment
            ) {
                context.openUri(context.getString(R.string.terms_and_condition_url))
            }

            val isBugReportChecked = bugReport.collectAsState()
            SwitchPreferenceItem(
                icon = AppIcons.IcBugReport,
                title = stringResource(id = I18nR.string.crash_reporting),
                subtitle = stringResource(id = I18nR.string.crash_report_desc),
                checked = isBugReportChecked.value!!
            ) {
                scope.launch {
                    bugReport.set(it)
                }
            }

            TextPreferenceItem(
                title = stringResource(id = I18nR.string.whats_new),
                icon = AppIcons.IcInfo
            ) {
                scope.launch {
                    whatsNewBottomSheetState.peek()
                }
            }
        }

        WhatsNewBottomSheet(bottomSheetState = whatsNewBottomSheetState)
    }
}
