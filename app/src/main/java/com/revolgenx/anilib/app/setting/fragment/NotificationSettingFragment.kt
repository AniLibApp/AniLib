package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.NotificationSettingFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.NotificationType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.app.setting.viewmodel.NotificationSettingViewModel
import com.revolgenx.anilib.notification.data.field.UserNotificationMutateField
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.notificationSettings.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.notificationSettingProgressBar.root.visibility = View.GONE
                    it.data?.let {
                        binding.updateView(it)
                    }
                }
                Status.ERROR -> {
                    binding.notificationSettingProgressBar.root.visibility = View.GONE
                    makeToast(R.string.error)
                }
                Status.LOADING -> {
                    binding.notificationSettingProgressBar.root.visibility = View.VISIBLE
                }
            }
        }

        if (savedInstanceState == null) {
            if (requireContext().loggedIn()) {
                with(viewModel.field) {
                    userId = requireContext().userId()
                }
            }
            viewModel.getNotificationSettings()
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
        }).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.notificationSettingProgressBar.root.visibility = View.GONE
                    makeToast(R.string.saved_successfully)
                }
                Status.ERROR -> {
                    binding.notificationSettingProgressBar.root.visibility = View.GONE
                    makeToast(R.string.error_occured_while_saving)
                }
                Status.LOADING -> {
                    binding.notificationSettingProgressBar.root.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun NotificationSettingFragmentLayoutBinding.updateView(data: Map<NotificationType, Boolean>) {
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

        notificationTypesEnabled[NotificationType.RELATED_MEDIA_ADDITION] = mediaRelatedToMe.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_DATA_CHANGE] = mediaModifiedInMyList.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_MERGE] = mediaMergedInMyList.isChecked
        notificationTypesEnabled[NotificationType.MEDIA_DELETION] = mediaDeletedInMyList.isChecked

        return notificationTypesEnabled
    }

}