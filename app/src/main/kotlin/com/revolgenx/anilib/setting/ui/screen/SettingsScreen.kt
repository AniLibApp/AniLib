package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.common.data.constant.Config
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.common.ShowIfNotLoggedIn
import com.revolgenx.anilib.common.ui.component.dialog.ConfirmationDialog
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAnilib
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeart
import com.revolgenx.anilib.common.ui.icons.appicon.IcInfoOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcList
import com.revolgenx.anilib.common.ui.icons.appicon.IcLogin
import com.revolgenx.anilib.common.ui.icons.appicon.IcLogout
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcPalette
import com.revolgenx.anilib.common.ui.icons.appicon.IcPerson
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonAdd
import com.revolgenx.anilib.common.ui.icons.appicon.IcPersonOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcSearch
import com.revolgenx.anilib.common.ui.icons.appicon.IcTune
import com.revolgenx.anilib.common.ui.icons.appicon.IcWidgets
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.theme.logout_color
import com.revolgenx.anilib.common.ui.theme.support_color
import com.revolgenx.anilib.common.util.versionName
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.screen.about.AboutSettingsScreen
import com.revolgenx.anilib.setting.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object SettingScreen : BaseTabScreen() {
    var isTab = false

    override val tabIcon: ImageVector = AppIcons.IcPersonOutline
    override val selectedIcon: ImageVector = AppIcons.IcPerson

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(I18nR.string.profile)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                )
            }
        }

    @Composable
    override fun Content() {
        SettingScreenContent(isTab)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContent(isTab: Boolean) {
    val viewModel: SettingsViewModel = koinViewModel()
    val context = localContext()
    val scope = rememberCoroutineScope()
    val navigator = localNavigator()

    val openLoginDialog = remember {
        mutableStateOf(false)
    }
    val openRegisterDialog = remember {
        mutableStateOf(false)
    }

    val openLogoutDialog = remember {
        mutableStateOf(false)
    }

    ScreenScaffold(
        title = stringResource(id = I18nR.string.settings),
        topBar = (@Composable {}).takeIf { isTab },
        contentWindowInsets = if (isTab) WindowInsets.statusBars else ScaffoldDefaults.contentWindowInsets
    ) { snackbarHostState ->
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            if (isTab) {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center),
                        imageVector = AppIcons.IcAnilib,
                        contentDescription = null,
                    )

                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        ActionMenu(
                            icon = AppIcons.IcSearch,
                            tonalButton = true
                        ) {
                            navigator.push(SearchSettingScreen)
                        }
                    }
                }
                HorizontalDivider()
            }

            ShowIfNotLoggedIn {
                TextPreferenceItem(
                    icon = AppIcons.IcLogin,
                    title = stringResource(id = I18nR.string.settings_login),
                    subtitle = stringResource(id = I18nR.string.settings_log_into_anilist)
                ) {
                    openLoginDialog.value = true
                }

                TextPreferenceItem(
                    icon = AppIcons.IcPersonAdd,
                    title = stringResource(I18nR.string.settings_sign_up),
                    subtitle = stringResource(I18nR.string.settings_sign_up_to_anilist)
                ) {
                    openRegisterDialog.value = true
                }

                HorizontalDivider()
            }

            TextPreferenceItem(
                icon = AppIcons.IcTune,
                title = stringResource(I18nR.string.settings_general),
                subtitle = stringResource(I18nR.string.settings_general_desc)
            ) {
                navigator.push(GeneralSettingsScreen)

            }

            TextPreferenceItem(
                icon = AppIcons.IcPalette,
                title = stringResource(I18nR.string.settings_appearance),
                subtitle = stringResource(I18nR.string.settings_appearance_desc)
            ) {
                navigator.push(AppearanceSettingsScreen)
            }

            TextPreferenceItem(
                icon = AppIcons.IcMedia,
                title = stringResource(I18nR.string.settings_anime_and_manga),
                subtitle = stringResource(I18nR.string.settings_anime_and_manga_desc)
            ) {
                navigator.push(MediaSettingsScreen)
            }

            ShowIfLoggedIn {
                TextPreferenceItem(
                    icon = AppIcons.IcList,
                    title = stringResource(I18nR.string.settings_lists),
                    subtitle = stringResource(I18nR.string.settings_list_desc)
                ) {
                    navigator.push(MediaListSettingsScreen)
                }

                TextPreferenceItem(
                    icon = AppIcons.IcNotification,
                    title = stringResource(I18nR.string.notifications),
                    subtitle = stringResource(I18nR.string.settings_notifications_desc)
                ) {
                    navigator.push(NotificationSettingsScreen)
                }
            }

//            TextPreferenceItem(
//                icon = AppIcons.IcFilter,
//                title = stringResource(I18nR.string.filter),
//                subtitle = stringResource(I18nR.string.settings_filter_desc)
//            )

            TextPreferenceItem(
                icon = AppIcons.IcWidgets,
                title = stringResource(I18nR.string.widget),
                subtitle = stringResource(I18nR.string.settings_filter_desc)
            ){
                navigator.push(WidgetSettingsScreen)
            }


            TextPreferenceItem(
                icon = AppIcons.IcHeart,
                title = stringResource(I18nR.string.settings_support),
                subtitle = stringResource(I18nR.string.settings_support_desc)
                    .format(versionName),
                iconTint = support_color,
                onClick = {
                    navigator.push(SupportSettingsScreen)
                }
            )

            TextPreferenceItem(
                icon = AppIcons.IcInfoOutline,
                title = stringResource(I18nR.string.about),
                subtitle = stringResource(I18nR.string.settings_about_desc)
                    .format(versionName)
            ) {
                navigator.push(AboutSettingsScreen)
            }

            ShowIfLoggedIn {
                TextPreferenceItem(
                    icon = AppIcons.IcLogout,
                    title = stringResource(I18nR.string.logout),
                    iconTint = logout_color
                ) {
                    openLogoutDialog.value = true
                }
            }
        }


        ConfirmationDialog(
            openDialog = openLoginDialog,
            message = stringResource(id = I18nR.string.settings_login_signup_notice),
            title = stringResource(id = I18nR.string.settings_important_to_know)
        ) {
            login(context)
        }


        ConfirmationDialog(
            openDialog = openRegisterDialog,
            message = stringResource(id = I18nR.string.settings_login_signup_notice),
            title = stringResource(id = I18nR.string.settings_important_to_know)
        ) {
            context.openUri(
                Config.SIGN_UP_URL,
                scope = scope,
                snackbarHostState = snackbarHostState
            )
        }

        ConfirmationDialog(
            openDialog = openLogoutDialog,
            message = stringResource(id = I18nR.string.settings_are_you_sure_you_want_to_log_out),
            title = stringResource(id = I18nR.string.logout)
        ) {
            scope.launch {
                viewModel.appPreferencesDataStore.logout()
                context.startActivity(Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                if (context is MainActivity) {
                    context.finish()
                }
            }
        }
    }


}

private fun login(context: Context) {
    val authUri = Config.AUTH_ENDPOINT.toUri().buildUpon()
        .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
        .appendQueryParameter("response_type", "token")
        .build()
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, authUri)
}

