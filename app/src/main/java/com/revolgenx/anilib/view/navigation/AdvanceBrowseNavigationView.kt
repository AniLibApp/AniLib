package com.revolgenx.anilib.view.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.jaygoo.widget.RangeSeekBar
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicNavigationView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.util.onItemSelected
import kotlinx.android.synthetic.main.advance_browse_navigation_view.view.*


//todo://search year not working after rotation
class AdvanceBrowseNavigationView(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicNavigationView(context, attributeSet, style) {

    private val rView by lazy {
        LayoutInflater.from(context).inflate(
            R.layout.advance_browse_navigation_view,
            null,
            false
        )
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
        addView(
            rView
        )
        rView.advanceNavigationSearchInputLayout.apply {
            this.setEndIconTintList(ColorStateList.valueOf(DynamicTheme.getInstance().get().tintAccentColor))
            this.setStartIconTintList(ColorStateList.valueOf(DynamicTheme.getInstance().get().accentColorDark))
        }

        updateTheme(rView)
        updateView(rView)
        initListener(rView)
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

        }
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
}