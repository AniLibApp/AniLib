package com.revolgenx.anilib.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.ListEditorResultMeta
import com.revolgenx.anilib.data.model.DateModel
import com.revolgenx.anilib.common.preference.getUserPrefModel
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.preference.userScoreFormat
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.type.MediaListStatusEditor
import com.revolgenx.anilib.databinding.ListEditorFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.ui.bottomsheet.airing.CalendarViewBottomSheetDialog
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.entry.MediaEntryEditorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


class EntryListEditorFragment : BaseLayoutFragment<ListEditorFragmentLayoutBinding>() {

    companion object {
        const val LIST_EDITOR_META_KEY = "list_editor_meta_key"

        const val COLLAPSED = 0
        const val EXPANDED = 1

        fun newInstance(meta:ListEditorMeta) = EntryListEditorFragment().also {
            it.arguments = bundleOf(LIST_EDITOR_META_KEY to meta)
        }
    }

    private var state = COLLAPSED //collapsed
    private val mediaMeta: ListEditorMeta? get() = arguments?.getParcelable(LIST_EDITOR_META_KEY)
    private val viewModel by viewModel<MediaEntryEditorViewModel>()

    private var saving = false
    private var deleting = false
    private var toggling = false
    private var isFavourite = false

    private var apiModelEntry
        get() = viewModel.apiModelEntry
        set(value) {
            viewModel.apiModelEntry = value
        }

    override val setHomeAsUp: Boolean = true

