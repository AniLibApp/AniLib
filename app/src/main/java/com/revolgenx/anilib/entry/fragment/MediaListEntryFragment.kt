package com.revolgenx.anilib.entry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.extensions.MainSource
import com.otaliastudios.elements.extensions.SimplePresenter
import com.otaliastudios.elements.pagers.NoPagesPager
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.MediaListStatusEditor
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.databinding.MediaListEntryFragmentLayoutBinding
import com.revolgenx.anilib.entry.data.model.UserMediaModel
import com.revolgenx.anilib.entry.viewmodel.MediaListEntryVM
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.calendar.bottomsheet.CalendarViewBottomSheetDialog
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt

class MediaListEntryFragment : BaseLayoutFragment<MediaListEntryFragmentLayoutBinding>() {
    override val menuRes: Int = R.menu.list_editor_fragment_menu
    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.edit

    private val viewModel by viewModel<MediaListEntryVM>()
    private val field get() = viewModel.field
    private val saveField get() = viewModel.saveField

    private val mediaModel get() = viewModel.media
    private val userModel get() = viewModel.user


    private val calendar = Calendar.getInstance()

    companion object {
        private const val media_id_key = "media_id_key"
        fun newInstance(mediaId: Int) = MediaListEntryFragment().also {
            it.arguments = bundleOf(media_id_key to mediaId)
        }
    }

