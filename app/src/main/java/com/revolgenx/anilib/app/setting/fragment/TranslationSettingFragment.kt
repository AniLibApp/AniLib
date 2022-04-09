package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.enableAutoMlTranslation
import com.revolgenx.anilib.common.preference.enableMlTranslation
import com.revolgenx.anilib.common.preference.inUseMlLanguageModel
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.TranslationSettingFragmentLayoutBinding
import com.revolgenx.anilib.common.event.OpenSettingEvent
import com.revolgenx.anilib.common.event.SettingEventTypes
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
            OpenSettingEvent(SettingEventTypes.LANGUAGE_CHOOSER).postEvent
        }

        binding.translateLayout.setOnClickListener {
            requireContext().openLink(getString(R.string.translate_link))
        }

        binding.enableMiscTranslateOption.isChecked = enableMlTranslation()
        binding.enableAutoTranslation.isChecked = enableAutoMlTranslation()

        binding.miscTranslateOption.isEnabled = enableMlTranslation()
        binding.enableAutoTranslation.isEnabled = enableMlTranslation()

        binding.enableMiscTranslateOption.setOnCheckedChangeListener { _, isChecked ->
            binding.miscTranslateOption.isEnabled = isChecked
            binding.enableAutoTranslation.isEnabled = isChecked
            enableMlTranslation(isChecked)
        }

        binding.enableAutoTranslation.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                if(inUseMlLanguageModel(requireContext()).isNotEmpty()){
                    enableAutoMlTranslation(isChecked)
                }else{
                    makeToast(R.string.select_a_language_model)
                    binding.enableAutoTranslation.isChecked = false
                }
            }else{
                enableAutoMlTranslation(isChecked)
            }
        }
    }
}