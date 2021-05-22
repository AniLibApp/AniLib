package com.revolgenx.anilib.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.common.preference.enableAutoMlTranslation
import com.revolgenx.anilib.common.preference.enableMlTranslation
import com.revolgenx.anilib.common.preference.inUseMlLanguageModel
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.databinding.TranslationSettingFragmentLayoutBinding
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink

class TranslationSettingFragment:BaseToolbarFragment<TranslationSettingFragmentLayoutBinding>() {

    override var titleRes: Int? = R.string.translation_setting
    override var setHomeAsUp: Boolean = true
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): TranslationSettingFragmentLayoutBinding {
        return TranslationSettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.miscTranslateOption.setOnClickListener {
            ToolbarContainerActivity.openActivity(
                requireContext(),
                ParcelableFragment(
                    MlLanguageChooserFragment::class.java,
                    null
                )
            )
        }

        binding.translateLayout.setOnClickListener {
            requireContext().openLink(getString(R.string.translate_link))
        }

        binding.enableMiscTranslateOption.isChecked = enableMlTranslation(requireContext())
        binding.enableAutoTranslation.isChecked = enableAutoMlTranslation(requireContext())

        binding.miscTranslateOption.isEnabled = enableMlTranslation(requireContext())
        binding.enableAutoTranslation.isEnabled = enableMlTranslation(requireContext())

        binding.enableMiscTranslateOption.setOnCheckedChangeListener { _, isChecked ->
            binding.miscTranslateOption.isEnabled = isChecked
            binding.enableAutoTranslation.isEnabled = isChecked
            enableMlTranslation(requireContext(), isChecked)
        }

        binding.enableAutoTranslation.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                if(inUseMlLanguageModel(requireContext()).isNotEmpty()){
                    enableAutoMlTranslation(requireContext(), isChecked)
                }else{
                    makeToast(R.string.select_a_language_model)
                    binding.enableAutoTranslation.isChecked = false
                }
            }else{
                enableAutoMlTranslation(requireContext(), isChecked)
            }
        }
    }
}