    private val mediaId get() = arguments?.getInt(media_id_key)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaListEntryFragmentLayoutBinding {
        return MediaListEntryFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        field.mediaId = mediaId ?: return
        saveField.mediaId = mediaId
        field.userId = UserPreference.userId

        viewModel.mediaListEntry.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.clearResourceStatus()
                    it.data?.let {
                        binding.bind(it)
                    }
                }
                Status.ERROR -> {
                    binding.showError()
                }
                Status.LOADING -> {
                    binding.showLoading()
                }
            }
        }

        viewModel.saveMediaListEntry.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    popBackStack()
                }
                Status.ERROR -> {
                    makeToast(R.string.failed_to_save)
                }
                Status.LOADING -> {
                    binding.showLoading()
                }
            }
        }

        viewModel.deleteMediaListEntry.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    popBackStack()
                }
                Status.ERROR -> {
                    makeErrorToast(R.string.failed_to_delete)
                }
                Status.LOADING -> {
                    binding.showLoading()
                }
            }
        }

        viewModel.favouriteLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    updateFavouriteToolbar()
                }
                Status.ERROR -> {
                    makeToast(R.string.failed_to_toggle)
                    updateFavouriteToolbar()
                }
                Status.LOADING -> {}
            }
        }
        if (savedInstanceState == null) {
            viewModel.getMediaListEntry()
        }
    }

    override fun updateToolbar() {
        super.updateToolbar()
        updateEntryToolbar()
    }

    private fun MediaListEntryFragmentLayoutBinding.bind(userMediaModel: UserMediaModel) {
        mediaModel ?: return
        listEditorContainer.visibility = View.VISIBLE
        updateEntryToolbar()
        bindStatus()
        bindScore()
        bindProgress()
        bindDate()
        bindTotalRewatch()
        bindNotes()
        bindCustomLists()
        bindAdvancedScores()

        listPrivateCheckBox.isChecked = saveField.private == true
        listPrivateCheckBox.setOnCheckedChangeListener { _, isChecked ->
            saveField.private = isChecked
        }

        hideFromStatusList.isChecked = saveField.hiddenFromStatusLists
        hideFromStatusList.setOnCheckedChangeListener { _, isChecked ->
            saveField.hiddenFromStatusLists = isChecked
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindStatus() {
        val media = mediaModel ?: return

        val statusListRes = if (media.isAnime()) {
            R.array.media_list_status
        } else {
            R.array.media_list_manga_status
        }

        val statusItems = requireContext().resources.getStringArray(statusListRes).map {
            DynamicMenu(null, it)
        }

        val statusSpinner = statusSpinnerLayout.spinnerView
        statusSpinner.adapter = makeSpinnerAdapter(requireContext(), statusItems)

        saveField.status?.let {
            statusSpinner.setSelection(
                MediaListStatusEditor.from(it).ordinal
            )
        }

        statusSpinner.onItemSelected {
            saveField.status = MediaListStatusEditor.values()[it].status
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindScore() {
        userModel?.mediaListOptions?.scoreFormat?.let {
            listScoreLayout.scoreFormatType = ScoreFormat.values()[it]
        }

        saveField.score?.let {
            listScoreLayout.updateScore(it)
        }

        listScoreLayout.onScoreChangeListener = {
            saveField.score = it
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindProgress() {
        val media = mediaModel ?: return

        if (media.isManga()) {
            listEpisodeChapterHeader.setText(R.string.manga_progress)
            listVolumeLayout.visibility = View.VISIBLE

            media.volumes?.let {
                listVolumeCountLayout.max = it
            } ?: let {
                listVolumeCountLayout.infiniteCount = true
            }

            saveField.progressVolumes?.let {
                listVolumeCountLayout.updateCount(it)
            }


            media.chapters?.let {
                listEpisodeChapterCountLayout.max = it
            } ?: let {
                listEpisodeChapterCountLayout.infiniteCount = true
            }

            listVolumeCountLayout.onCountChangeListener = {
                saveField.progressVolumes = it.toInt()
            }
        } else {
            media.episodes?.let {
                listEpisodeChapterCountLayout.max = it
            } ?: let {
                listEpisodeChapterCountLayout.infiniteCount = true
            }
        }

        saveField.progress?.let {
            listEpisodeChapterCountLayout.updateCount(it)
        }

        listEpisodeChapterCountLayout.onCountChangeListener = {
            saveField.progress = it.toInt()
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindDate() {
        startDateTv.setDrawables(startRes = R.drawable.ic_calendar)
        finishDateTv.setDrawables(startRes = R.drawable.ic_calendar)

        changeDate()
        startDateClearIv.setOnClickListener {
            startDateTv.text = ""
            saveField.startedAt = null
        }

        finishDateClearIv.setOnClickListener {
            finishDateTv.text = ""
            saveField.completedAt = null
        }

        startDateTv.setOnClickListener {
            CalendarViewBottomSheetDialog().show(requireContext()) {
                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE
                selectedDate = LocalDate.of(
                    saveField.startedAt?.year ?: calendar.get(Calendar.YEAR),
                    saveField.startedAt?.month ?: calendar.get(Calendar.MONTH) + 1,
                    saveField.startedAt?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
                )
                listener = { startDate, _ ->
                    saveField.startedAt =
                        (saveField.startedAt ?: FuzzyDateModel()).also {
                            it.year = startDate.year
                            it.month = startDate.month.value
                            it.day = startDate.dayOfMonth
                        }
                    changeDate()
                }
            }

        }

        finishDateTv.setOnClickListener {
            CalendarViewBottomSheetDialog().show(requireContext()) {
                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE
                selectedDate = LocalDate.of(
                    saveField.completedAt?.year ?: calendar.get(Calendar.YEAR),
                    saveField.completedAt?.month ?: calendar.get(Calendar.MONTH) + 1,
                    saveField.completedAt?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
                )
                listener = { startDate, _ ->
                    saveField.completedAt =
                        (saveField.completedAt ?: FuzzyDateModel()).also {
                            it.year = startDate.year
                            it.month = startDate.month.value
                            it.day = startDate.dayOfMonth
                        }
                    changeDate()
                }
            }
        }

    }

    private fun MediaListEntryFragmentLayoutBinding.changeDate() {
        saveField.startedAt?.let {
            startDateTv.text = "${it.year}-${it.month}-${it.day}"
        }
        saveField.completedAt?.let {
            finishDateTv.text = "${it.year}-${it.month}-${it.day}"
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindTotalRewatch() {
        val media = mediaModel ?: return

        if (media.isManga()) {
            totalRewatchHeader.setText(R.string.total_reread)
        }

        totalRewatchCountLayout.infiniteCount = true
        saveField.repeat?.let {
            totalRewatchCountLayout.updateCount(it)
        }

        totalRewatchCountLayout.onCountChangeListener = {
            saveField.repeat = it.toInt()
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindNotes() {
        saveField.notes?.let {
            notesEt.setText(it)
        }

        notesEt.doOnTextChanged { text, _, _, _ ->
            saveField.notes = text.toString()
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.bindCustomLists() {
        Adapter.builder(viewLifecycleOwner)
            .setPager(NoPagesPager())
            .addPresenter(
                SimplePresenter<MutablePair<String, Boolean>>(
                    requireContext(),
                    R.layout.custom_list_presenter_layout,
                    0
                ) { view, item ->
                    val checkBox = view.findViewById<CheckBox>(R.id.custom_list_checkbox)
                    checkBox.isChecked = item.second
                    checkBox.text = item.first
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        item.second = isChecked
                    }
                })
            .addPresenter(
                Presenter.forEmptyIndicator(
                    requireContext(),
                    R.layout.empty_custom_lists
                )
            )
            .addSource(object : MainSource<MutablePair<String, Boolean>>() {
                override fun areItemsTheSame(
                    first: MutablePair<String, Boolean>,
                    second: MutablePair<String, Boolean>
                ) = false

                override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
                    super.onPageOpened(page, dependencies)
                    if (page.isFirstPage()) {
                        postResult(page, saveField.customLists ?: emptyList())
                    }
                }
            })
            .into(customListRecyclerView)
    }

    private fun MediaListEntryFragmentLayoutBinding.bindAdvancedScores() {
        val user = userModel ?: return
        val media = mediaModel ?: return

        val isAdvancedScoreEnabled = if (media.isAnime()) {
            user.mediaListOptions?.let {
                (it.scoreFormat == ScoreFormat.POINT_10_DECIMAL.ordinal
                        || it.scoreFormat == ScoreFormat.POINT_100.ordinal)
                        && it.animeList?.advancedScoringEnabled == true
            } == true
        } else {
            user.mediaListOptions?.let {
                (it.scoreFormat == ScoreFormat.POINT_10_DECIMAL.ordinal
                        || it.scoreFormat == ScoreFormat.POINT_100.ordinal)
                        && it.mangaList?.advancedScoringEnabled == true
            } == true
        }

        if (isAdvancedScoreEnabled) {
            advancedScoreLayout.visibility = View.VISIBLE
        } else {
            advancedScoreLayout.visibility = View.GONE
            return
        }

        saveField.advancedScores?.let {
            advanceScoreView.setAdvanceScore(it)
        }

        advanceScoreView.advanceScoreObserver = {
            saveField.advancedScores?.let { advancedScoring ->
                val meanScore = advancedScoring.sumOf { it.second }
                    .div(advancedScoring.count { it.second != 0.0 }.takeIf { it != 0 } ?: 1)
                listScoreLayout.updateScore((meanScore * 10).roundToInt() / 10.0)
            }
        }
    }

    private fun MediaListEntryFragmentLayoutBinding.showLoading() {
        resourceStatusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
        resourceStatusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
        resourceStatusLayout.resourceStatusContainer.visibility = View.VISIBLE
    }

    private fun MediaListEntryFragmentLayoutBinding.showError() {
        resourceStatusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
        resourceStatusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
        resourceStatusLayout.resourceStatusContainer.visibility = View.VISIBLE
    }

    private fun MediaListEntryFragmentLayoutBinding.clearResourceStatus() {
        resourceStatusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
        resourceStatusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
        resourceStatusLayout.resourceStatusContainer.visibility = View.GONE
    }


    private fun MediaModel.isAnime() = type == MediaType.ANIME.ordinal
    private fun MediaModel.isManga() = type == MediaType.MANGA.ordinal


    private fun updateEntryToolbar() {
        val media = mediaModel ?: return
        media.title?.title(requireContext())?.let { updateToolbarTitle(it) }
        val mediaListEntry = media.mediaListEntry
        getBaseToolbar().menu.let { menu ->
            menu.findItem(R.id.list_save_menu).isVisible = true
            menu.findItem(R.id.list_delete_menu).isVisible = mediaListEntry != null
        }
        updateFavouriteToolbar()
    }

    private fun updateFavouriteToolbar() {
        mediaModel?.apply {
            getBaseToolbar().menu.findItem(R.id.list_favourite_menu)?.let {
                it.isVisible = true
                it.setIcon(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
            }
        }
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

    private fun updateToolbarTitle(title: String) {
        getBaseToolbar().title = title
    }


    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_favourite_menu -> {
                viewModel.toggleFavourite()
                true
            }
            R.id.list_delete_menu -> {
                val title =
                    viewModel.mediaListEntry.value?.data?.media?.title?.title(requireContext())
                makeConfirmationDialog(
                    requireContext(),
                    message = getString(
                        R.string.do_you_really_want_to_delete_the_entry_s,
                        title?.let { " $it" } ?: ""
                    )
                ) {
                    viewModel.deleteMediaListEntry()
                }
                true
            }
            R.id.list_save_menu -> {
                viewModel.saveMediaListEntry()
                true
            }
            else -> super.onToolbarMenuSelected(item)
        }
    }

}