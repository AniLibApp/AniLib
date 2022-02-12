package com.revolgenx.anilib.entry.fragment
//
//import android.annotation.SuppressLint
//import android.content.res.ColorStateList
//import android.os.Bundle
//import android.view.*
//import android.widget.AdapterView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.content.ContextCompat
//import androidx.core.os.bundleOf
//import androidx.core.widget.doOnTextChanged
//import com.google.android.material.appbar.AppBarLayout
//import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
//import com.pranavpandey.android.dynamic.support.model.DynamicMenu
//import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
//import com.pranavpandey.android.dynamic.theme.Theme
//import com.revolgenx.anilib.R
//import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
//import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
//import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
//import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
//import com.revolgenx.anilib.entry.data.meta.EntryEditorResultMeta
//import com.revolgenx.anilib.common.data.model.FuzzyDateModel
//import com.revolgenx.anilib.common.preference.UserPreference
//import com.revolgenx.anilib.common.preference.getUserPrefModel
//import com.revolgenx.anilib.common.preference.userScoreFormat
//import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
//import com.revolgenx.anilib.constant.MediaListStatusEditor
//import com.revolgenx.anilib.databinding.MediaListEditorFragmentLayoutBinding
//import com.revolgenx.anilib.infrastructure.repository.util.Status.*
//import com.revolgenx.anilib.type.MediaType
//import com.revolgenx.anilib.type.ScoreFormat
//import com.revolgenx.anilib.ui.calendar.bottomsheet.CalendarViewBottomSheetDialog
//import com.revolgenx.anilib.ui.view.makeConfirmationDialog
//import com.revolgenx.anilib.ui.view.makeToast
//import com.revolgenx.anilib.entry.viewmodel.EntryEditorViewModel
//import org.koin.androidx.viewmodel.ext.android.viewModel
//import java.time.LocalDate
//import java.util.*
//import kotlin.math.abs
//import kotlin.math.roundToInt
//
////TODO refactor
//class MediaEntryEditorFragment : BaseLayoutFragment<MediaListEditorFragmentLayoutBinding>() {
//
//    companion object {
//        const val LIST_EDITOR_META_KEY = "list_editor_meta_key"
//
//        const val COLLAPSED = 0
//        const val EXPANDED = 1
//
//        fun newInstance(meta: EntryEditorMeta) = MediaEntryEditorFragment().also {
//            it.arguments = bundleOf(LIST_EDITOR_META_KEY to meta)
//        }
//    }
//
//    private var state = COLLAPSED //collapsed
//    private val mediaMeta: EntryEditorMeta? get() = arguments?.getParcelable(LIST_EDITOR_META_KEY)
//    private val viewModel by viewModel<EntryEditorViewModel>()
//
//    private var saving = false
//    private var deleting = false
//    private var toggling = false
//    private var isFavourite = false
//
//    private var apiModelEntry
//        get() = viewModel.apiModelEntry
//        set(value) {
//            viewModel.apiModelEntry = value
//        }
//
//    override val setHomeAsUp: Boolean = true
//
//    private val calendar by lazy {
//        Calendar.getInstance()
//    }
//
//
//    private val offSetChangeListener by lazy {
//        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            if (verticalOffset == 0) {
//                if (state != EXPANDED) {
//                    state = EXPANDED
//                    updateToolbar()
//                }
//            } else if (abs(verticalOffset) >= (appBarLayout.totalScrollRange - binding.listEditorToolbar.height)) {
//                if (state != COLLAPSED) {
//                    state = COLLAPSED
//                    updateToolbar()
//                }
//            }
//        }
//    }
//
//    override fun bindView(
//        inflater: LayoutInflater,
//        parent: ViewGroup?
//    ): MediaListEditorFragmentLayoutBinding {
//        return MediaListEditorFragmentLayoutBinding.inflate(inflater, parent, false).also {
//            it.root.setBackgroundColor(dynamicBackgroundColor)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        setHasOptionsMenu(true)
//        (activity as AppCompatActivity).invalidateOptionsMenu()
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        //bug or smth wont parcelize without class loader
//        val mediaListMeta = mediaMeta ?: return
//
//        apiModelEntry.also {
//            it.mediaId = mediaListMeta.mediaId ?: -1
////            it.type = mediaListMeta.type
//            it.userId = UserPreference.userId
//        }
//
//        setToolbarTheme()
//        binding.showViews()
//        binding.initListener()
//
//        viewModel.mediaLiveData.observe(viewLifecycleOwner) {
//            when (it.status) {
//                SUCCESS -> {
//                    if (it.data == null) return@observe
//                    mediaListMeta.type = it.data.type
////                    apiModelEntry.type = mediaListMeta.type
//                    if (mediaListMeta.coverImage == null || mediaListMeta.bannerImage == null) {
//                        mediaListMeta.coverImage = it.data.coverImage?.large ?: ""
//                        mediaListMeta.bannerImage = it.data.bannerImage ?: ""
//                        mediaListMeta.title = it.data.title?.romaji ?: ""
//                        binding.showMetaViews()
//                    }
//                }
//                else -> {
//                }
//            }
//        }
//
//        if(savedInstanceState == null){
//            viewModel.getMediaInfo(mediaListMeta.mediaId)
//        }
//
//        val statusLayout = binding.resourceStatusLayout
//
//        viewModel.queryMediaListEntryLiveData.observe(viewLifecycleOwner, { resource ->
//            when (resource.status) {
//                SUCCESS -> {
//                    statusLayout.resourceStatusContainer.visibility = View.GONE
//                    binding.listEditorContainer.visibility = View.VISIBLE
//                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
//                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
////                    if (apiModelEntry.hasData.not() && resource.data != null) {
////                        apiModelEntry = resource.data
////                        apiModelEntry.hasData = true
////                    }
//                    binding.updateView()
//                    updateToolbar()
//                }
//                ERROR -> {
//                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
//                    binding.listEditorContainer.visibility = View.GONE
//                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
//                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
//                }
//                LOADING -> {
//                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
//                    binding.listEditorContainer.visibility = View.GONE
//                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
//                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
//                }
//            }
//        })
//
//        viewModel.isFavLiveData.observe(viewLifecycleOwner){
//            when (it.status) {
//                SUCCESS -> {
//                    isFavourite = it.data!!
//                    binding.listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
//                    updateToolbar()
//                }
//                ERROR -> {
//                }
//                LOADING -> {
//                }
//            }
//        }
//
//
//        viewModel.toggleFavMediaLiveData.observe(viewLifecycleOwner, {
//            toggling = when (it.status) {
//                SUCCESS -> {
//                    isFavourite = !isFavourite
//                    binding.listFavButton.showLoading(false)
//                    binding.listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favourite else R.drawable.ic_not_favourite)
//                    updateToolbar()
//                    false
//                }
//                ERROR -> {
//                    binding.listFavButton.showLoading(false)
//                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
//                    false
//                }
//                LOADING -> {
//                    binding.listFavButton.showLoading(true)
//                    true
//                }
//            }
//        })
//
//        viewModel.saveMediaListEntryLiveData.observe(viewLifecycleOwner, {
//            saving = when (it.status) {
//                SUCCESS -> {
//                    val meta = EntryEditorResultMeta(
//                        apiModelEntry.mediaId,
//                        apiModelEntry.progress,
//                        apiModelEntry.status
//                    )
//                    ListEditorResultEvent(
//                        meta
//                    ).postSticky
//                    closeListEditor()
//                    false
//                }
//                ERROR -> {
//                    makeToast(R.string.failed_to_save, icon = R.drawable.ic_error)
//                    binding.listSaveButton.showLoading(false)
//                    false
//                }
//                LOADING -> {
//                    binding.listSaveButton.showLoading(true)
//                    true
//                }
//            }
//        })
//
//        viewModel.deleteMediaListEntryLiveData.observe(viewLifecycleOwner, {
//            deleting = when (it.status) {
//                SUCCESS -> {
//                    ListEditorResultEvent(
//                        EntryEditorResultMeta(
//                            apiModelEntry.mediaId,
//                            status = apiModelEntry.status,
//                            deleted = true
//                        )
//                    ).postEvent
//                    closeListEditor()
//                    false
//                }
//                ERROR -> {
//                    makeToast(R.string.failed_to_delete, icon = R.drawable.ic_error)
//                    binding.listDeleteButton.showLoading(false)
//                    false
//                }
//                LOADING -> {
//                    binding.listDeleteButton.showLoading(true)
//                    true
//                }
//            }
//        })
//
//        if (savedInstanceState == null){
//            viewModel.isFavouriteQuery(mediaListMeta.mediaId)
//            viewModel.queryMediaListEntry(mediaListMeta.mediaId)
//        }
//    }
//
//    private fun closeListEditor() {
//        if (activity != null)
//            requireActivity().supportFragmentManager.popBackStack()
//    }
//
//    private fun MediaListEditorFragmentLayoutBinding.showViews() {
//        val mediaListMeta = mediaMeta ?: return
//        showMetaViews()
//        listDeleteButton.backgroundTintList =
//            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))
//
//        alListEditorScoreLayout.scoreFormatType =
//            ScoreFormat.values()[requireContext().userScoreFormat()]
//
//        if (mediaListMeta.type == MediaType.MANGA.ordinal) {
//            progressHeader.text = getString(R.string.manga_progress)
//            volumeProgressLayout.visibility = View.VISIBLE
//        }
//
//
//        val spinnerItems = mutableListOf<DynamicMenu>()
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_watching
//                ),
//                if (mediaListMeta.type == MediaType.MANGA.ordinal) getString(R.string.reading) else getString(
//                    R.string.watching
//                )
//            )
//        )
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_planning
//                ),
//                if (mediaListMeta.type == MediaType.MANGA.ordinal) getString(R.string.plan_to_read) else getString(
//                    R.string.plan_to_watch
//                )
//            )
//        )
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_completed
//                ), getString(R.string.completed)
//            )
//        )
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_rewatching
//                ),
//                if (mediaListMeta.type == MediaType.MANGA.ordinal) getString(R.string.rereading) else getString(
//                    R.string.rewatching
//                )
//            )
//        )
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_paused_filled
//                ), getString(R.string.paused)
//            )
//        )
//        spinnerItems.add(
//            DynamicMenu(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_dropped
//                ), getString(R.string.dropped)
//            )
//        )
//
//
//
//        statusSpinner.adapter = DynamicSpinnerImageAdapter(
//            requireContext(),
//            R.layout.ads_layout_spinner_item,
//            R.id.ads_spinner_item_icon,
//            R.id.ads_spinner_item_text, spinnerItems
//        )
//    }
//
//    private fun MediaListEditorFragmentLayoutBinding.showMetaViews() {
//        val mediaListMeta = mediaMeta ?: return
//        getBaseToolbar().title = mediaListMeta.title
//
//        mediaTitleTv.text = mediaListMeta.title
//        listEditorCoverImage.setImageURI(mediaListMeta.coverImage)
//        listEditorBannerImage.setImageURI(mediaListMeta.bannerImage)
//    }
//
//    override fun getBaseToolbar(): Toolbar {
//        return binding.listEditorToolbar
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private fun MediaListEditorFragmentLayoutBinding.initListener() {
//        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)
//
//        listSaveButton.setOnClickListener {
//            saveList()
//        }
//
//        listDeleteButton.setOnClickListener {
//            deleteList()
//        }
//
//        listFavButton.setOnClickListener {
//            toggleFav()
//        }
//
//        startDateClearIv.setOnClickListener {
//            startDateDynamicTv.text = ""
//            apiModelEntry.startedAt = null
//        }
//
//        endDateClearIv.setOnClickListener {
//            endDateDynamicTv.text = ""
//            apiModelEntry.completedAt = null
//        }
//
//        startDateDynamicTv.setDrawables(startRes = R.drawable.ic_calendar)
//        endDateDynamicTv.setDrawables(startRes = R.drawable.ic_calendar)
//
//        startDateDynamicTv.setOnClickListener {
//            CalendarViewBottomSheetDialog().show(requireContext()) {
//                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE
//                selectedDate = LocalDate.of(
//                    apiModelEntry.startedAt?.year ?: calendar.get(Calendar.YEAR),
//                    apiModelEntry.startedAt?.month ?: calendar.get(Calendar.MONTH) + 1,
//                    apiModelEntry.startedAt?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
//                )
//                listener = { startDate, _ ->
//                    apiModelEntry.startedAt =
//                        (apiModelEntry.startedAt ?: FuzzyDateModel()).also {
//                            it.year = startDate.year
//                            it.month = startDate.month.value
//                            it.day = startDate.dayOfMonth
//                        }
//                    updateMediaProgressDate()
//                }
//            }
//
//        }
//
//        endDateDynamicTv.setOnClickListener {
//            CalendarViewBottomSheetDialog().show(requireContext()) {
//                selectionMode = CalendarViewBottomSheetDialog.SelectionMode.DATE
//                selectedDate = LocalDate.of(
//                    apiModelEntry.completedAt?.year ?: calendar.get(Calendar.YEAR),
//                    apiModelEntry.completedAt?.month ?: calendar.get(Calendar.MONTH) + 1,
//                    apiModelEntry.completedAt?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
//                )
//                listener = { startDate, _ ->
//                    apiModelEntry.completedAt =
//                        (apiModelEntry.completedAt ?: FuzzyDateModel()).also {
//                            it.year = startDate.year
//                            it.month = startDate.month.value
//                            it.day = startDate.dayOfMonth
//                        }
//                    updateMediaProgressDate()
//                }
//            }
//        }
//
//        statusSpinner.spinnerView.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    apiModelEntry.status = MediaListStatusEditor.values()[position].status
//                }
//            }
//
//        listEditorEpisodeLayout.onCountChangeListener = {
//            apiModelEntry.progress = it.toInt()
//        }
//
//        listEditorVolumeProgressLayout.onCountChangeListener = {
//            apiModelEntry.progressVolumes = it.toInt()
//        }
//
//        alListEditorScoreLayout.onScoreChangeListener = {
//            apiModelEntry.score = it
//        }
//
//        privateToggleButton.setToggleListener {
//            apiModelEntry.private = it
//        }
//
//        privateToggleButton.setOnLongClickListener {
//            makeToast(R.string.make_private)
//            true
//        }
//
//        notesEt.doOnTextChanged { text, _, _, _ ->
//            apiModelEntry.notes = text.toString()
//        }
//
//        totalRewatchesLayout.onCountChangeListener = {
//            apiModelEntry.repeat = it.toInt()
//        }
//
//        advanceScoreView.advanceScoreObserver = {
//            apiModelEntry.advanceScores?.let { advanceScoring ->
//                val meanScore = advanceScoring.values.sum()
//                    .div(advanceScoring.values.count { it != 0.0 }.takeIf { it != 0 } ?: 1)
//                alListEditorScoreLayout.updateScore((meanScore * 10).roundToInt() / 10.0)
//            }
//        }
//    }
//
//    private fun MediaListEditorFragmentLayoutBinding.updateView() {
//        apiModelEntry.status?.let {
//            statusSpinner.setSelection(MediaListStatusEditor.from(it).ordinal)
//        }
//        alListEditorScoreLayout.updateScore(apiModelEntry.score ?: 0.0)
//        privateToggleButton.checked = apiModelEntry.private == true
//        listEditorEpisodeLayout.updateCount(apiModelEntry.progress)
//        totalRewatchesLayout.updateCount(apiModelEntry.repeat)
//        listEditorVolumeProgressLayout.updateCount(apiModelEntry.progressVolumes)
//        notesEt.setText(apiModelEntry.notes)
//
//        updateMediaProgressDate()
//
//        getUserPrefModel(requireContext()).mediaListOptions?.let { optionSetting ->
//            if (optionSetting.scoreFormat == ScoreFormat.POINT_10_DECIMAL.ordinal || optionSetting.scoreFormat == ScoreFormat.POINT_100.ordinal) {
////                if (apiModelEntry.type == MediaType.ANIME.ordinal) {
////                    optionSetting.animeList?.let { animeListOption ->
////                        if (animeListOption.advancedScoringEnabled) {
////                            if (apiModelEntry.advancedScoring == null) {
////                                apiModelEntry.advancedScoring =
////                                    optionSetting.animeList?.advancedScoring
////                            }
////                            advanceScoreView.setAdvanceScore(apiModelEntry.advancedScoring!!)
////                        } else {
////                            advanceScoreHeader.visibility = View.GONE
////                            advanceScoreView.visibility = View.GONE
////                        }
////                    }
////                } else {
////                    optionSetting.animeList?.let { mangaListOptions ->
////                        if (mangaListOptions.advancedScoringEnabled) {
////                            if (apiModelEntry.advancedScoring == null) {
////                                apiModelEntry.advancedScoring = mangaListOptions.advancedScoring
////                            }
////                            advanceScoreView.setAdvanceScore(apiModelEntry.advancedScoring!!)
////                        } else {
////                            advanceScoreHeader.visibility = View.GONE
////                            advanceScoreView.visibility = View.GONE
////                        }
////                    }
////                }
//            } else {
//                advanceScoreHeader.visibility = View.GONE
//                advanceScoreView.visibility = View.GONE
//            }
//        }
//    }
//
//
//    private fun updateMediaProgressDate() {
//        if (apiModelEntry.startedAt?.year != null) {
//            binding.startDateDynamicTv.text = this.apiModelEntry.startedAt!!.let { "${it.year}-${it.month}-${it.day}" }
//        }
//
//        if (apiModelEntry.completedAt?.year != null) {
//            binding.endDateDynamicTv.text = this.apiModelEntry.completedAt!!.let { "${it.year}-${it.month}-${it.day}" }
//        }
//    }
//
//
//    private fun toggleFav() {
//        if (toggling) return
//        val mediaListMeta = mediaMeta ?: return
//        viewModel.toggleMediaFavourite(ToggleFavouriteField().also {
//            when (mediaListMeta.type) {
//                MediaType.ANIME.ordinal -> {
//                    it.animeId = mediaListMeta.mediaId
//                }
//                MediaType.MANGA.ordinal -> {
//                    it.mangaId = mediaListMeta.mediaId
//                }
//            }
//        })
//    }
//
//    private fun deleteList() {
////        if (deleting || !apiModelEntry.hasData || mediaMeta == null) return
//        apiModelEntrymutation($id:Int $mediaId:Int $status:MediaListStatus $score:Float $progress:Int $progressVolumes:Int $repeat:Int $private:Boolean $notes:String $customLists:[String]$hiddenFromStatusLists:Boolean $advancedScores:[Float]$startedAt:FuzzyDateInput $completedAt:FuzzyDateInput){SaveMediaListEntry(id:$id mediaId:$mediaId status:$status score:$score progress:$progress progressVolumes:$progressVolumes repeat:$repeat private:$private notes:$notes customLists:$customLists hiddenFromStatusLists:$hiddenFromStatusLists advancedScores:$advancedScores startedAt:$startedAt completedAt:$completedAt){id mediaId status score advancedScores progress progressVolumes repeat priority private hiddenFromStatusLists customLists notes updatedAt startedAt{year month day}completedAt{year month day}user{id name}media{id title{userPreferred}coverImage{large}type format status episodes volumes chapters averageScore popularity isAdult startDate{year}}}}.id.takeIf { it != -1 }?.let { listId ->
//            makeConfirmationDialog(
//                requireContext(),
//                message = getString(
//                    R.string.do_you_really_want_to_delete_the_entry_s,
//                    mediaMeta!!.title ?: ""
//                )
//            ) {
//                viewModel.deleteMediaListEntry(listId)
//            }
//        }
//    }
//
//    private fun saveList() {
//        if (saving) return
//        viewModel.saveMediaListEntry(apiModelEntry)
//    }
//
//
//    override fun updateToolbar() {
//        super.updateToolbar()
//        if (state == EXPANDED) {
////            if (!apiModelEntry.isUserList) {
////                binding.listDeleteButton.visibility = View.GONE
////            } else {
////                binding.listDeleteButton.visibility = View.VISIBLE
////            }
//
//            if (isFavourite) {
//                binding.listFavButton.setImageResource(R.drawable.ic_favourite)
//            }
//            return
//        }
//
//        getBaseToolbar().inflateMenu(R.menu.list_editor_menu)
//        val menu = getBaseToolbar().menu
////        if (!apiModelEntry.isUserList) {
////            menu.findItem(R.id.listDeleteMenu).isVisible = false
////            binding.listDeleteButton.visibility = View.GONE
////        } else {
////            binding.listDeleteButton.visibility = View.VISIBLE
////        }
//
//        if (isFavourite) {
//            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favourite)
//            menu.findItem(R.id.listFavMenu).icon = drawable
//        }
//    }
//
//    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.listSaveMenu -> {
//                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
//                saveList()
//                true
//            }
//            R.id.listDeleteMenu -> {
//                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
//                deleteList()
//                true
//            }
//            R.id.listFavMenu -> {
//                makeToast(R.string.please_wait, icon = R.drawable.ic_hour_glass)
//                toggleFav()
//                true
//            }
//            android.R.id.home -> {
//                closeListEditor()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//
//    private fun setToolbarTheme() {
//        binding.listEditorCollapsingToolbar.setCollapsedTitleTextColor(
//            DynamicTheme.getInstance().get().textPrimaryColor
//        )
//        binding.listEditorCollapsingToolbar.setBackgroundColor(
//            DynamicTheme.getInstance().get().backgroundColor
//        )
//        binding.listEditorCollapsingToolbar.setStatusBarScrimColor(
//            DynamicTheme.getInstance().get().backgroundColor
//        )
//        binding.listEditorCollapsingToolbar.setContentScrimColor(
//            DynamicTheme.getInstance().get().backgroundColor
//        )
//        binding.listEditorToolbar.colorType = Theme.ColorType.BACKGROUND
//        binding.listEditorToolbar.textColorType = Theme.ColorType.TEXT_PRIMARY
//    }
//
//    override fun onDestroyView() {
//        binding.appbarLayout.removeOnOffsetChangedListener(offSetChangeListener)
//        super.onDestroyView()
//    }
//
//}
//
