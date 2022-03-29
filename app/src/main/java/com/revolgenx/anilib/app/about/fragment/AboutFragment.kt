package com.revolgenx.anilib.app.about.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.pranavpandey.android.dynamic.theme.Theme
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.AboutFragmentLayoutBinding


class AboutFragment : BaseLayoutFragment<AboutFragmentLayoutBinding>() {

    override var titleRes: Int? = R.string.app_name
    override var setHomeAsUp: Boolean = true

    override fun getSubtitle(): CharSequence? {
        return DynamicPackageUtils.getVersionName(requireContext())
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AboutFragmentLayoutBinding {
        return AboutFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    private val getPages: List<BaseFragment> by lazy{ listOf(AppInfoFragment(), LicensesFragment(), TranslatorsFragment())}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            aboutTabLayout.setupWithViewPager(aboutFragmentViewpager)
            aboutFragmentViewpager.adapter = makePagerAdapter(getPages,requireContext().resources.getStringArray(R.array.about_tab_titles))
        }
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

}