    private val calendarDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar)!!.mutate().also {
            it.setTint(dynamicTextColorPrimary)
        }
    }

    private val crossDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)!!.mutate().also {
            it.setTint(dynamicTextColorPrimary)
        }
    }


    private val calendar by lazy {
        Calendar.getInstance()
    }


    private val offSetChangeListener by lazy {
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if(state != EXPANDED){
                    state = EXPANDED
                    updateToolbar()
                }
            } else if (abs(verticalOffset) >= (appBarLayout.totalScrollRange - binding.listEditorToolbar.height)) {
                if(state != COLLAPSED) {
                    state = COLLAPSED
                    updateToolbar()
                }
            }
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ListEditorFragmentLayoutBinding {
        return ListEditorFragmentLayoutBinding.inflate(inflater, parent, false).also {
            it.root.setBackgroundColor(dynamicBackgroundColor)
        }
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //bug or smth wont parcelize without class loader
        val mediaListMeta = mediaMeta ?: return

        apiModelEntry.also {
            it.mediaId = mediaListMeta.mediaId
            it.type = mediaListMeta.type
            it.userId = requireContext().userId()
        }

        setToolbarTheme()
        binding.showViews()
        binding.initListener()

        viewModel.mediaLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data == null) return@observe
                    mediaListMeta.type = it.data.type
                    apiModelEntry.type = mediaListMeta.type
                    if (mediaListMeta.coverImage == null || mediaListMeta.bannerImage == null) {
                        mediaListMeta.coverImage = it.data.coverImage?.large ?: ""
                        mediaListMeta.bannerImage = it.data.bannerImage ?: ""
                        mediaListMeta.title = it.data.title?.romaji ?: ""
                        binding.showMetaViews()
                    }
                }
                else -> {
                }
            }
        }

        viewModel.getMediaInfo(mediaListMeta.mediaId)

        val statusLayout = binding.resourceStatusLayout

        viewModel.queryMediaListEntryLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                SUCCESS -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    binding.listEditorContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    if (apiModelEntry.hasData.not() && resource.data != null) {
                        apiModelEntry = resource.data
                        apiModelEntry.hasData = true
                    }
                    binding.updateView()
                    updateToolbar()
                }
                ERROR -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    binding.listEditorContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    binding.listEditorContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        })

        viewModel.isFavouriteQuery(mediaListMeta.mediaId).observe(viewLifecycleOwner, {
            when (it.status) {
                SUCCESS -> {
                    isFavourite = it.data!!
                    binding.listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
                    updateToolbar()
                }
                ERROR -> {
                }
                LOADING -> {
                }
            }
        })

        viewModel.toggleFavMediaLiveData.observe(viewLifecycleOwner, {
            toggling = when (it.status) {
                SUCCESS -> {
                    isFavourite = !isFavourite
                    binding.listFavButton.showLoading(false)
                    binding.listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
                    updateToolbar()
                    false
                }
                ERROR -> {
                    binding.listFavButton.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
                    false
                }
                LOADING -> {
                    binding.listFavButton.showLoading(true)
                    true
                }
            }
        })

        viewModel.saveMediaListEntryLiveData.observe(viewLifecycleOwner, {
            saving = when (it.status) {
                SUCCESS -> {
                    val meta = ListEditorResultMeta(
                        apiModelEntry.mediaId,
                        apiModelEntry.progress,
                        apiModelEntry.status
                    )
                    ListEditorResultEvent(
                        meta
                    ).postSticky
                    closeListEditor()
                    false
                }
                ERROR -> {
                    makeToast(R.string.failed_to_save, icon = R.drawable.ic_error)
                    binding.listSaveButton.showLoading(false)
                    false
                }
                LOADING -> {
                    binding.listSaveButton.showLoading(true)
                    true
                }
            }
        })

        viewModel.deleteMediaListEntryLiveData.observe(viewLifecycleOwner, {
            deleting = when (it.status) {
                SUCCESS -> {
                    ListEditorResultEvent(
                        ListEditorResultMeta(
                            apiModelEntry.mediaId,
                            status = apiModelEntry.status,
                            deleted = true
                        )
                    ).postEvent
                    closeListEditor()
                    false
                }
                ERROR -> {
                    makeToast(R.string.failed_to_delete, icon = R.drawable.ic_error)
                    binding.listDeleteButton.showLoading(false)
                    false
                }
                LOADING -> {
                    binding.listDeleteButton.showLoading(true)
                    true
                }
            }
        })

        if (savedInstanceState == null)
            viewModel.queryMediaListEntry(mediaListMeta.mediaId)
    }

    private fun closeListEditor() {
        if (activity != null)
            requireActivity().supportFragmentManager.popBackStack()
    }

    private fun ListEditorFragmentLayoutBinding.showViews() {
        val mediaListMeta = mediaMeta?: return
        showMetaViews()
        listDeleteButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))

        listEditorScoreLayout.scoreFormatType = requireContext().userScoreFormat()

        if (mediaListMeta.type == MediaType.MANGA.ordinal) {
            progressHeader.title = getString(R.string.manga_progress)
            volumeProgressHeader.visibility = View.VISIBLE
            listEditorVolumeProgressLayout.visibility = View.VISIBLE
        }


        startDateDynamicEt.setCompoundDrawablesRelativeWithIntrinsicBounds(
            calendarDrawable,
            null,
            crossDrawable,
            null
        )
        endDateDynamicEt.setCompoundDrawablesRelativeWithIntrinsicBounds(
            calendarDrawable,
            null,
            crossDrawable,
            null
        )

        val spinnerItems = mutableListOf<DynamicMenu>()
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_watching
                ), if(mediaListMeta.type == MediaType.MANGA.ordinal) getString(R.string.reading) else getString(R.string.watching)
            )
        )
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_planning
                ), getString(R.string.planning)
            )
        )
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_completed
                ), getString(R.string.completed)
            )
        )
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_rewatching
                ), if(mediaListMeta.type == MediaType.MANGA.ordinal) getString(R.string.rereading) else getString(R.string.rewatching)
            )
        )
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_paused_filled
                ), getString(R.string.paused)
            )
        )
        spinnerItems.add(
            DynamicMenu(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_dropped
                ), getString(R.string.dropped)
            )
        )



        statusSpinner.adapter = DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItems
        )
    }

    private fun ListEditorFragmentLayoutBinding.showMetaViews() {
        val mediaListMeta = mediaMeta?:return
        getBaseToolbar().title = mediaListMeta.title

        mediaTitleTv.text = mediaListMeta.title
        listEditorCoverImage.setImageURI(mediaListMeta.coverImage)
        listEditorBannerImage.setImageURI(mediaListMeta.bannerImage)
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.listEditorToolbar
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun ListEditorFragmentLayoutBinding.initListener() {
        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)

        listSaveButton.setOnClickListener {
            saveList()
        }

        listDeleteButton.setOnClickListener {
            deleteList()
        }

        listFavButton.setOnClickListener {
            toggleFav()
        }

        startDateDynamicEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (startDateDynamicEt.right - startDateDynamicEt.compoundDrawablesRelative[2].bounds.width())) {
                    startDateDynamicEt.setText("")
                    apiModelEntry.startDate = null
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }

        endDateDynamicEt.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (startDateDynamicEt.right - startDateDynamicEt.compoundDrawablesRelative[2].bounds.width())) {
                    endDateDynamicEt.setText("")
                    apiModelEntry.endDate = null
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }



        startDateDynamicEt.setOnClickListener {
            CalendarViewBottomSheetDialog().show(requireContext()) {
                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE
                selectedDate = LocalDate.of(
                    apiModelEntry.startDate?.year ?: calendar.get(Calendar.YEAR),
                    apiModelEntry.startDate?.month ?: calendar.get(Calendar.MONTH) + 1,
                    apiModelEntry.startDate?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
                )
                listener = { startDate, _ ->
                    apiModelEntry.startDate =
                        (apiModelEntry.startDate ?: DateModel()).also {
                            it.year = startDate.year
                            it.month = startDate.month.value
                            it.day = startDate.dayOfMonth
                        }
                    updateMediaProgressDate()
                }
            }

        }

        endDateDynamicEt.setOnClickListener {

            CalendarViewBottomSheetDialog().show(requireContext()) {
                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE

                selectedDate = LocalDate.of(
                    apiModelEntry.endDate?.year ?: calendar.get(Calendar.YEAR),
                    apiModelEntry.endDate?.month ?: calendar.get(Calendar.MONTH) + 1,
                    apiModelEntry.endDate?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
                )
                listener = { startDate, _ ->
                    apiModelEntry.endDate =
                        (apiModelEntry.endDate ?: DateModel()).also {
                            it.year = startDate.year
                            it.month = startDate.month.value
                            it.day = startDate.dayOfMonth
                        }
                    updateMediaProgressDate()
                }
            }
        }

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                apiModelEntry.status = MediaListStatusEditor.values()[position].status
            }
        }

        listEditorEpisodeLayout.textChangeListener {
            apiModelEntry.progress = it.toString().toInt()
        }

        listEditorVolumeProgressLayout.textChangeListener {
            apiModelEntry.progressVolumes = it.toString().toInt()
        }

        listEditorScoreLayout.onScoreChangeListener {
            apiModelEntry.score = it
        }
        privateToggleButton.setToggleListener {
            apiModelEntry.private = it
        }

        privateToggleButton.setOnLongClickListener {
            makeToast(R.string.make_private)
            true
        }

        notesEt.doOnTextChanged { text, _, _, _ ->
            apiModelEntry.notes = text.toString()
        }

        totalRewatchesLayout.textChangeListener {
            apiModelEntry.repeat = it.toString().toInt()
        }

        advanceScoreView.advanceScoreObserver = {
            apiModelEntry.advancedScoring?.let { advanceScoring ->
                val meanScore = advanceScoring.map { it.score }.sum()
                    .div(advanceScoring.count { it.score != 0.0 }.takeIf { it != 0 } ?: 1)
                listEditorScoreLayout.mediaListScore = (meanScore * 10).roundToInt() / 10.0
            }
        }
    }

    private fun ListEditorFragmentLayoutBinding.updateView() {
        apiModelEntry.status?.let {
            statusSpinner.setSelection(MediaListStatusEditor.from(it).ordinal)
        }
        listEditorScoreLayout.mediaListScore = apiModelEntry.score ?: 0.0
        privateToggleButton.checked = apiModelEntry.private == true
        listEditorEpisodeLayout.setCounter(apiModelEntry.progress?.toDouble())
        totalRewatchesLayout.setCounter(apiModelEntry.repeat?.toDouble())
        listEditorVolumeProgressLayout.setCounter(apiModelEntry.progressVolumes?.toDouble())
        notesEt.setText(apiModelEntry.notes)

        updateMediaProgressDate()

        getUserPrefModel(requireContext()).mediaListOption?.let { optionSetting ->
            if (optionSetting.scoreFormat == ScoreFormat.POINT_10_DECIMAL.ordinal || optionSetting.scoreFormat == ScoreFormat.POINT_100.ordinal) {
                if (apiModelEntry.type == MediaType.ANIME.ordinal) {
                    optionSetting.animeList?.let { animeListOption ->
                        if (animeListOption.advancedScoringEnabled) {
                            if (apiModelEntry.advancedScoring == null) {
                                apiModelEntry.advancedScoring =
                                    optionSetting.animeList?.advancedScoring
                            }
                            advanceScoreView.setAdvanceScore(apiModelEntry.advancedScoring!!)
                        } else {
                            advancedScoreLayout.visibility = View.GONE
                        }
                    }
                } else {
                    optionSetting.animeList?.let { mangaListOptions ->
                        if (mangaListOptions.advancedScoringEnabled) {
                            if (apiModelEntry.advancedScoring == null) {
                                apiModelEntry.advancedScoring = mangaListOptions.advancedScoring
                            }
                            advanceScoreView.setAdvanceScore(apiModelEntry.advancedScoring!!)
                        } else {
                            advancedScoreLayout.visibility = View.GONE
                        }
                    }
                }
            } else {
                advancedScoreLayout.visibility = View.GONE
            }
        }
    }


    private fun updateMediaProgressDate() {
        if (apiModelEntry.startDate?.year != null) {
            binding.startDateDynamicEt.setText(this.apiModelEntry.startDate!!.let { "${it.year}-${it.month}-${it.day}" })
        }

        if (apiModelEntry.endDate?.year != null) {
            binding.endDateDynamicEt.setText(this.apiModelEntry.endDate!!.let { "${it.year}-${it.month}-${it.day}" })
        }
    }


    private fun toggleFav() {
        if (toggling) return
        val mediaListMeta = mediaMeta?: return
        viewModel.toggleMediaFavourite(ToggleFavouriteField().also {
            when (mediaListMeta.type) {
                MediaType.ANIME.ordinal -> {
                    it.animeId = mediaListMeta.mediaId
                }
                MediaType.MANGA.ordinal -> {
                    it.mangaId = mediaListMeta.mediaId
                }
            }
        })
    }

    private fun deleteList() {
        if (deleting || !apiModelEntry.hasData || mediaMeta == null) return
        apiModelEntry.listId.takeIf { it != -1 }?.let { listId ->
            makeConfirmationDialog(
                requireContext(),
                message = getString(
                    R.string.do_you_really_want_to_delete_the_entry_s,
                    mediaMeta!!.title ?: ""
                )
            ) {
                viewModel.deleteMediaListEntry(listId)
            }
        }
    }

    private fun saveList() {
        if (saving) return
        viewModel.saveMediaListEntry(apiModelEntry)
    }


    override fun updateToolbar() {
        super.updateToolbar()
        if (state == EXPANDED) {
            if (!apiModelEntry.isUserList) {
                binding.listDeleteButton.visibility = View.GONE
            } else {
                binding.listDeleteButton.visibility = View.VISIBLE
            }

            if (isFavourite) {
                binding.listFavButton.setImageResource(R.drawable.ic_favourite)
            }
            return
        }

        getBaseToolbar().inflateMenu(R.menu.list_editor_menu)
        val menu = getBaseToolbar().menu
        if (!apiModelEntry.isUserList) {
            menu.findItem(R.id.listDeleteMenu).isVisible = false
            binding.listDeleteButton.visibility = View.GONE
        } else {
            binding.listDeleteButton.visibility = View.VISIBLE
        }

        if (isFavourite) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite)
            menu.findItem(R.id.listFavMenu).icon = drawable
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listSaveMenu -> {
                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
                saveList()
                true
            }
            R.id.listDeleteMenu -> {
                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
                deleteList()
                true
            }
            R.id.listFavMenu -> {
                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
                toggleFav()
                true
            }
            android.R.id.home -> {
                closeListEditor()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setToolbarTheme() {
        binding.listEditorCollapsingToolbar.setCollapsedTitleTextColor(
            DynamicTheme.getInstance().get().textPrimaryColor
        )
        binding.listEditorCollapsingToolbar.setBackgroundColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        binding.listEditorCollapsingToolbar.setStatusBarScrimColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        binding.listEditorCollapsingToolbar.setContentScrimColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        binding.listEditorToolbar.colorType = Theme.ColorType.BACKGROUND
        binding.listEditorToolbar.textColorType = Theme.ColorType.TEXT_PRIMARY
    }

    override fun onDestroyView() {
        binding.appbarLayout.removeOnOffsetChangedListener(offSetChangeListener)
        super.onDestroyView()
    }

}

