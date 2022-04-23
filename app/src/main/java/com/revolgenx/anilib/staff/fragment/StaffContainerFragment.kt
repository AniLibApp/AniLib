package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.StaffContainerFragmentLayoutBinding
import com.revolgenx.anilib.staff.viewmodel.StaffContainerViewModel
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.util.shareText

class StaffContainerFragment : BaseLayoutFragment<StaffContainerFragmentLayoutBinding>() {
    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.staff_fragment_menu
    override val showMenuIcon: Boolean = true

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        staffId ?: return
        binding.staffViewPager.adapter = makeViewPagerAdapter2(staffFragments)

        val tabItems = listOf(
            R.string.about to R.drawable.ic_role,
            R.string.voice_role to R.drawable.ic_voice_role,
            R.string.staff_role to R.drawable.ic_staff
        )

        setupWithViewPager2(binding.staffTabLayout, binding.staffViewPager, tabItems)
        binding.staffViewPager.offscreenPageLimit = 2
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.staff_share_menu -> {
                shareText(viewModel.staffLink)
                true
            }
            R.id.staff_open_in_browser_menu->{
                openLink(viewModel.staffLink)
                true
            }
            else -> {
                false
            }
        }
    }
}