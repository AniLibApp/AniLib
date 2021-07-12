package com.revolgenx.anilib.social.ui.fragments.activity_composer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.ActivityComposerContainerFragmentLayoutBinding
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityComposerViewModel
import com.revolgenx.anilib.ui.view.makeToast

abstract class ActivityComposerContainerFragment<VM:ActivityComposerViewModel<*,*,*>> :
    BaseLayoutFragment<ActivityComposerContainerFragmentLayoutBinding>() {

    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.composer
    override val menuRes: Int = R.menu.activity_composer_fragment_menu

    protected var rotating = false

    protected abstract val activityComposerFragments:List<BaseFragment>

    protected abstract val viewModel:VM
    protected abstract val tabEntries:Array<String>

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ActivityComposerContainerFragmentLayoutBinding {
        return ActivityComposerContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            activityComposerViewPager.adapter = makePagerAdapter(activityComposerFragments, tabEntries)
            activityComposerTabLayout.setupWithViewPager(activityComposerViewPager)
        }
    }

    override fun onResume() {
        super.onResume()
        rotating = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rotating = true
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_composer_publish -> {
                if(viewModel.text.isBlank()){
                    makeToast(R.string.field_is_empty)
                }else{
                    publish()
                }
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        binding.activityComposerViewPager.adapter = null
        if(!rotating){
            viewModel.activeModel = null
            viewModel.resetField()
            viewModel.text = ""
        }
        super.onDestroyView()
    }

    abstract fun publish()

    override fun getBaseToolbar(): Toolbar {
        return binding.activityComposerToolbar
    }

}