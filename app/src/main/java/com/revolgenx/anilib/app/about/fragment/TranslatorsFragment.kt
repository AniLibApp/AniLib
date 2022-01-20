package com.revolgenx.anilib.app.about.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.TranslatorFragmentLayoutBinding

class TranslatorsFragment : BaseLayoutFragment<TranslatorFragmentLayoutBinding>() {
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): TranslatorFragmentLayoutBinding =
        TranslatorFragmentLayoutBinding.inflate(inflater, parent, false)
}
