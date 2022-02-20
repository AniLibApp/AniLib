package com.revolgenx.anilib.social.ui.fragments.activity_composer.text

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.event.OnActivityInfoUpdateEvent
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.social.ui.fragments.activity_composer.ActivityComposerContainerFragment
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import com.revolgenx.anilib.ui.view.makeToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityTextComposerContainerFragment :
    ActivityComposerContainerFragment<ActivityTextComposerViewModel>() {

    override val viewModel by sharedViewModel<ActivityTextComposerViewModel>()
    override val tabEntries: Array<String>
        get() = resources.getStringArray(R.array.activity_composer_tab_entries)

    override val activityComposerFragments: List<BaseFragment> by lazy {
        listOf(
            ActivityTextComposerFragment(),
            ActivityTextComposerPreviewFragment()
        )
    }

    override fun publish() {
        this.viewModel.saveActivity {
            when (it.status) {
                Status.SUCCESS -> {
                    if (this.viewModel.field.id == null) {
                        makeToast(R.string.posted_successfully)
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
                    makeToast(R.string.posting)
                }
            }
        }
    }

}