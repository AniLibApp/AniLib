package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.field.MediaSettingMutateField
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.databinding.MediaSettingFragmentBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.app.setting.data.model.SettingViewModel
import com.revolgenx.anilib.common.preference.UserPreference
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
        binding.titleLanguageSpinner.adapter = languageListAdapter

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
                userId = UserPreference.userId
            }
            viewModel.getMediaSetting(requireContext())
        }
    }

    private fun MediaSettingFragmentBinding.showLoading(b: Boolean) {
        mediaSettingProgressBar.visibility = if (b) View.VISIBLE else View.INVISIBLE
    }

    private fun MediaSettingFragmentBinding.updateView(data: UserOptionsModel) {
        titleLanguageSpinner.setSelection(data.titleLanguage)
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
        model.titleLanguage = titleLanguageSpinner.selectedItemPosition
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