package com.revolgenx.anilib.social.ui.fragments.activity_composer.message

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.event.OnActivityInfoUpdateEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.social.data.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerContainerFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import com.revolgenx.anilib.ui.view.makeToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityMessageComposerContainerFragment :
    ActivityComposerContainerFragment<ActivityMessageComposerViewModel>() {


    companion object {
        private const val MESSAGE_RECIPIENT_ID_KEY = "MESSAGE_RECIPIENT_ID_KEY"
        fun newInstance(recipientId: Int) = ActivityMessageComposerContainerFragment().also {
            it.arguments = bundleOf(MESSAGE_RECIPIENT_ID_KEY to recipientId)
        }
    }

    private val recipientId get() = arguments?.getInt(MESSAGE_RECIPIENT_ID_KEY)
    override val viewModel by sharedViewModel<ActivityMessageComposerViewModel>()
    override val tabEntries: Array<String>
        get() = resources.getStringArray(R.array.message_composer_tab_entries)

    override val menuRes: Int = R.menu.message_composer_fragment_menu

    override val activityComposerFragments: List<BaseFragment> by lazy {
        listOf(
            ActivityMessageComposerFragment(),
            ActivityMessageComposerPreviewFragment()
        )
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.private_message_menu -> {
                viewModel.field.private = !viewModel.field.private
                togglePrivateMenu(viewModel.field.private)
                true
            }
            R.id.message_composer_publish_menu -> {
                if (viewModel.message.isBlank()) {
                    makeToast(R.string.field_is_empty)
                } else {
                    publish()
                }
                true
            }
            else -> false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.field.private =
                (viewModel.activeModel as? MessageActivityModel)?.isPrivate == true
        }
    }

    private fun togglePrivateMenu(isPrivate: Boolean) {
        val privateMessageMenu = getBaseToolbar().menu.findItem(R.id.private_message_menu)
        privateMessageMenu.icon = ContextCompat.getDrawable(
            requireContext(),
            if (isPrivate) R.drawable.ic_private else R.drawable.ic_eye
        )
    }

    override fun onToolbarInflated() {
        togglePrivateMenu(viewModel.field.private)
    }

    override fun publish() {
        viewModel.field.recipientId = recipientId ?: return
        this.viewModel.saveActivity {
            when (it) {
                is Resource.Error -> {
                    makeToast(R.string.operation_failed)
                }
                is Resource.Loading -> {
                    makeToast(R.string.sending)
                }
                is Resource.Success -> {
                    if (this.viewModel.field.id == null) {
                        makeToast(R.string.sent_successfully)
                    } else {
                        makeToast(R.string.updated)
                    }
                    OnActivityInfoUpdateEvent(it.data!!).postEvent
                    popBackStack()
                }
            }
        }
    }

}