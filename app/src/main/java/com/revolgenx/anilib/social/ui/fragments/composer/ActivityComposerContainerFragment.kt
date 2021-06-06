package com.revolgenx.anilib.social.ui.fragments.composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerContainerFragmentLayoutBinding
import com.revolgenx.anilib.social.ui.viewmodel.composer.ActivityComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ActivityComposerContainerFragment :
    BaseLayoutFragment<ActivityComposerContainerFragmentLayoutBinding>() {

    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.activity_composer
    override val menuRes: Int = R.menu.activity_composer_fragment_menu

    private val viewModel by sharedViewModel<ActivityComposerViewModel>()


    private val activityComposerFragments by lazy {
        listOf(
            ActivityComposerFragment(),
            ActivityComposerPreviewFragment()
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerContainerFragmentLayoutBinding {
        return ActivityComposerContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            activityComposerViewPager.adapter = makePagerAdapter(activityComposerFragments, resources.getStringArray(R.array.activity_composer_tab_entries))
            activityComposerTabLayout.setupWithViewPager(activityComposerViewPager)
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_composer_publish -> {

                true
            }
            else -> false
        }
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.activityComposerToolbar
    }

}