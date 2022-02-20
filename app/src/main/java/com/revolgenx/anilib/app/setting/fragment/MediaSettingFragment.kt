package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.databinding.MediaSettingFragmentBinding
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.app.setting.data.model.SettingViewModel
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.ui.view.makeErrorToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaSettingFragment : BaseToolbarFragment<MediaSettingFragmentBinding>() {

    override var setHomeAsUp: Boolean = true
    override var titleRes: Int? = R.string.anilist_and_manga
    override val noScrollToolBar: Boolean = true
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    private val viewModel by viewModel<SettingViewModel>()
    override val menuRes: Int = R.menu.save_menu

    private val titleList by lazy {
        requireContext().resources.getStringArray(R.array.title_language_list).map {
            DynamicMenu(null, it)
        }
    }

    override fun updateToolbar() {
        super.updateToolbar()
        updateSettingToolbar()
    }

    private fun updateSettingToolbar(){
        getBaseToolbar().menu.findItem(R.id.save_menu)?.isVisible = viewModel.mediaOptionLiveData.value?.data != null
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
                    clearStatus()
                    it.data?.let {
                        binding.updateView(it)
                    }
                }
                Status.ERROR -> {
                    showError()
                    makeToast(R.string.something_went_wrong, icon = R.drawable.ic_info)
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
        }

        viewModel.saveMediaOptionLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    clearStatus()
                    makeToast(R.string.saved_successfully)
                }
                Status.ERROR -> {
                    clearStatus()
                    makeErrorToast(R.string.error_occured_while_saving)
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.mediaSettingField.userId = UserPreference.userId
            viewModel.getMediaSetting(requireContext())
        }
    }


    private fun showLoading() {
        binding.resourceStatusLayout.apply {
            resourceProgressLayout.progressLayout.visibility = View.VISIBLE
            resourceErrorLayout.errorLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        binding.resourceStatusLayout.apply {
            resourceErrorLayout.errorLayout.visibility = View.VISIBLE
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.VISIBLE
        }
    }

    private fun clearStatus() {
        binding.resourceStatusLayout.apply {
            resourceErrorLayout.errorLayout.visibility = View.GONE
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceStatusContainer.visibility = View.GONE
        }
    }

    private fun MediaSettingFragmentBinding.updateView(data: UserOptionsModel) {
        mediaSettingContainerLayout.visibility = View.VISIBLE
        titleLanguageSpinner.spinnerView.setSelection(data.titleLanguage)
        airingAnimeNotificationSwitch.isChecked = data.airingNotifications
        if (data.displayAdultContent) {
            adultContentSwitch.visibility = View.VISIBLE
        } else {
            adultContentSwitch.visibility = View.GONE
        }
        updateSettingToolbar()
    }

    private fun MediaSettingFragmentBinding.saveMediaSetting() {
        val model = viewModel.mediaOptionLiveData.value?.data ?: return
        model.airingNotifications = airingAnimeNotificationSwitch.isChecked
        model.titleLanguage = titleLanguageSpinner.spinnerView.selectedItemPosition
        viewModel.setMediaSetting(requireContext(), MediaSettingMutateField(model))
    }


    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
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