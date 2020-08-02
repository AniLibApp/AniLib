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
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.preference.getUserGenre
import com.revolgenx.anilib.preference.getUserStream
import com.revolgenx.anilib.preference.getUserTag
import com.revolgenx.anilib.presenter.TagPresenter
import com.revolgenx.anilib.util.hideKeyboard
import com.revolgenx.anilib.util.onItemSelected
import kotlinx.android.synthetic.main.browse_filter_navigation_view.view.*
import java.util.*
import kotlin.math.ceil


class BrowseFilterNavigationView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicNavigationView(context, attributeSet, style) {

    private var mListener: AdvanceBrowseNavigationCallbackListener? = null
    private val rView by lazy {
        LayoutInflater.from(context).inflate(
            R.layout.browse_filter_navigation_view,
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
                mListener?.onStreamRemoved(it)
            }
        }
    }
    private val genrePresenter by lazy {
        TagPresenter(context).also {
            it.tagRemoved {
                genreTagMap!![it]?.isTagged = false
                mListener?.onGenreRemoved(it)
            }
        }
    }
    private val tagPresenter by lazy {
        TagPresenter(context).also {
            it.tagRemoved {
                tagTagMap!![it]?.isTagged = false
                mListener?.onTagRemoved(it)
            }
        }
    }

    private val streamingOnList
        get() =
            getUserStream(context)

    private val tagList
        get() = getUserTag(context)

    private val genreList
        get() =
            getUserGenre(context)

    private var genreTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                genreList.forEach { map[it] = TagField(it, false) }
            }
            return field
        }

    private var tagTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                tagList.forEach { map[it] = TagField(it, false) }
            }
            return field
        }

    private var streamingTagMap: MutableMap<String, TagField>? = null
        get() {
            field = field ?: mutableMapOf<String, TagField>().also { map ->
                streamingOnList.forEach { map[it] = TagField(it, false) }
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
                browseSearchEt.setText(it)
            }
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        addView(rView)
        rView.browseSearchInputLayout.apply {
            this.setEndIconTintList(
                ColorStateList.valueOf(
                    DynamicTheme.getInstance().get().tintAccentColor
                )
            )
            this.setStartIconTintList(
                ColorStateList.valueOf(
                    DynamicTheme.getInstance().get().accentColor
                )
            )
        }
        updateTheme(rView)
        updateView(rView)
        updateRecyclerView(rView)
        updateListener(rView)
    }

    private fun updateRecyclerView(rView: View) {
        rView.apply {
            streamingOnRecyclerView.layoutManager = FlexboxLayoutManager(context)
            seasonGenreRecyclerView.layoutManager = FlexboxLayoutManager(context)
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
            genreTagMap!![it.tag] = it
        }
        invalidateGenreAdapter(builder)
    }


    fun buildTagAdapter(builder: Adapter.Builder, list: List<TagField>) {
        list.forEach {
            tagTagMap!![it.tag]?.isTagged = it.isTagged
        }
        invalidateTagAdapter(builder)
    }

    fun addTagField(list: List<TagField>) {
        list.forEach { tagTagMap!![it.tag] = it }
    }

    fun addGenreField(list: List<TagField>) {
        list.forEach { genreTagMap!![it.tag] = it }

    }

    fun addStreamField(list: List<TagField>) {
        list.forEach { streamingTagMap!![it.tag] = it }
    }

    fun removeTagField(list: List<TagField>) {
        list.forEach { tagTagMap!!.remove(it.tag) }
    }

    fun removeGenreField(list: List<TagField>) {
        list.forEach { genreTagMap!!.remove(it.tag) }
    }

    fun removeStreamField(list: List<TagField>) {
        list.forEach { streamingTagMap!!.remove(it.tag) }
    }


    fun invalidateGenreAdapter(builder: Adapter.Builder) {
        genreAdapter = builder
            .addSource(
                Source.fromList(genreTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(genrePresenter)
            .into(seasonGenreRecyclerView)
    }


    fun invalidateTagAdapter(builder: Adapter.Builder) {
        tagAdapter = builder
            .addSource(
                Source.fromList(tagTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(tagPresenter)
            .into(tagRecyclerView)
    }

    fun invalidateStreamAdapter(builder: Adapter.Builder) {
        streamAdapter = builder
            .addSource(
                Source.fromList(streamingTagMap!!.values.filter { it.isTagged }.map { it.tag })
            )
            .addPresenter(streamPresenter)
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

            browseTypeSpinner.adapter = makeSpinnerAdapter(searchTypeItems)
            browseSeasonSpinner.adapter = makeSpinnerAdapter(searchSeasonItems)
            browseSortSpinner.adapter = makeSpinnerAdapter(searchSortItems)
            browseFormatSpinner.adapter = makeSpinnerAdapter(searchFormatItems)
            browseStatusSpinner.adapter = makeSpinnerAdapter(searchStatusItems)
            browseSourceSpinner.adapter = makeSpinnerAdapter(searchSourceItems)
            browseCountrySpinner.adapter = makeSpinnerAdapter(searchCountryItems)

            browseYearSeekBar.isEnabled = enableYearCheckBox.isChecked
            val currentYear = Calendar.getInstance().get(Calendar.YEAR) + 1f
            browseYearSeekBar.setRange(1950f, currentYear)
            browseYearSeekBar.setProgress(1950f, currentYear)
            yearTv.text =
                if (enableYearCheckBox.isChecked)
                    context.getString(R.string.year)
                else
                    context.getString(R.string.year_disabled)
            browseYearSeekBar.progressLeft = 1950

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
                browseSeasonFrameLayout.setBackgroundColor(it)
                browseSortFrameLayout.setBackgroundColor(it)
                browseFormatFrameLayout.setBackgroundColor(it)
                browseStatusFrameLayout.setBackgroundColor(it)
                browseSourceFrameLayout.setBackgroundColor(it)
                browseCountryFrameLayout.setBackgroundColor(it)
                browseStreamingFrameLayout.setBackgroundColor(it)
                genreFrameLayout.setBackgroundColor(it)
                tagFrameLayout.setBackgroundColor(it)
                tagAddIv.background = rippleDrawable
                genreAddIv.background = rippleDrawable
                streamAddIv.background = rippleDrawable

                browseYearSeekBar.setIndicatorTextDecimalFormat("0")
                browseYearSeekBar.setTypeface(
                    ResourcesCompat.getFont(
                        context,
                        R.font.open_sans_light
                    )
                )

            }

            DynamicTheme.getInstance().get().accentColor.let {
                browseYearSeekBar.progressColor = it
                browseYearSeekBar.leftSeekBar?.indicatorBackgroundColor = it
                browseYearSeekBar.rightSeekBar?.indicatorBackgroundColor = it
            }
        }
    }


    private fun updateListener(rView: View) {
        rView.apply {
            enableYearCheckBox.setOnCheckedChangeListener { _, isChecked ->
                browseYearSeekBar.isEnabled = isChecked
                yearTv.text =
                    if (isChecked)
                        context.getString(R.string.year)
                    else
                        context.getString(R.string.year_disabled)
            }


            browseTypeSpinner.onItemSelected {
                if (it == 0 || it == 1) {
                    browseMediaFilterContainer.visibility = View.VISIBLE

                } else {
                    browseMediaFilterContainer.visibility = View.GONE
                }
            }

            streamAddIv.setOnClickListener {
                mListener?.onStreamChoose(streamingTagMap!!.values.toList())
            }

            genreAddIv.setOnClickListener {
                mListener?.onGenreChoose(genreTagMap!!.values.toList())
            }

            tagAddIv.setOnClickListener {
                mListener?.onTagChoose(tagTagMap!!.values.toList())
            }



            applyFilterCardView.setOnClickListener {
                context.hideKeyboard(browseSearchEt)
                applyFilter()
            }
        }
    }


    fun getFilter(): BrowseFilterModel? {
        return when (browseTypeSpinner.selectedItemPosition) {
            BrowseTypes.ANIME.ordinal, BrowseTypes.MANGA.ordinal -> {
                MediaBrowseFilterModel().apply {
                    query = browseSearchEt?.text?.toString()
                    season = browseSeasonSpinner?.selectedItemPosition?.minus(1)
                        ?.takeIf { it >= 0 }

                    type = browseTypeSpinner.selectedItemPosition
                    yearEnabled = enableYearCheckBox.isChecked
                    if (yearEnabled) {
                        minYear = browseYearSeekBar?.leftSeekBar?.progress?.let {
                            ceil(it).toInt()
                        }
                        maxYear = browseYearSeekBar?.rightSeekBar?.progress?.let {
                            ceil(it).toInt()
                        }
                    }

                    sort = browseSortSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                        it >= 0
                    }
                    format =
                        browseFormatSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                            it >= 0
                        }

                    status =
                        browseStatusSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                            it >= 0
                        }

                    streamingOn = streamingTagMap!!.values.filter { it.isTagged }.map { it.tag }
                    countryOfOrigin =
                        browseCountrySpinner?.selectedItemPosition?.minus(1)?.takeIf {
                            it >= 0
                        }
                    source =
                        browseSourceSpinner?.selectedItemPosition?.minus(1)?.takeIf {
                            it >= 0
                        }
                    genre = genreTagMap!!.values.filter { it.isTagged }.map { it.tag }
                    tags = tagTagMap!!.values.filter { it.isTagged }.map { it.tag }
                }
            }
            BrowseTypes.CHARACTER.ordinal -> {
                CharacterBrowseFilterModel().apply {
                    query = browseSearchEt?.text?.toString()
                }
            }
            BrowseTypes.STAFF.ordinal -> {
                StaffBrowseFilterModel().apply {
                    query = browseSearchEt?.text?.toString()
                }
            }
            BrowseTypes.STUDIO.ordinal -> {
                StudioBrowseFilterModel().apply {
                    query = browseSearchEt?.text?.toString()
                }
            }
            else -> {
                null
            }
        }
    }

    fun setFilter(value: BrowseFilterModel, applyFilter: Boolean = true) {
        browseTypeSpinner?.setSelection(value.type)
        when (value) {
            is MediaBrowseFilterModel -> {
                value.let {
                    browseSearchEt?.setText(value.query ?: "")
                    it.season?.let {
                        browseSeasonSpinner?.setSelection(it + 1)
                    }
                    enableYearCheckBox.isChecked = it.yearEnabled
                    browseYearSeekBar.isEnabled = it.yearEnabled

                    if (it.minYear != null && it.maxYear != null)
                        browseYearSeekBar?.setProgress(
                            it.minYear!!.toFloat(),
                            it.maxYear!!.toFloat()
                        )
                    it.sort?.let {
                        browseSortSpinner?.setSelection(it + 1)
                    }
                    it.format?.let {
                        browseFormatSpinner?.setSelection(it + 1)
                    }
                    it.status?.let {
                        browseStatusSpinner?.setSelection(it + 1)
                    }
                    it.streamingOn?.forEach {
                        streamingTagMap!![it]?.isTagged = true
                    }

                    it.countryOfOrigin?.let {
                        browseCountrySpinner.setSelection(it + 1)
                    }
                    it.source?.let {
                        browseSourceSpinner.setSelection(it + 1)
                    }
                    it.genre?.mapNotNull {
                        genreTagMap!![it]?.isTagged = true
                        genreTagMap!![it]
                    }?.let {
                        mListener?.onGenreAdd(it)
                    }

                    it.tags?.mapNotNull {
                        tagTagMap!![it]?.isTagged = true
                        tagTagMap!![it]
                    }?.let {
                        mListener?.onTagAdd(it)
                    }

                    mListener?.updateTags()
                    mListener?.updateGenre()
                    mListener?.updateStream()
                }
            }
            is CharacterBrowseFilterModel -> {
                browseSearchEt?.setText(value.query ?: "")
            }
            is StaffBrowseFilterModel -> {
                browseSearchEt?.setText(value.query ?: "")
            }
            is StudioBrowseFilterModel -> {
                browseSearchEt?.setText(value.query ?: "")
            }
        }
        if (applyFilter)
            applyFilter()
    }

    private fun applyFilter() {
        mListener?.applyFilter()
    }


    fun setNavigationCallbackListener(listener: AdvanceBrowseNavigationCallbackListener) {
        mListener = listener
    }

    interface AdvanceBrowseNavigationCallbackListener {
        fun onGenreChoose(tags: List<TagField>)
        fun onStreamChoose(tags: List<TagField>)
        fun onTagChoose(tags: List<TagField>)
        fun onGenreAdd(tags: List<TagField>)
        fun onStreamAdd(tags: List<TagField>)
        fun onTagAdd(tags: List<TagField>)
        fun onTagRemoved(tag: String)
        fun onGenreRemoved(tag: String)
        fun onStreamRemoved(tag: String)
        fun updateGenre()
        fun updateTags()
        fun updateStream()
        fun getQuery(): String
        fun applyFilter()
    }


}