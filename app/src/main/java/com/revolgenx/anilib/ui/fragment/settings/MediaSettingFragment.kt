package com.revolgenx.anilib.ui.fragment.settings

import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.canShowAdult
import com.revolgenx.anilib.common.preference.userEnabledAdultContent
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.field.setting.MediaSettingMutateField
import com.revolgenx.anilib.data.model.setting.MediaOptionModel
import com.revolgenx.anilib.databinding.MediaSettingFragmentBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.setting.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaSettingFragment : BaseLayoutFragment<MediaSettingFragmentBinding>() {

    override var setHomeAsUp: Boolean = true
    override var titleRes: Int? = R.string.anilist_and_manga

    private val viewModel by viewModel<SettingViewModel>()

    private val titleList by lazy {
        requireContext().resources.getStringArray(R.array.title_language_list).map {
            DynamicSpinnerItem(null, it)
        }
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaSettingFragmentBinding {
        return MediaSettingFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val languageListAdapter = makeSpinnerAdapter(requireContext(), titleList)
        binding.titleLanguageSpinner.spinnerView.adapter = languageListAdapter

        viewModel.mediaOptionLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.showLoading(false)
                    val data = it.data ?: return@observe
                    binding.updateView(data)
                }
                Status.ERROR -> {
                    binding.showLoading(false)
                    makeToast(R.string.something_went_wrong, icon = R.drawable.ads_ic_info)
                }
                Status.LOADING -> {
                    binding.showLoading(true)
                    binding.updateView(it.data!!)
                }
            }
        }

        if (savedInstanceState == null) {
            with(viewModel.mediaSettingField) {
                userId = requireContext().userId()
            }
            viewModel.getMediaSetting(requireContext())
        }
    }

    private fun MediaSettingFragmentBinding.showLoading(b: Boolean) {
        mediaSettingProgressBar.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun MediaSettingFragmentBinding.updateView(data: MediaOptionModel) {
        titleLanguageSpinner.spinnerView.setSelection(data.titleLanguage)
        airingAnimeNotificationSwitch.isChecked = data.airingNotifications
        if (data.displayAdultContent) {
            adultContentSwitch.visibility = View.VISIBLE
        } else {
            adultContentSwitch.visibility = View.GONE
        }
    }

    private fun MediaSettingFragmentBinding.saveMediaSetting() {
        val model = viewModel.mediaOptionLiveData.value?.data ?: return
        model.airingNotifications = airingAnimeNotificationSwitch.isChecked
        model.titleLanguage = titleLanguageSpinner.spinnerView.selectedItemPosition
        viewModel.setMediaSetting(requireContext(), MediaSettingMutateField(model))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.showLoading(false)
                        makeToast(R.string.saved_successfully)
                    }
                    Status.ERROR -> {
                        binding.showLoading(false)
                        makeToast(R.string.error_occured_while_saving)
                    }
                    Status.LOADING -> {
                        binding.showLoading(true)
                    }
                }
            }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_menu -> {
                binding.saveMediaSetting()
                true
            }
            else -> {
                false
            }
        }
    }

}