package com.revolgenx.anilib.fragment

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.meta.ListEditorMeta
import com.revolgenx.anilib.meta.ListEditorResultMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.model.DateModel
import com.revolgenx.anilib.model.EntryListEditorMediaModel
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.preference.userScoreFormat
import com.revolgenx.anilib.repository.util.Status.*
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.COLLAPSED
import com.revolgenx.anilib.util.EXPANDED
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.viewmodel.MediaEntryEditorViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.list_editor_fragment_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.math.abs


//todo:// check for manga
class EntryListEditorFragment : BaseFragment() {

    companion object {
        const val LIST_EDITOR_MODEL = "list_editor_model"
        const val LIST_EDITOR_META_KEY = "list_editor_meta_key"
    }

    private var state = COLLAPSED //collapsed
    private var fetched = false
    private lateinit var mediaMeta: ListEditorMeta
    private val viewModel by viewModel<MediaEntryEditorViewModel>()
    private var modelEntry: EntryListEditorMediaModel? = null

    private var saving = false
    private var deleting = false
    private var toggling = false
    private var apiModelEntry: EntryListEditorMediaModel? = null
    private var isFavourite = false


    private val calendarDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar)!!.mutate().also {
            it.setTint(tintAccentColor)
        }
    }

    private val crossDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)!!.mutate().also {
            it.setTint(tintAccentColor)
        }
    }

    private val tintAccentColor by lazy {
        DynamicTheme.getInstance().get().tintAccentColor
    }

    private val calendar by lazy {
        Calendar.getInstance()
    }


    private val offSetChangeListener by lazy {
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                state = EXPANDED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                state = COLLAPSED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_editor_fragment_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //bug or smth wont parcelize without class loader
        arguments?.classLoader = ListEditorMeta::class.java.classLoader
        mediaMeta = arguments?.getParcelable(LIST_EDITOR_META_KEY) ?: return

        setToolbarTheme()
        showViews()
        initListener()

        viewModel.mediaLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                SUCCESS -> {
                    if (mediaMeta.coverImage == null || mediaMeta.bannerImage == null) {
                        mediaMeta.coverImage = it.data?.coverImage?.large ?: ""
                        mediaMeta.bannerImage = it.data?.bannerImage ?: ""
                        mediaMeta.title = it.data?.title?.romaji ?: ""
                        showMetaViews()
                    }
                }
            }
        }

        viewModel.getMediaInfo(mediaMeta.mediaId)

        viewModel.queryMediaListEntryLiveData.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    listEditorContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    fetched = true
                    modelEntry = resource.data

                    apiModelEntry = if (savedInstanceState == null) {
                        if (modelEntry == null) createListEditorMediaModel() else modelEntry
                    } else {
                        savedInstanceState.getParcelable(LIST_EDITOR_MODEL)
                            ?: if (modelEntry == null) createListEditorMediaModel() else modelEntry
                    }
                    updateView()
                    invalidateOptionMenu()
                }
                ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    listEditorContainer.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    fetched = false
                }
                LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    listEditorContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    fetched = false
                }
            }
        })

        viewModel.isFavouriteQuery(mediaMeta.mediaId).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    isFavourite = it.data!!
                    listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favorite else R.drawable.ic_not_favourite)
                    invalidateOptionMenu()
                }
                ERROR -> {
                }
                LOADING -> {
                }
            }
        })

        viewModel.toggleFavMediaLiveData.observe(viewLifecycleOwner, Observer {
            toggling = when (it.status) {
                SUCCESS -> {
                    isFavourite = !isFavourite
                    listFavButton.showLoading(false)
                    listFavButton.setImageResource(if (isFavourite) R.drawable.ic_favorite else R.drawable.ic_not_favourite)
                    invalidateOptionMenu()
                    false
                }
                ERROR -> {
                    listFavButton.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
                    false
                }
                LOADING -> {
                    listFavButton.showLoading(true)
                    true
                }
            }
        })

        viewModel.saveMediaListEntryLiveData.observe(viewLifecycleOwner, Observer {
            saving = when (it.status) {
                SUCCESS -> {
                    ListEditorResultEvent(
                        ListEditorResultMeta(
                            apiModelEntry!!.mediaId,
                            apiModelEntry!!.progress?:0,
                            apiModelEntry!!.status
                        )
                    ).postSticky
                    finishActivity()
                    false
                }
                ERROR -> {
                    makeToast(R.string.failed_to_save, icon = R.drawable.ic_error)
                    listSaveButton.showLoading(false)
                    false
                }
                LOADING -> {
                    listSaveButton.showLoading(true)
                    true
                }
            }
        })

        viewModel.deleteMediaListEntryLiveData.observe(viewLifecycleOwner, Observer {
            deleting = when (it.status) {
                SUCCESS -> {
                    ListEditorResultEvent(
                        ListEditorResultMeta(
                            apiModelEntry!!.mediaId,
                            status = apiModelEntry!!.status,
                            deleted =  true
                        )
                    ).postSticky
                    finishActivity()
                    false
                }
                ERROR -> {
                    makeToast(R.string.failed_to_delete, icon = R.drawable.ic_error)
                    listDeleteButton.showLoading(false)
                    false
                }
                LOADING -> {
                    listDeleteButton.showLoading(true)
                    true
                }
            }
        })

        if (savedInstanceState == null)
            viewModel.queryMediaListEntry(mediaMeta.mediaId)
    }

    private fun createListEditorMediaModel(): EntryListEditorMediaModel {
        return EntryListEditorMediaModel().also {
            mediaMeta.mediaId?.let { id ->
                it.mediaId = id
            }
            mediaMeta.type?.let { type ->
                it.type = type
            }
            it.userId = requireContext().userId()
        }
    }

    private fun showViews() {
        if (!::mediaMeta.isInitialized) return
        showMetaViews()
        listDeleteButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.red))

        listEditorScoreLayout.scoreFormatType = requireContext().userScoreFormat()

        if (mediaMeta.type == MediaType.MANGA.ordinal) {
            progressHeader.title = getString(R.string.manga_progress)
            volumeProgressHeader.visibility = View.VISIBLE
            listEditorVolumeProgressLayout.visibility = View.VISIBLE
        }

        val lightSurfaceColor = ThemeController.lightSurfaceColor()
        startDateDynamicEt.setBackgroundColor(lightSurfaceColor)
        endDateDynamicEt.setBackgroundColor(lightSurfaceColor)
        notesEt.setBackgroundColor(lightSurfaceColor)
        statusContainerSpinner.setBackgroundColor(lightSurfaceColor)

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

        val spinnerItems = mutableListOf<DynamicSpinnerItem>()
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_watching
                ), getString(R.string.watching)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_planning
                ), getString(R.string.planning)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_completed
                ), getString(R.string.completed)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_dropped
                ), getString(R.string.dropped)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_paused_filled
                ), getString(R.string.paused)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_rewatching
                ), getString(R.string.rewatching)
            )
        )

        statusSpinner.adapter = DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItems
        )
    }

    private fun showMetaViews() {
        if (!::mediaMeta.isInitialized) return
        (activity as AppCompatActivity).also { act ->
            act.setSupportActionBar(listEditorToolbar)
            act.supportActionBar!!.title = mediaMeta.title
            act.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        listEditorCoverImage.setImageURI(mediaMeta.coverImage)
        listEditorBannerImage.setImageURI(mediaMeta.bannerImage)
    }

    private fun initListener() {
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

        startDateDynamicEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (startDateDynamicEt.right - startDateDynamicEt.compoundDrawablesRelative[2].bounds.width())) {
                    startDateDynamicEt.setText("")
                    apiModelEntry!!.startDate = null
                    true
                }
            }
            false
        }

        endDateDynamicEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (startDateDynamicEt.right - startDateDynamicEt.compoundDrawablesRelative[2].bounds.width())) {
                    endDateDynamicEt.setText("")
                    apiModelEntry!!.endDate = null
                    true
                }
            }
            false
        }



        startDateDynamicEt.setOnClickListener {
            DatePickerDialog(
                requireContext(), { view, year, month, dayOfMonth ->
                    apiModelEntry!!.startDate =
                        (apiModelEntry!!.startDate ?: DateModel()).also {
                            it.year = year
                            it.month = month + 1
                            it.day = dayOfMonth
                        }
                    updateView()
                },
                apiModelEntry!!.startDate?.year ?: calendar.get(Calendar.YEAR),
                apiModelEntry!!.startDate?.month?.minus(1) ?: calendar.get(Calendar.MONTH),
                apiModelEntry!!.startDate?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        endDateDynamicEt.setOnClickListener {
            DatePickerDialog(
                requireContext(), { view, year, month, dayOfMonth ->
                    apiModelEntry!!.endDate =
                        (apiModelEntry!!.endDate ?: DateModel()).also {
                            it.year = year
                            it.month = month + 1
                            it.day = dayOfMonth
                        }
                    updateView()
                },
                apiModelEntry!!.startDate?.year ?: calendar.get(Calendar.YEAR),
                apiModelEntry!!.startDate?.month?.minus(1) ?: calendar.get(Calendar.MONTH),
                apiModelEntry!!.startDate?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
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
                apiModelEntry!!.status = position
            }
        }

        listEditorEpisodeLayout.textChangeListener {
            apiModelEntry!!.progress = it.toString().toInt()
        }

        listEditorVolumeProgressLayout.textChangeListener {
            apiModelEntry!!.progressVolumes = it.toString().toInt()
        }

        listEditorScoreLayout.onScoreChangeListener {
            apiModelEntry!!.score = it.toDouble()
        }
        privateToggleButton.setToggleListener {
            apiModelEntry!!.private = it
        }

        privateToggleButton.setOnLongClickListener {
            makeToast(R.string.make_private)
            true
        }

        notesEt.doOnTextChanged { text, _, _, _ ->
            apiModelEntry!!.notes = text.toString()
        }

        totalRewatchesLayout.textChangeListener {
            apiModelEntry!!.repeat = it.toString().toInt()
        }
    }

    private fun updateView() {
        if (apiModelEntry == null) return
        apiModelEntry!!.status?.let {
            statusSpinner.setSelection(it)
        }
        listEditorScoreLayout.mediaListScore = apiModelEntry!!.score ?: 0.0
        privateToggleButton.checked = apiModelEntry!!.private == true
        listEditorEpisodeLayout.setCounter(apiModelEntry!!.progress?.toDouble())
        totalRewatchesLayout.setCounter(apiModelEntry!!.repeat?.toDouble())
        listEditorVolumeProgressLayout.setCounter(apiModelEntry!!.progressVolumes?.toDouble())
        notesEt.setText(apiModelEntry!!.notes)

        if (apiModelEntry!!.startDate?.year != null) {
            startDateDynamicEt.setText(apiModelEntry!!.startDate!!.let { "${it.year}-${it.month}-${it.day}" })
        }

        if (apiModelEntry!!.endDate?.year != null) {
            endDateDynamicEt.setText(apiModelEntry!!.endDate!!.let { "${it.year}-${it.month}-${it.day}" })
        }
    }


    private fun toggleFav() {
        if (toggling || apiModelEntry == null) return
        viewModel.toggleMediaFavourite(ToggleFavouriteField().also {
            when (mediaMeta.type) {
                MediaType.ANIME.ordinal -> {
                    it.animeId = mediaMeta.mediaId
                }
                MediaType.MANGA.ordinal -> {
                    it.mangaId = mediaMeta.mediaId
                }
            }
        })
    }

    private fun deleteList() {
        if (deleting || apiModelEntry == null) return
        apiModelEntry!!.listId.takeIf { it != -1 }?.let {
            viewModel.deleteMediaListEntry(it)
        }
    }

    private fun saveList() {
        if (saving || apiModelEntry == null) return
        viewModel.saveMediaListEntry(apiModelEntry!!)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (state == EXPANDED) {
            if (modelEntry == null) {
                listDeleteButton.visibility = View.GONE
            } else {
                listDeleteButton.visibility = View.VISIBLE
            }

            if (isFavourite) {
                listFavButton.setImageResource(R.drawable.ic_favorite)
            }
            return
        }

        inflater.inflate(R.menu.list_editor_menu, menu)

        if (modelEntry == null) {
            menu.findItem(R.id.listDeleteMenu).isVisible = false
            listDeleteButton.visibility = View.GONE
        } else {
            listDeleteButton.visibility = View.VISIBLE
        }

        if (isFavourite) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
            menu.findItem(R.id.listFavMenu).icon = drawable
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(LIST_EDITOR_MODEL, apiModelEntry)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                finishActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setToolbarTheme() {
        listEditorCollapsingToolbar.setStatusBarScrimColor(DynamicTheme.getInstance().get().primaryColorDark)
        listEditorCollapsingToolbar.setContentScrimColor(DynamicTheme.getInstance().get().primaryColor)
        listEditorCollapsingToolbar.setCollapsedTitleTextColor(DynamicTheme.getInstance().get().tintPrimaryColor)
        listEditorCollapsingToolbar.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
    }


    override fun onDestroy() {
        appbarLayout?.removeOnOffsetChangedListener(offSetChangeListener)
        super.onDestroy()
    }
}

