package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.NotificationSettingsViewModel
import com.revolgenx.anilib.type.NotificationType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

object NotificationSettingsScreen : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: NotificationSettingsViewModel = koinViewModel()

        ScreenScaffold(
            title = stringResource(id = R.string.notifications),
            actions = {

                ActionMenu(
                    icon = AppIcons.IcSave
                ) {
                    viewModel.save()
                }
            },
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ResourceScreen(viewModel = viewModel) {
                    NotificationScreenContent(viewModel = viewModel)
                }
            }
        }
    }

}

@Composable
private fun NotificationScreenContent(viewModel: NotificationSettingsViewModel) {
    val resourceValue = viewModel.resource.value?.stateValue ?: emptyMap()
    NotificationRefreshInterval(viewModel)
    ActivitySubscriptionsGroup(resourceValue)
    SocialGroup(resourceValue)
    SiteDataChangesGroup(resourceValue)
}

@Composable
private fun NotificationRefreshInterval(viewModel: NotificationSettingsViewModel) {
    val context = localContext()
    val scope = rememberCoroutineScope()

    GroupPreferenceItem(title = stringResource(id = I18nR.string.refresh_interval)) {
        val notificationIntervalPref =
            viewModel.generalPreferencesDataStore.notificationRefreshInterval
        val notificationInterval = notificationIntervalPref.collectAsState().value
        val notificationEntry = remember {
            listOf(15, 20, 25, 30).map {
                ListPreferenceEntry(context.getString(R.string.s_min).format(it), it)
            }
        }
        ListPreferenceItem(
            value = notificationInterval,
            title = stringResource(id = R.string.notification_refresh_interval),
            entries = notificationEntry
        ) {
            scope.launch {
                notificationIntervalPref.set(it!!)
            }
        }
    }
}

@Composable
private fun ActivitySubscriptionsGroup(
    map: Map<NotificationType, MutableState<Boolean>>,
) {
    GroupPreferenceItem(title = stringResource(id = I18nR.string.activity_subscriptions)) {
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.automatically_subscribe_me_to_activity_i_create),
            checked = map[NotificationType.ACTIVITY_REPLY]?.value == true
        ) {
            map[NotificationType.ACTIVITY_REPLY]?.value = it
        }

        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.automatically_subscribe_me_to_activity_i_reply_to),
            checked = map[NotificationType.ACTIVITY_REPLY_SUBSCRIBED]?.value == true
        ) {
            map[NotificationType.ACTIVITY_REPLY_SUBSCRIBED]?.value = it
        }
    }
}


@Composable
private fun SocialGroup(map: Map<NotificationType, MutableState<Boolean>>) {
    GroupPreferenceItem(title = stringResource(id = I18nR.string.social)) {
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.when_someone_follows_me),
            checked = map[NotificationType.FOLLOWING]?.value == true
        ) {
            map[NotificationType.FOLLOWING]?.value = it
        }

        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.receive_message_notif),
            checked = map[NotificationType.ACTIVITY_MESSAGE]?.value == true
        ) {
            map[NotificationType.ACTIVITY_MESSAGE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.mention_replay_notif),
            checked = map[NotificationType.ACTIVITY_MENTION]?.value == true
        ) {
            map[NotificationType.ACTIVITY_MENTION]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.activity_like_notif),
            checked = map[NotificationType.ACTIVITY_LIKE]?.value == true
        ) {
            map[NotificationType.ACTIVITY_LIKE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.likes_activity_reply_notif),
            checked = map[NotificationType.ACTIVITY_REPLY_LIKE]?.value == true
        ) {
            map[NotificationType.ACTIVITY_REPLY_LIKE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.forum_reply_notif),
            checked = map[NotificationType.THREAD_COMMENT_REPLY]?.value == true
        ) {
            map[NotificationType.THREAD_COMMENT_REPLY]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.forum_mention_notif),
            checked = map[NotificationType.THREAD_COMMENT_MENTION]?.value == true
        ) {
            map[NotificationType.THREAD_COMMENT_MENTION]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.forum_coment_liked_notif),
            checked = map[NotificationType.THREAD_COMMENT_LIKE]?.value == true
        ) {
            map[NotificationType.THREAD_COMMENT_LIKE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.reply_to_subscribed_forum_notif),
            checked = map[NotificationType.THREAD_SUBSCRIBED]?.value == true
        ) {
            map[NotificationType.THREAD_SUBSCRIBED]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.forum_liked_notif),
            checked = map[NotificationType.THREAD_LIKE]?.value == true
        ) {
            map[NotificationType.THREAD_LIKE]?.value = it
        }
    }

}

@Composable
fun SiteDataChangesGroup(map: Map<NotificationType, MutableState<Boolean>>) {
    GroupPreferenceItem(title = stringResource(id = I18nR.string.site_data_changes)) {

        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.media_related_to_me),
            checked = map[NotificationType.RELATED_MEDIA_ADDITION]?.value == true
        ) {
            map[NotificationType.RELATED_MEDIA_ADDITION]?.value = it
        }

        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.media_modified_in_my_list),
            checked = map[NotificationType.MEDIA_DATA_CHANGE]?.value == true
        ) {
            map[NotificationType.MEDIA_DATA_CHANGE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.media_merged_in_my_list),
            checked = map[NotificationType.MEDIA_MERGE]?.value == true
        ) {
            map[NotificationType.MEDIA_MERGE]?.value = it
        }
        SwitchPreferenceItem(
            title = stringResource(id = I18nR.string.media_deleted_in_my_list),
            checked = map[NotificationType.MEDIA_DELETION]?.value == true
        ) {
            map[NotificationType.MEDIA_DELETION]?.value = it
        }

    }
}
