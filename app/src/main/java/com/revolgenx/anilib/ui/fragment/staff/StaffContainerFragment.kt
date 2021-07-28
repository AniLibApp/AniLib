package com.revolgenx.anilib.ui.fragment.staff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.StaffContainerFragmentLayoutBinding
import com.revolgenx.anilib.ui.viewmodel.staff.StaffContainerViewModel
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.view.iterator
import androidx.core.view.forEachIndexed

class StaffContainerFragment:BaseLayoutFragment<StaffContainerFragmentLayoutBinding>() {

    override val setHomeAsUp: Boolean =true
    override val menuRes: Int = R.menu.staff_fragment_menu

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): StaffContainerFragmentLayoutBinding {
        return StaffContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    private val staffFragments by lazy {
        listOf(
            StaffFragment.newInstance(staffId!!),
            StaffMediaCharacterFragment.newInstance(staffId!!),
            StaffMediaRoleFragment.newInstance(staffId!!)
        )
    }

    private val staffId get() = arguments?.getInt(STAFF_ID_KEY)
    private val viewModel by viewModel<StaffContainerViewModel>()

    companion object {
        private const val STAFF_ID_KEY = "STAFF_ID_KEY"
        fun newInstance(staffId: Int) = StaffContainerFragment().also {
            it.arguments = bundleOf(STAFF_ID_KEY to staffId)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        staffId ?: return
        updateToolbarTitle(viewModel.title ?: requireContext().getString(R.string.staff))
        binding.staffViewPager.adapter = makePagerAdapter(staffFragments)
        binding.staffViewPager.offscreenPageLimit = 2
        binding.staffBottomNav.inflateMenu(R.menu.staff_nav_menu)

        binding.staffBottomNav.setOnNavigationItemSelectedListener {
            binding.staffBottomNav.menu.forEachIndexed { index, item ->
                if (it == item) {
                    binding.staffViewPager.setCurrentItem(index, true)
                }
            }
            false
        }

        binding.staffViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                binding.staffBottomNav.menu.iterator().forEach {
                    it.isChecked = false
                }
                binding.staffBottomNav.menu.getItem(position).isChecked = true
            }
        })
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.staffToolbar.dynamicToolbar
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.staff_share_menu -> {
                requireContext().openLink(viewModel.characterLink)
                true
            }
            else -> {
                false
            }
        }
    }

    fun updateToolbarTitle(title: String) {
        viewModel.title = title
        getBaseToolbar().title = title
    }

    fun updateShareableLink(link: String?) {
        viewModel.characterLink = link
    }
}