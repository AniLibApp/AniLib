package com.revolgenx.anilib.app.setting.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.chip.Chip
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.extensions.SimplePresenter
import com.otaliastudios.elements.pagers.NoPagesPager
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.databinding.MediaListSettingFragmentBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.dialog.InputDialog
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.app.setting.viewmodel.MediaListSettingVM
import com.revolgenx.anilib.common.ListSource
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.databinding.AdvancedScoringLayoutBinding
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaListSettingFragment : BaseToolbarFragment<MediaListSettingFragmentBinding>() {
    override var setHomeAsUp: Boolean = true
    override var titleRes: Int? = R.string.list
    override val noScrollToolBar: Boolean = true
    override val menuRes: Int = R.menu.save_menu
    private val viewModel by viewModel<MediaListSettingVM>()

    private val field get() = viewModel.field
    private val saveField get() = viewModel.saveField

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListSettingFragmentBinding {
        return MediaListSettingFragmentBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.clearStatus()
                    binding.bind()
                }
                Status.ERROR -> {
                    binding.showError()
                    makeToast(R.string.something_went_wrong, icon = R.drawable.ic_error)
                }
                Status.LOADING -> {
                    binding.showLoading()
                }
            }
        }

        viewModel.saveListLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    makeToast(R.string.saved_successfully)
                    binding.clearStatus()
                }
                Status.ERROR -> {
                    binding.clearStatus()
                    makeErrorToast(R.string.failed_to_save)
                }
                Status.LOADING -> {
                    binding.showLoading()
                }
            }
        }

        if (savedInstanceState == null) {
            field.userId = UserPreference.userId
            viewModel.getMediaListSetting()
        }
    }

    private fun MediaListSettingFragmentBinding.bind() {
        mediaListSettingContainerLayout.visibility = View.VISIBLE
        val scoringSystemList =
            requireContext().resources.getStringArray(R.array.scoring_system_list).map {
                DynamicMenu(null, it)
            }

        val defaultSpinnerOrder =
            requireContext().resources.getStringArray(R.array.media_list_order_list).map {
                DynamicMenu(null, it)
            }

        val scoringAdapter = makeSpinnerAdapter(requireContext(), scoringSystemList)
        val listOrderAdapter = makeSpinnerAdapter(requireContext(), defaultSpinnerOrder)

        binding.scoringSystemSpinner.spinnerView.adapter = scoringAdapter
        binding.listOrderSpinner.spinnerView.adapter = listOrderAdapter

        saveField.scoreFormat?.let {
            scoringSystemSpinner.spinnerView.setSelection(it)
        }
        saveField.rowOrder?.let {
            listOrderSpinner.spinnerView.setSelection(it)
        }
        splitAnimeList.isChecked = saveField.splitCompletedAnimeListByFormat == true
        splitMangaList.isChecked = saveField.splitCompletedMangaListByFormat == true

        bindAdvanceScoring()
        bindCustomList()
        updateSettingToolbar()
        initListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun MediaListSettingFragmentBinding.initListener() {
        advanceScoreSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveField.advancedScoringEnabled = isChecked
            updateAdvanceScoreView()
        }
        advanceScoringAdd.setOnClickListener {
            advanceScoringAddEt.text?.toString()?.takeIf { it.isNotBlank() }?.let {
                saveField.advancedScoring?.add(it)
                advancedScoresRecyclerView.onTagAdded()
                advanceScoringAddEt.setText("")
            }
        }

        scoringSystemSpinner.spinnerView.onItemSelected {
            saveField.scoreFormat = it
            updateAdvanceScoreView()
        }

        listOrderSpinner.spinnerView.onItemSelected {
            saveField.rowOrder = it
        }

        splitAnimeList.setOnCheckedChangeListener { _, isChecked ->
            saveField.splitCompletedAnimeListByFormat = isChecked
        }

        splitMangaList.setOnCheckedChangeListener { _, isChecked ->
            saveField.splitCompletedMangaListByFormat = isChecked
        }

        customAnimeListAddIv.setOnClickListener {
            customAnimeListEt.text?.toString()?.takeIf { it.isNotBlank() }?.let {
                saveField.animeCustomLists?.add(it)
                customAnimeListRecyclerView.onTagAdded()
                customAnimeListEt.setText("")
            }
        }
        customMangaListAddIv.setOnClickListener {
            customMangaListEt.text?.toString()?.takeIf { it.isNotBlank() }?.let {
                saveField.mangaCustomLists?.add(it)
                customMangaRecyclerView.onTagAdded()
                customMangaListEt.setText("")
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun MediaListSettingFragmentBinding.bindAdvanceScoring() {
        updateAdvanceScoreView()
        advanceScoreSwitch.isChecked = saveField.advancedScoringEnabled
        advancedScoresRecyclerView.submitTags(saveField.advancedScoring!!)
    }

    private fun MediaListSettingFragmentBinding.updateAdvanceScoreView() {
        if (saveField.scoreFormat == ScoreFormat.POINT_100.ordinal || saveField.scoreFormat == ScoreFormat.POINT_10_DECIMAL.ordinal) {
            advanceScoreSwitch.visibility = View.VISIBLE
            advanceScoringListHeader.visibility = View.VISIBLE
            advanceScoringLayout.visibility =
                if (saveField.advancedScoringEnabled) View.VISIBLE else View.GONE
        } else {
            advanceScoreSwitch.visibility = View.GONE
            advanceScoringListHeader.visibility = View.GONE
            advanceScoringLayout.visibility = View.GONE
        }
    }


    private fun MediaListSettingFragmentBinding.bindCustomList() {
        customAnimeListRecyclerView.submitTags(saveField.animeCustomLists!!)
        customMangaRecyclerView.submitTags(saveField.mangaCustomLists!!)
    }

    private fun MediaListSettingFragmentBinding.showLoading() {
        with(resourceStatusLayout) {
            resourceProgressLayout.progressLayout.visibility = View.VISIBLE
            resourceErrorLayout.errorLayout.visibility = View.GONE
        }
    }

    private fun MediaListSettingFragmentBinding.showError() {
        with(resourceStatusLayout) {
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceErrorLayout.errorLayout.visibility = View.VISIBLE
        }
    }

    private fun MediaListSettingFragmentBinding.clearStatus() {
        with(resourceStatusLayout) {
            resourceProgressLayout.progressLayout.visibility = View.GONE
            resourceErrorLayout.errorLayout.visibility = View.GONE
        }
    }


    override fun updateToolbar() {
        super.updateToolbar()
        updateSettingToolbar()
    }

    private fun updateSettingToolbar() {
        getBaseToolbar().menu.findItem(R.id.save_menu)?.isVisible =
            viewModel.listLiveData.value?.data != null
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_menu -> {
                viewModel.saveMediaListSetting()
                true
            }
            else -> {
                false
            }
        }
    }


}
