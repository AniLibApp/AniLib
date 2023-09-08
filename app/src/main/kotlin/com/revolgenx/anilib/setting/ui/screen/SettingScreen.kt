package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.constant.Config
import com.revolgenx.anilib.common.data.store.logout
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.toPainterResource
import com.revolgenx.anilib.common.ext.toStringResource
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.common.ShowIfNotLoggedIn
import com.revolgenx.anilib.common.ui.component.dialog.ConfirmationDialog
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.tab.BaseTabScreen
import com.revolgenx.anilib.common.ui.theme.logout_color
import com.revolgenx.anilib.common.util.versionName
import com.revolgenx.anilib.setting.ui.component.SettingItem
import kotlinx.coroutines.launch

object SettingScreen : BaseTabScreen() {
    var isTab = false

    override val iconRes: Int = R.drawable.ic_person_outline
    override val selectedIconRes: Int = R.drawable.ic_person

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.profile)

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
    val context = localContext()
    val scope = rememberCoroutineScope()
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
        title = R.string.settings.toStringResource(),
        topBar = (@Composable {}).takeIf { isTab },
        actions = {
            ActionMenu(
                iconRes = R.drawable.ic_search
            ) {

            }
        }
    ) {
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
                        painter = R.drawable.ic_anilib.toPainterResource(),
                        contentDescription = null,
                    )

                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        ActionMenu(
                            iconRes = R.drawable.ic_search,
                            tonalButton = true
                        ) {

                        }
                    }
                }
                HorizontalDivider()
            }

            ShowIfNotLoggedIn {
                SettingItem(
                    icon = R.drawable.ic_login,
                    title = R.string.login.toStringResource(),
                    subtitle = R.string.log_into_anilist.toStringResource()
                ) {
                    openLoginDialog.value = true
                }

                SettingItem(
                    icon = R.drawable.ic_person_add,
                    title = R.string.sign_up.toStringResource(),
                    subtitle = R.string.sign_up_to_anilist.toStringResource()
                ) {
                    openRegisterDialog.value = true
                }

                HorizontalDivider()
            }

            SettingItem(
                icon = R.drawable.ic_tune,
                title = R.string.general.toStringResource(),
                subtitle = R.string.general_setting_desc.toStringResource()
            )

            SettingItem(
                icon = R.drawable.ic_palette,
                title = R.string.appearance.toStringResource(),
                subtitle = R.string.appearance_desc.toStringResource()
            )

            SettingItem(
                icon = R.drawable.ic_media,
                title = R.string.anime_and_manga.toStringResource(),
                subtitle = R.string.anime_and_manga_setting_desc.toStringResource()
            )

            ShowIfLoggedIn {
                SettingItem(
                    icon = R.drawable.ic_list,
                    title = R.string.lists.toStringResource(),
                    subtitle = R.string.list_setting_desc.toStringResource()
                )

                SettingItem(
                    icon = R.drawable.ic_notification,
                    title = R.string.notifications.toStringResource(),
                    subtitle = R.string.notifications_setting_desc.toStringResource()
                )
            }

            SettingItem(
                icon = R.drawable.ic_filter,
                title = R.string.filter.toStringResource(),
                subtitle = R.string.filter_desc.toStringResource()
            )


            SettingItem(
                icon = R.drawable.ic_info_outline,
                title = R.string.about.toStringResource(),
                subtitle = R.string.about_desc.toStringResource()
                    .format(versionName)
            )

            ShowIfLoggedIn {
                SettingItem(
                    icon = R.drawable.ic_logout,
                    title = R.string.logout.toStringResource(),
                    iconTint = logout_color
                ) {
                    openLogoutDialog.value = true
                }
            }
        }
    }


    ConfirmationDialog(
        openDialog = openLoginDialog,
        message = stringResource(id = R.string.login_signup_notice),
        title = stringResource(id = R.string.important_to_know)
    ) {
        login(context)
    }


    ConfirmationDialog(
        openDialog = openRegisterDialog,
        message = stringResource(id = R.string.login_signup_notice),
        title = stringResource(id = R.string.important_to_know)
    ) {
        register(context)
    }

    ConfirmationDialog(
        openDialog = openLogoutDialog,
        message = stringResource(id = R.string.are_you_sure_you_want_to_log_out),
        title = stringResource(id = R.string.logout)
    ) {
        scope.launch {
            context.logout()
        }
    }

}

private fun register(context: Context) {
    context.openLink(Config.SIGN_UP_URL)
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

