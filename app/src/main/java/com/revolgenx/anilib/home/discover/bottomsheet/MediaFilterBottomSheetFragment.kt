package com.revolgenx.anilib.home.discover.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.common.preference.getPopularField
import com.revolgenx.anilib.common.preference.getSeasonField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.constant.AlMediaSort
import com.revolgenx.anilib.databinding.MediaFilterBottomSheetLayoutBinding
import com.revolgenx.anilib.home.discover.data.field.NewlyAddedMediaField
import com.revolgenx.anilib.home.discover.data.field.PopularMediaField
import com.revolgenx.anilib.home.discover.data.field.TrendingMediaField
import com.revolgenx.anilib.home.season.data.field.SeasonField
import com.revolgenx.anilib.media.data.field.MediaField
import com.revolgenx.anilib.ui.adapter.MediaFilterFormatAdapter
import com.revolgenx.anilib.ui.dialog.FormatSelectionDialog
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.onItemSelected
import java.util.*
import kotlin.math.round

class MediaFilterBottomSheetFragment :
    DynamicBottomSheetFragment<MediaFilterBottomSheetLayoutBinding>() {

    var onDoneListener: (() -> Unit)? = null

    companion object {
        const val mediaFilterTypeKey = "mediaFilterTypeKey"
    }

    private var formatDialog: DialogFragment? = null

    fun show(
        ctx: Context,
        func: MediaFilterBottomSheetFragment.() -> Unit
    ): MediaFilterBottomSheetFragment {
        this.windowContext = ctx
        this.func()
        this.show()
        return this
    }

    private val mediaFilterType
        get() =
            arguments?.getInt(mediaFilterTypeKey)

    private val field: MediaField by lazy {
        when (mediaFilterType) {
            MediaFilterType.SEASON.ordinal -> {
                getSeasonField(requireContext())
            }
            MediaFilterType.TRENDING.ordinal -> {
                getTrendingField(requireContext())
            }
            MediaFilterType.POPULAR.ordinal -> {
                getPopularField(requireContext())
            }
            MediaFilterType.NEWLY_ADDED.ordinal -> {
                getNewlyAddedField(requireContext())
            }
            else -> {
                MediaField()
            }
        }
    }

    private val listStatusItems
        get() =
            requireContext().resources.getStringArray(R.array.advance_search_status).map {
                DynamicMenu(
                    null, it
                )
            }

    private val alMediaSortList by lazy {
        requireContext().resources.getStringArray(R.array.al_media_sort)
    }

    private val seasons
        get() =
            requireContext().resources.getStringArray(if (mediaFilterType == MediaFilterType.SEASON.ordinal) R.array.media_season else R.array.advance_search_season)
                .map {
                    DynamicMenu(
                        null, it
                    )
                }

    private val adapter by lazy {
        MediaFilterFormatAdapter(requireContext())
    }

    enum class MediaFilterType {
        SEASON, TRENDING, POPULAR, NEWLY_ADDED
    }

    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MediaFilterBottomSheetLayoutBinding {
        return MediaFilterBottomSheetLayoutBinding.inflate(inflater, container, false)
    }

    private fun onDone() {
        field.formatsIn = adapter.currentList
        when (field) {
            is SeasonField -> (field as SeasonField).saveSeasonField(requireContext())
            is TrendingMediaField -> {
                if (!binding.enableYearCheckBox.isChecked) {
                    field.seasonYear = null
                }
                (field as TrendingMediaField).saveTrendingField(requireContext())
            }
            is PopularMediaField -> {
                if (!binding.enableYearCheckBox.isChecked) {
                    field.seasonYear = null
                }
                (field as PopularMediaField).savePopularField(requireContext())
            }
            is NewlyAddedMediaField -> {
                if (!binding.enableYearCheckBox.isChecked) {
                    field.seasonYear = null
                }
                (field as NewlyAddedMediaField).saveNewlyAddedField(requireContext())
            }
        }
        onDoneListener?.invoke()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(binding) {

            field.formatsIn = field.formatsIn ?: mutableListOf()

            formatChipRecyclerView.layoutManager = FlexboxLayoutManager(requireContext())
            bindFormat()

            formatAddHeader.setOnClickListener {
                formatDialog = FormatSelectionDialog.newInstance(field.formatsIn!!).also {
                    it.onDoneListener = {
                        field.formatsIn = it.toMutableList()
                        binding.bindFormat()
                    }
                    it.show(childFragmentManager, FormatSelectionDialog::class.java.simpleName)
                }
            }

            seasonStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), listStatusItems)
            seasonSpinner.adapter = makeSpinnerAdapter(requireContext(), seasons)

            if (field is SeasonField) {
                val alMediaSorts = AlMediaSort.values()
                var sortOrder = SortOrder.NONE
                val currentSort = field.sort?.let {
                    if (it < 34) {
                        if (it % 2 == 0) {
                            sortOrder = SortOrder.ASC
                            it
                        } else {
                            sortOrder = SortOrder.DESC
                            it - 1
                        }
                    } else if (it > 34) {
                        if (it % 2 == 0) {
                            sortOrder = SortOrder.DESC
                            it - 1
                        } else {
                            sortOrder = SortOrder.ASC
                            it
                        }
                    } else {
                        null
                    }
                }

                var currentSortIndex = -1
                if (currentSort != null) {
                    currentSortIndex = alMediaSorts.first { it.sort == currentSort }.ordinal
                }

                seasonSortLayout.setSortItems(alMediaSortList.mapIndexed { index, s ->
                    AniLibSortingModel(
                        alMediaSorts[index],
                        s,
                        if (currentSortIndex == index) sortOrder else SortOrder.NONE
                    )
                })

                seasonSortLayout.onSortItemSelected = {
                    field.sort = it?.let {
                        when (it.order) {
                            SortOrder.ASC -> {
                                (it.data as AlMediaSort).sort
                            }
                            SortOrder.DESC -> {
                                (it.data as AlMediaSort).sort + 1
                            }
                            else -> {
                                null
                            }
                        }
                    }
                }

            } else {
                seasonSortHeaderTv.visibility = View.GONE
                seasonSortCardView.visibility = View.GONE
            }

            field.status?.let {
                seasonStatusSpinner.setSelection(it + 1)
            }
            field.season?.let {
                seasonSpinner.setSelection(if (field is SeasonField) it else it + 1)
            }


            seasonYearSeekBar.setIndicatorTextDecimalFormat("0")
            seasonYearSeekBar.setTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.cabincondensed_regular
                )
            )

            seasonYearSeekBar.progressColor = dynamicAccentColor
            seasonYearSeekBar.leftSeekBar?.indicatorBackgroundColor = dynamicAccentColor

            val currentYear = Calendar.getInstance().get(Calendar.YEAR) + 1f

            seasonYearSeekBar.setRange(1950f, currentYear)

            if (field is SeasonField) {
                enableYearCheckBox.visibility = View.GONE
                yearHeaderTv.visibility = View.VISIBLE
                seasonYearSeekBar.visibility = View.VISIBLE
            }else{
                val hasSeasonYear = field.seasonYear != null
                enableYearCheckBox.setOnCheckedChangeListener(null)
                enableYearCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    seasonYearSeekBar.visibility = if (isChecked) View.VISIBLE else View.GONE
                }
                seasonYearSeekBar.visibility = if (hasSeasonYear) View.VISIBLE else View.GONE
                enableYearCheckBox.isChecked = hasSeasonYear
            }

            field.seasonYear?.toFloat()?.let {
                seasonYearSeekBar.setProgress(it)
            }

            seasonStatusSpinner.onItemSelected {
                field.status = it.minus(1).takeIf { it >= 0 }
            }
            seasonSpinner.onItemSelected {
                field.season = if (field is SeasonField) it else it.minus(1).takeIf { it >= 0 }
            }
            seasonYearSeekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                }

                override fun onRangeChanged(
                    view: RangeSeekBar?,
                    leftValue: Float,
                    rightValue: Float,
                    isFromUser: Boolean
                ) {
                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    view?.leftSeekBar?.progress?.let {
                        field.seasonYear = round(it).toInt()
                    }
                }
            })

            onPositiveClicked = {
                onDone()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        formatDialog?.dismissAllowingStateLoss()
        formatDialog = null
    }


    private fun MediaFilterBottomSheetLayoutBinding.bindFormat() {
        formatChipRecyclerView.adapter = adapter

        adapter.currentList = field.formatsIn!!
    }

}