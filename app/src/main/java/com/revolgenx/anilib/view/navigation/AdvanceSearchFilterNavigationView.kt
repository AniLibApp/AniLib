package com.revolgenx.anilib.view.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.AdvanceSearchTypes
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.presenter.TagPresenter
import com.revolgenx.anilib.util.onItemSelected
import kotlinx.android.synthetic.main.advance_search_filter_navigation_view.view.*
import java.util.*


//todo://search year not working after rotation
class AdvanceSearchFilterNavigationView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicNavigationView(context, attributeSet, style) {

    private var mListener: AdvanceBrowseNavigationCallbackListener? = null
    private val rView by lazy {
        LayoutInflater.from(context).inflate(
            R.layout.advance_search_filter_navigation_view,
            null,
            false
        )
    }

    private val rippleDrawable: RippleDrawable
        get() = RippleDrawable(ColorStateList.valueOf(accentColor), null, null)

    private val accentColor: Int
        get() = DynamicTheme.getInstance().get().accentColor

    private lateinit var streamAdapter: Adapter
    private lateinit var genreAdapter: Adapter
    private lateinit var tagAdapter: Adapter
    private val streamPresenter by lazy {
        TagPresenter(context).also {
            it.tagRemoved {
                streamingTagMap!![it]?.isTagged = false
            }
        }
    }
    private val genrePresenter by lazy {
        TagPresenter(context).also {
            it.tagRemoved {
                genreTagMap!![it]?.isTagged = false
            }
        }
    }
    private val tagPresenter by lazy {
        TagPresenter(context).also {
            it.tagRemoved {
                tagTagMap!![it]?.isTagged = false
            }
        }
    }

    private val streamingOnList
        get() =
            context.resources.getStringArray(R.array.streaming_on)

    private val tagList
        get() =
            context.resources.getStringArray(R.array.media_tags)

    private val genreList
        get() =
            context.resources.getStringArray(R.array.media_genre)

    private var genreTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                genreList.map { map[it] = TagField(it, false) }
            }
            return field
        }

    private var tagTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                tagList.map { map[it] = TagField(it, false) }
            }
            return field
        }

    private var streamingTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                streamingOnList.map { map[it] = TagField(it, false) }
            }
            return field
        }

    val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {

        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerClosed(drawerView: View) {

        }

        override fun onDrawerOpened(drawerView: View) {
            mListener?.getQuery()?.let {
                advanceNavigationSearchEt.setText(it)
            }
        }
    }


    var filter: BaseSearchFilterModel? = null
        get() {
            return when (advanceSearchTypeSpinner.selectedItemPosition) {
                AdvanceSearchTypes.ANIME.ordinal, AdvanceSearchTypes.MANGA.ordinal -> {
                    MediaSearchFilterModel().apply {
                        query = advanceNavigationSearchEt?.text?.toString()
                        season = advanceSearchSeasonSpinner?.selectedItemPosition?.minus(1)
                            ?.takeIf { it >= 0 }

                        type = advanceSearchTypeSpinner.selectedItemPosition
                        yearEnabled = enableYearCheckBox.isChecked
                        if (yearEnabled) {
                            minYear = searchYearSeekbar?.minProgress?.toInt()
                            maxYear = searchYearSeekbar?.maxProgress?.toInt()
                        }

                        sort = advanceSearchCountrySpinner?.selectedItemPosition?.minus(1)?.takeIf {
                            it >= 0
                        }

                        format =
                            advanceSearchFormatSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                                it >= 0
                            }

                        status =
                            advanceSearchStatusSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                                it >= 0
                            }

                        streamingOn = streamingTagMap!!.values.filter { it.isTagged }.map { it.tag }
                        countryOfOrigin =
                            advanceSearchCountrySpinner?.selectedItemPosition?.minus(1)?.takeIf {
                                it >= 0
                            }
                        source =
                            advanceSearchSourceSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                                it >= 0
                            }
                        genre = genreTagMap!!.values.filter { it.isTagged }.map { it.tag }
                        tags = tagTagMap!!.values.filter { it.isTagged }.map { it.tag }
                    }
                }
                AdvanceSearchTypes.CHARACTER.ordinal -> {
                    CharacterSearchFilterModel().apply {
                        query = advanceNavigationSearchEt?.text?.toString()
                    }
                }
                AdvanceSearchTypes.STAFF.ordinal -> {
                    StaffSearchFilterModel().apply {
                        query = advanceNavigationSearchEt?.text?.toString()
                    }
                }
                AdvanceSearchTypes.STUDIO.ordinal -> {
                    StudioSearchFilterModel().apply {
                        query = advanceNavigationSearchEt?.text?.toString()
                    }
                }
                else -> {
                    null
                }
            }
        }
        set(value) {
            field = value
            when (value) {
                is MediaSearchFilterModel -> {
                    value.let {
                        advanceNavigationSearchEt?.setText(value.query ?: "")
                        it.type?.let {
                            advanceSearchTypeSpinner?.setSelection(it)
                        }
                        it.season?.let {
                            advanceSearchSeasonSpinner?.setSelection(it + 1)
                        }
                        if (it.yearEnabled) {
                            searchYearSeekbar?.setProgress(
                                it.minYear!!.toFloat(),
                                it.maxYear!!.toFloat()
                            )
                        }
                        it.sort?.let {
                            advanceSearchSortSpinner?.setSelection(it + 1)
                        }
                        it.format?.let {
                            advanceSearchFormatSpinner?.setSelection(it + 1)
                        }
                        it.status?.let {
                            advanceSearchStatusSpinner?.setSelection(it + 1)
                        }
                        it.streamingOn?.forEach {
                            streamingTagMap!![it]?.isTagged = true
                        }

                        it.countryOfOrigin?.let {
                            advanceSearchCountrySpinner.setSelection(it + 1)
                        }
                        it.source?.let {
                            advanceSearchSortSpinner.setSelection(it + 1)
                        }
                        it.genre?.forEach {
                            genreTagMap!![it]?.isTagged = true
                        }
                        it.tags?.forEach {
                            tagTagMap!![it]?.isTagged = true
                        }

                        mListener?.updateTags()
                        mListener?.updateGenre()
                        mListener?.updateStream()

                    }
                }
                is CharacterSearchFilterModel -> {
                    advanceNavigationSearchEt?.setText(value.query ?: "")
                }
                is StaffSearchFilterModel -> {
                    advanceNavigationSearchEt?.setText(value.query ?: "")
                }
                is StudioSearchFilterModel -> {
                    advanceNavigationSearchEt?.setText(value.query ?: "")
                }
            }
        }


