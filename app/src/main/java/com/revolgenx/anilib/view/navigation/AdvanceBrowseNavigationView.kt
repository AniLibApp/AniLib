package com.revolgenx.anilib.view.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.jaygoo.widget.RangeSeekBar
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.presenter.TagPresenter
import com.revolgenx.anilib.util.onItemSelected
import kotlinx.android.synthetic.main.advance_browse_navigation_view.view.*


//todo://search year not working after rotation
class AdvanceBrowseNavigationView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicNavigationView(context, attributeSet, style) {

    private var mListener: AdvanceBrowseNavigationCallbackListener? = null
    private val rView by lazy {
        LayoutInflater.from(context).inflate(
            R.layout.advance_browse_navigation_view,
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
        TagPresenter(context)
    }
    private val genrePresenter by lazy {
        TagPresenter(context)
    }
    private val tagPresenter by lazy {
        TagPresenter(context)
    }

    val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {
        }

        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerClosed(drawerView: View) {
        }

        override fun onDrawerOpened(drawerView: View) {
            mListener?.getQuery()
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
        initListener(rView)
    }

    private fun updateRecyclerView(rView: View) {
        rView.apply {
            streamingOnRecyclerView.layoutManager = FlexboxLayoutManager(context)
            genreRecyclerView.layoutManager = FlexboxLayoutManager(context)
        }
    }


    fun buildStreamAdapter(builder: Adapter.Builder) {
        streamAdapter = builder.addPresenter(streamPresenter)
            .into(streamingOnRecyclerView)
    }

    fun buildGenreAdapter(builder: Adapter.Builder) {
        genreAdapter = builder.addPresenter(genrePresenter)
            .into(genreRecyclerView)
    }

    fun buildTagAdapter(builder: Adapter.Builder) {
        tagAdapter = builder.addPresenter(tagPresenter)
            .into(tagRecyclerView)
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

//            seekBarMode = seekBarMode

            searchYearIndicator.isEnabled = enableYearCheckBox.isChecked
            yearTv.text =
                if (enableYearCheckBox.isChecked)
                    context.getString(R.string.year)
                else
                    context.getString(R.string.year_disabled)

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

                searchYearIndicator.setIndicatorTextDecimalFormat("0")
                searchYearIndicator.setTypeface(
                    ResourcesCompat.getFont(
                        context,
                        R.font.open_sans_light
                    )
                )

            }

            DynamicTheme.getInstance().get().accentColor.let {
                searchYearIndicator.progressColor = it
                searchYearIndicator.leftSeekBar?.indicatorBackgroundColor = it
                searchYearIndicator.rightSeekBar?.indicatorBackgroundColor = it
            }
        }
    }


    private fun initListener(rView: View) {
        rView.apply {
            enableYearCheckBox.setOnCheckedChangeListener { _, isChecked ->
                searchYearIndicator.isEnabled = isChecked
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
                mListener?.onStreamAdd()
            }

            genreAddIv.setOnClickListener {
                mListener?.onGenreAdd()

            }

            tagAddIv.setOnClickListener {
                mListener?.onTagAdd()
            }
        }
    }

    fun setNavigationCallbackListener(listener: AdvanceBrowseNavigationCallbackListener) {
        mListener = listener
    }


    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).also {
            //            it.seekBarMode = seekBarMode

        }
    }

    override fun onRestoreInstanceState(savedState: Parcelable?) {
        super.onRestoreInstanceState(savedState)
        if (savedState is SavedState) {
//            seekBarMode = savedState.seekBarMode
        }
    }

    internal class SavedState : BaseSavedState {
        var seekBarMode = RangeSeekBar.SEEKBAR_MODE_SINGLE

        constructor(parcelable: Parcelable?) : super(parcelable)
        constructor(parcel: Parcel) : super(parcel) {
            seekBarMode = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(seekBarMode)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    interface AdvanceBrowseNavigationCallbackListener {
        fun onGenreAdd()
        fun onStreamAdd()
        fun onTagAdd()
        fun getQuery(): String
    }
}