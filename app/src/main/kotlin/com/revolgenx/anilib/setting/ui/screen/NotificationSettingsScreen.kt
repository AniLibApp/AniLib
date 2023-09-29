package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.NotificationSettingsViewModel
import com.revolgenx.anilib.type.NotificationType
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class NotificationSettingsScreen : PreferenceScreen() {

    override val titleRes: Int = I18nR.string.notifications
    override var actions: @Composable() (RowScope.() -> Unit)? = {
        val viewModel: NotificationSettingsViewModel = koinViewModel()
        ActionMenu(
            icon = AppIcons.IcSave
        ) {
            viewModel.save()
        }
    }

    @Composable
    override fun PreferenceContent() {
        val viewModel: NotificationSettingsViewModel = koinViewModel()
        ResourceScreen(viewModel = viewModel, loading = viewModel.loading) {
            SaveResourceState(viewModel)
            super.PreferenceContent()
        }
    }

    @Composable
    private fun SaveResourceState(viewModel: NotificationSettingsViewModel) {
        val snackbar = localSnackbarHostState()
        when (viewModel.saveResource.value) {
            is ResourceState.Error -> {
                val failedToSave = stringResource(id = I18nR.string.failed_to_save)
                val retry = stringResource(id = I18nR.string.retry)
                LaunchedEffect(viewModel) {
                    when (snackbar.showSnackbar(
                        failedToSave, retry, duration = SnackbarDuration.Long
                    )) {
                        SnackbarResult.Dismissed -> {
                            viewModel.saveResource.value = null
                        }

                        SnackbarResult.ActionPerformed -> {
                            viewModel.save()
                        }
                    }
                }
            }

            else -> {}
        }
    }

    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val viewModel: NotificationSettingsViewModel = koinViewModel()
        val resourceValue = viewModel.resource.value?.stateValue ?: emptyMap()
        return listOf(
            getActivitySubscriptionsGroup(resourceValue),
            getSocialGroup(resourceValue),
            getSiteDataChangesGroup(resourceValue)
        )
    }

    @Composable
    private fun getActivitySubscriptionsGroup(
        map: Map<NotificationType, MutableState<Boolean>>,
    ): PreferenceModel.PreferenceGroup {
        val activitySubs = listOf(
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.automatically_subscribe_me_to_activity_i_create),
                prefState = map[NotificationType.ACTIVITY_REPLY],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_REPLY]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.automatically_subscribe_me_to_activity_i_reply_to),
                prefState = map[NotificationType.ACTIVITY_REPLY_SUBSCRIBED],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_REPLY_SUBSCRIBED]?.value = it
                    true
                }
            )
        )

        return PreferenceModel.PreferenceGroup(
            title = stringResource(id = I18nR.string.activity_subscriptions),
            preferenceItems = activitySubs
        )
    }


    @Composable
    private fun getSocialGroup(map: Map<NotificationType, MutableState<Boolean>>): PreferenceModel.PreferenceGroup {
        val social = listOf(
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.when_someone_follows_me),
                prefState = map[NotificationType.FOLLOWING],
                onValueChanged = {
                    map[NotificationType.FOLLOWING]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.receive_message_notif),
                prefState = map[NotificationType.ACTIVITY_MESSAGE],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_MESSAGE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.mention_replay_notif),
                prefState = map[NotificationType.ACTIVITY_MENTION],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_MENTION]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.activity_like_notif),
                prefState = map[NotificationType.ACTIVITY_LIKE],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_LIKE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.likes_activity_reply_notif),
                prefState = map[NotificationType.ACTIVITY_REPLY_LIKE],
                onValueChanged = {
                    map[NotificationType.ACTIVITY_REPLY_LIKE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.forum_reply_notif),
                prefState = map[NotificationType.THREAD_COMMENT_REPLY],
                onValueChanged = {
                    map[NotificationType.THREAD_COMMENT_REPLY]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.forum_mention_notif),
                prefState = map[NotificationType.THREAD_COMMENT_MENTION],
                onValueChanged = {
                    map[NotificationType.THREAD_COMMENT_MENTION]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.forum_coment_liked_notif),
                prefState = map[NotificationType.THREAD_COMMENT_LIKE],
                onValueChanged = {
                    map[NotificationType.THREAD_COMMENT_LIKE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.reply_to_subscribed_forum_notif),
                prefState = map[NotificationType.THREAD_SUBSCRIBED],
                onValueChanged = {
                    map[NotificationType.THREAD_SUBSCRIBED]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.forum_liked_notif),
                prefState = map[NotificationType.THREAD_LIKE],
                onValueChanged = {
                    map[NotificationType.THREAD_LIKE]?.value = it
                    true
                }
            )
        )

        return PreferenceModel.PreferenceGroup(
            title = stringResource(id = I18nR.string.social),
            preferenceItems = social
        )
    }

    @Composable
    fun getSiteDataChangesGroup(map: Map<NotificationType, MutableState<Boolean>>): PreferenceModel.PreferenceGroup {
        val siteDataChanges = listOf(
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.media_related_to_me),
                prefState = map[NotificationType.RELATED_MEDIA_ADDITION],
                onValueChanged = {
                    map[NotificationType.RELATED_MEDIA_ADDITION]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.media_modified_in_my_list),
                prefState = map[NotificationType.MEDIA_DATA_CHANGE],
                onValueChanged = {
                    map[NotificationType.MEDIA_DATA_CHANGE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.media_merged_in_my_list),
                prefState = map[NotificationType.MEDIA_MERGE],
                onValueChanged = {
                    map[NotificationType.MEDIA_MERGE]?.value = it
                    true
                }
            ),
            PreferenceModel.SwitchPreference(
                title = stringResource(id = I18nR.string.media_deleted_in_my_list),
                prefState = map[NotificationType.MEDIA_DELETION],
                onValueChanged = {
                    map[NotificationType.MEDIA_DELETION]?.value = it
                    true
                }
            ),

            )

        return PreferenceModel.PreferenceGroup(
            title = stringResource(id = I18nR.string.site_data_changes),
            preferenceItems = siteDataChanges
        )
    }

}