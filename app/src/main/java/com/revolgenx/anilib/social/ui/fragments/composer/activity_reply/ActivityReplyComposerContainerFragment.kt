package com.revolgenx.anilib.social.ui.fragments.composer.activity_reply

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.infrastructure.event.OnActivityInfoUpdateEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.social.ui.fragments.composer.ActivityComposerContainerFragment
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityReplyComposerViewModel
import com.revolgenx.anilib.ui.view.makeToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityReplyComposerContainerFragment:ActivityComposerContainerFragment<ActivityReplyComposerViewModel>() {

    override val viewModel: ActivityReplyComposerViewModel by sharedViewModel()
    override val tabEntries: Array<String>
        get() = resources.getStringArray(R.array.reply_composer_tab_entries)

    override val activityComposerFragments: List<BaseFragment> by lazy {
        listOf(
            ActivityReplyComposerFragment(),
            ActivityReplyComposerPreviewFragment()
        )
    }

    override fun publish() {
        this.viewModel.saveActivity {
            when (it.status) {
                Status.SUCCESS -> {
                    if (this.viewModel.field.id == null) {
                        makeToast(R.string.replied_successfully)
                    } else {
                        makeToast(R.string.updated)
                    }
                    OnActivityInfoUpdateEvent(it.data!!).postEvent
                    popBackStack()
                }
                Status.ERROR -> {
                    makeToast(R.string.operation_failed)
                }
                Status.LOADING -> {
                    makeToast(R.string.replying)
                }
            }
        }
    }
}