//    private var seekBarMode = RangeSeekBar.SEEKBAR_MODE_RANGE
//        set(value) {
//            field = value
//            searchYearIndicator.seekBarMode = value
//            searchYearIndicator.invalidate()
//            yearRangeToggleButton.checked = value == RangeSeekBar.SEEKBAR_MODE_RANGE
//        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        addView(rView)
        rView.advanceNavigationSearchInputLayout.apply {
            this.setEndIconTintList(ColorStateList.valueOf(DynamicTheme.getInstance().get().tintAccentColor))
            this.setStartIconTintList(ColorStateList.valueOf(DynamicTheme.getInstance().get().accentColorDark))
        }
        updateTheme(rView)
        updateView(rView)
        updateRecyclerView(rView)
        updateListener(rView)
    }

    private fun updateRecyclerView(rView: View) {
        rView.apply {
            streamingOnRecyclerView.layoutManager = FlexboxLayoutManager(context)
            genreRecyclerView.layoutManager = FlexboxLayoutManager(context)
            tagRecyclerView.layoutManager = FlexboxLayoutManager(context)
        }
    }


    fun buildStreamAdapter(builder: Adapter.Builder, list: List<TagField>) {
        list.forEach {
            streamingTagMap!![it.tag]?.isTagged = it.isTagged
        }
        invalidateStreamAdapter(builder)
    }

    fun buildGenreAdapter(builder: Adapter.Builder, list: List<TagField>) {
        list.forEach {
            genreTagMap!![it.tag]?.isTagged = it.isTagged
        }
        invalidateGenreAdapter(builder)
    }


    fun buildTagAdapter(builder: Adapter.Builder, list: List<TagField>) {
        list.forEach {
            tagTagMap!![it.tag]?.isTagged = it.isTagged
        }
        invalidateTagAdapter(builder)
    }


    fun invalidateGenreAdapter(builder: Adapter.Builder) {
        genreAdapter = builder
            .addSource(
                Source.fromList(genreTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(genrePresenter)
            .into(genreRecyclerView)
    }


    fun invalidateTagAdapter(builder: Adapter.Builder) {
        tagAdapter = builder
            .addSource(
                Source.fromList(tagTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(genrePresenter)
            .into(tagRecyclerView)
    }

    fun invalidateStreamAdapter(builder: Adapter.Builder) {
        streamAdapter = builder
            .addSource(
                Source.fromList(streamingTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(genrePresenter)
            .into(streamingOnRecyclerView)
    }


    fun updateView(rView: View) {
        val searchTypeItems: List<DynamicSpinnerItem>
        val searchSeasonItems: List<DynamicSpinnerItem>
        val searchSortItems: List<DynamicSpinnerItem>
        val searchFormatItems: List<DynamicSpinnerItem>
        val searchStatusItems: List<DynamicSpinnerItem>
        val searchSourceItems: List<DynamicSpinnerItem>
        val searchCountryItems: List<DynamicSpinnerItem>

        rView.apply {
            searchTypeItems = context.resources.getStringArray(R.array.advance_search_type).map {
                DynamicSpinnerItem(
                    null, it
                )
            }
            searchSeasonItems =
                context.resources.getStringArray(R.array.advance_search_season).map {
                    DynamicSpinnerItem(
                        null, it
                    )
                }
            searchSortItems = context.resources.getStringArray(R.array.advance_search_sort).map {
                DynamicSpinnerItem(
                    null, it
                )
            }

            searchFormatItems =
                context.resources.getStringArray(R.array.advance_search_format).map {
                    DynamicSpinnerItem(
                        null, it
                    )
                }

            searchStatusItems =
                context.resources.getStringArray(R.array.advance_search_status).map {
                    DynamicSpinnerItem(
                        null, it
                    )
                }

            searchSourceItems =
                context.resources.getStringArray(R.array.advance_search_source).map {
                    DynamicSpinnerItem(
                        null, it
                    )
                }
            searchCountryItems =
                context.resources.getStringArray(R.array.advance_search_country).map {
                    DynamicSpinnerItem(
                        null, it
                    )
                }

            advanceSearchTypeSpinner.adapter = makeSpinnerAdapter(searchTypeItems)
            advanceSearchSeasonSpinner.adapter = makeSpinnerAdapter(searchSeasonItems)
            advanceSearchSortSpinner.adapter = makeSpinnerAdapter(searchSortItems)
            advanceSearchFormatSpinner.adapter = makeSpinnerAdapter(searchFormatItems)
            advanceSearchStatusSpinner.adapter = makeSpinnerAdapter(searchStatusItems)
            advanceSearchSourceSpinner.adapter = makeSpinnerAdapter(searchSourceItems)
            advanceSearchCountrySpinner.adapter = makeSpinnerAdapter(searchCountryItems)

            searchYearSeekbar.isEnabled = enableYearCheckBox.isChecked
            val currentYear = Calendar.getInstance().get(Calendar.YEAR) + 1f
            searchYearSeekbar.setRange(1950f, currentYear)
            searchYearSeekbar.setProgress(1950f, currentYear)
            yearTv.text =
                if (enableYearCheckBox.isChecked)
                    context.getString(R.string.year)
                else
                    context.getString(R.string.year_disabled)
            searchYearSeekbar.progressLeft = 1950

        }

    }

    private fun View.makeSpinnerAdapter(items: List<DynamicSpinnerItem>) =
        DynamicSpinnerImageAdapter(
            context,
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, items
        )

    private fun updateTheme(rView: View) {
        rView.apply {
            ThemeController.lightSurfaceColor().let {
                searchTypeFrameLayout.setBackgroundColor(it)
                searchSeasonFrameLayout.setBackgroundColor(it)
                searchSortFrameLayout.setBackgroundColor(it)
                searchFormatFrameLayout.setBackgroundColor(it)
                searchStatusFrameLayout.setBackgroundColor(it)
                searchSourceFrameLayout.setBackgroundColor(it)
                searchCountryFrameLayout.setBackgroundColor(it)
                searchStreamingFrameLayout.setBackgroundColor(it)
                genreFrameLayout.setBackgroundColor(it)
                tagFrameLayout.setBackgroundColor(it)
                tagAddIv.background = rippleDrawable
                genreAddIv.background = rippleDrawable
                streamAddIv.background = rippleDrawable

                searchYearSeekbar.setIndicatorTextDecimalFormat("0")
                searchYearSeekbar.setTypeface(
                    ResourcesCompat.getFont(
                        context,
                        R.font.open_sans_light
                    )
                )

            }

            DynamicTheme.getInstance().get().accentColor.let {
                searchYearSeekbar.progressColor = it
                searchYearSeekbar.leftSeekBar?.indicatorBackgroundColor = it
                searchYearSeekbar.rightSeekBar?.indicatorBackgroundColor = it
            }
        }
    }


    private fun updateListener(rView: View) {
        rView.apply {
            enableYearCheckBox.setOnCheckedChangeListener { _, isChecked ->
                searchYearSeekbar.isEnabled = isChecked
                yearTv.text =
                    if (isChecked)
                        context.getString(R.string.year)
                    else
                        context.getString(R.string.year_disabled)
            }

//            yearRangeToggleButton.setToggleListener {
//                seekBarMode =
//                    if (it) RangeSeekBar.SEEKBAR_MODE_RANGE else RangeSeekBar.SEEKBAR_MODE_SINGLE
//            }

            advanceSearchTypeSpinner.onItemSelected {
                if (it == 0 || it == 1) {
                    advanceMediaFilterContainer.visibility = View.VISIBLE

                } else {
                    advanceMediaFilterContainer.visibility = View.GONE
                }
            }

            streamAddIv.setOnClickListener {
                mListener?.onStreamAdd(streamingTagMap!!.values.toList())
            }

            genreAddIv.setOnClickListener {
                mListener?.onGenreAdd(genreTagMap!!.values.toList())
            }

            tagAddIv.setOnClickListener {
                mListener?.onTagAdd(tagTagMap!!.values.toList())
            }



            applyFilterCardView.setOnClickListener {
                applyFilter()
            }
        }
    }

    private fun View.applyFilter() {
        mListener?.applyFilter()
    }


    fun setNavigationCallbackListener(listener: AdvanceBrowseNavigationCallbackListener) {
        mListener = listener
    }

    interface AdvanceBrowseNavigationCallbackListener {
        fun onGenreAdd(tags: List<TagField>)
        fun onStreamAdd(tags: List<TagField>)
        fun onTagAdd(tags: List<TagField>)
        fun updateGenre()
        fun updateTags()
        fun updateStream()
        fun getQuery(): String
        fun applyFilter()
    }


}