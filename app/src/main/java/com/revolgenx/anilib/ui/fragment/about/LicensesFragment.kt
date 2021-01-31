package com.revolgenx.anilib.ui.fragment.about

import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.LicenseFragmentLayoutBinding

class LicensesFragment : BaseLayoutFragment<LicenseFragmentLayoutBinding>() {
    override fun bindView(inflater: LayoutInflater, parent: ViewGroup?): LicenseFragmentLayoutBinding {
        return LicenseFragmentLayoutBinding.inflate(inflater, parent, false)
    }
}
