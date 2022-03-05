package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.NotificationSettingFragmentLayoutBinding
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.app.setting.viewmodel.NotificationSettingViewModel
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.notification.data.field.UserNotificationMutateField
import com.revolgenx.anilib.ui.view.makeErrorToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationSettingFragment :
    BaseToolbarFragment<NotificationSettingFragmentLayoutBinding>() {
    override var titleRes: Int? = R.string.notification_setting
    override var setHomeAsUp: Boolean = true
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    private val viewModel by viewModel<NotificationSettingViewModel>()
    override val menuRes: Int = R.menu.save_menu

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): NotificationSettingFragmentLayoutBinding {
        return NotificationSettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.saveNotifSettingLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    clearStatus()
                    makeErrorToast(R.string.failed_to_save)
                }
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    clearStatus()
                    makeToast(R.string.saved_successfully)
                }
            }
        }
        viewModel.notificationSettings.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    clearStatus()
                    it.data?.let {
                        binding.updateView(it)
                    }
                }
                is Resource.Error -> {
                    showError()
                    makeErrorToast(R.string.error)
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.field.userId = UserPreference.userId
            viewModel.getNotificationSettings()
        }
    }

    private fun showLoading() {
        binding.resourceStatusLayout.apply {
            resourceProgressLayout.progressLayout.visibility = View.VISIBLE
            resourceErrorLayout.errorLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        binding.resourceStatusLayout.apply {
            resourceErrorLayout.errorLayout.visibility = View.VISIBLE
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.VISIBLE
        }
    }

    private fun clearStatus() {
        binding.resourceStatusLayout.apply {
            resourceErrorLayout.errorLayout.visibility = View.GONE
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.GONE
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.save_menu) {
            saveNotificationSetting()
            true
        } else false
    }


    private fun saveNotificationSetting() {
        val notificationSettings = binding.getNotificationSettingsFromView()
        viewModel.saveNotificationSetting(UserNotificationMutateField().also {
            it.notificationSettings = notificationSettings
        })
    }

    override fun updateToolbar() {
        super.updateToolbar()
        updateNotificationToolbar()
    }

    private fun updateNotificationToolbar() {
        getBaseToolbar().menu.findItem(R.menu.save_menu)?.isVisible =
            viewModel.notificationSettings.value?.data?.isNotEmpty() == true
    }

    private fun NotificationSettingFragmentLayoutBinding.updateView(data: Map<NotificationType, Boolean>) {
        notificationSettingLayout.visibility = View.VISIBLE
        updateNotificationToolbar()
        data.forEach { t ->
            val isChecked = t.value
            val checkbox = when (t.key) {
                NotificationType.ACTIVITY_MESSAGE -> receiveMessageCb
                NotificationType.ACTIVITY_REPLY -> subscribeActivityCreate
                NotificationType.ACTIVITY_REPLY_SUBSCRIBED -> subscribeActivityReply
                NotificationType.FOLLOWING -> followsMeCb
                NotificationType.ACTIVITY_MENTION -> mentionedCb
                NotificationType.THREAD_COMMENT_MENTION -> mentionForumCommentCb
                NotificationType.THREAD_SUBSCRIBED -> replyForumSubscribedCb
                NotificationType.THREAD_COMMENT_REPLY -> replyForumCommentCb
                NotificationType.ACTIVITY_LIKE -> likeActivityCb
                NotificationType.ACTIVITY_REPLY_LIKE -> likeActivityReplyCb
                NotificationType.THREAD_LIKE -> likeForumThreadCb
                NotificationType.THREAD_COMMENT_LIKE -> likeForumCommentCb
                NotificationType.RELATED_MEDIA_ADDITION -> mediaRelatedToMe
                NotificationType.MEDIA_DATA_CHANGE -> mediaModifiedInMyList
                NotificationType.MEDIA_MERGE -> mediaMergedInMyList
                NotificationType.MEDIA_DELETION -> mediaDeletedInMyList
                else -> null
            }
            checkbox?.isChecked = isChecked
        }
    }


    private fun NotificationSettingFragmentLayoutBinding.getNotificationSettingsFromView(): Map<NotificationType, Boolean> {
        val notificationTypesEnabled = mutableMapOf<NotificationType, Boolean>()

        notificationTypesEnabled[NotificationType.ACTIVITY_MESSAGE] = receiveMessageCb.isChecked
        notificationTypesEnabled[NotificationType.ACTIVITY_REPLY] =
            subscribeActivityCreate.isChecked
        notificationTypesEnabled[NotificationType.ACTIVITY_REPLY_SUBSCRIBED] =
            subscribeActivityReply.isChecked
        notificationTypesEnabled[NotificationType.FOLLOWING] = followsMeCb.isChecked
        notificationTypesEnabled[NotificationType.ACTIVITY_MENTION] = mentionedCb.isChecked
        notificationTypesEnabled[NotificationType.THREAD_COMMENT_MENTION] =
            mentionForumCommentCb.isChecked
        notificationTypesEnabled[NotificationType.THREAD_SUBSCRIBED] =
            replyForumSubscribedCb.isChecked
        notificationTypesEnabled[NotificationType.THREAD_COMMENT_REPLY] =
            replyForumCommentCb.isChecked
        notificationTypesEnabled[NotificationType.ACTIVITY_LIKE] = likeActivityCb.isChecked
        notificationTypesEnabled[NotificationType.ACTIVITY_REPLY_LIKE] =
            likeActivityReplyCb.isChecked
        notificationTypesEnabled[NotificationType.THREAD_LIKE] = likeForumThreadCb.isChecked
        notificationTypesEnabled[NotificationType.THREAD_COMMENT_LIKE] =
            likeForumCommentCb.isChecked

        notificationTypesEnabled[NotificationType.RELATED_MEDIA_ADDITION] =
            mediaRelatedToMe.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_DATA_CHANGE] =
            mediaModifiedInMyList.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_MERGE] = mediaMergedInMyList.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_DELETION] = mediaDeletedInMyList.isChecked

        return notificationTypesEnabled
    }

}