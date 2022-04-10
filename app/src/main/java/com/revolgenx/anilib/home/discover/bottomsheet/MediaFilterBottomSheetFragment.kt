package com.revolgenx.anilib.home.discover.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.flexbox.FlexboxLayoutManager
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getNewlyAddedField
import com.revolgenx.anilib.common.preference.getPopularField
import com.revolgenx.anilib.common.preference.getSeasonField
import com.revolgenx.anilib.common.preference.getTrendingField
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.common.ui.model.SelectableSpinnerMenu
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
import com.revolgenx.anilib.ui.view.makeSelectableSpinnerAdapter
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.onItemSelected
import java.util.*

class MediaFilterBottomSheetFragment :
    DynamicBottomSheetFragment<MediaFilterBottomSheetLayoutBinding>() {

    var onDoneListener: (() -> Unit)? = null

    companion object {
        const val mediaFilterTypeKey = "mediaFilterTypeKey"
    }

    private var formatDialog: DialogFragment? = null

    private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1
    private val yearList by lazy {
        (yearLesser downTo 1940).toList()
    }

    fun show(
        ctx: Context,
        func: MediaFilterBottomSheetFragment.() -> Unit
    ): MediaFilterBottomSheetFragment {
        this.func()
        this.show(ctx)
        return this
    }

    private val mediaFilterType
        get() =
            arguments?.getInt(mediaFilterTypeKey)

    private val field: MediaField by lazy {
        when (mediaFilterType) {
            MediaFilterType.SEASON.ordinal -> {
                getSeasonField()
            }
            MediaFilterType.TRENDING.ordinal -> {
                getTrendingField()
            }
            MediaFilterType.POPULAR.ordinal -> {
                getPopularField()
            }
            MediaFilterType.NEWLY_ADDED.ordinal -> {
                getNewlyAddedField()
            }
            else -> {
                MediaField()
            }
        }
    }


    private val alMediaSortList by lazy {
        requireContext().resources.getStringArray(R.array.al_media_sort)
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
            is SeasonField -> (field as SeasonField).saveSeasonField()
            is TrendingMediaField -> {
                (field as TrendingMediaField).saveTrendingField()
            }
            is PopularMediaField -> {
                (field as PopularMediaField).savePopularField()
            }
            is NewlyAddedMediaField -> {
                (field as NewlyAddedMediaField).saveNewlyAddedField()
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

            val listStatusItems =
                requireContext().resources.getStringArray(R.array.advance_search_status).map {
                    DynamicMenu(null, it)
                }

            val seasonItems =
                requireContext().resources.getStringArray(if (mediaFilterType == MediaFilterType.SEASON.ordinal) R.array.media_season else R.array.advance_search_season)
                    .map {
                        DynamicMenu(null, it)
                    }

            seasonStatusSpinner.adapter = makeSpinnerAdapter(requireContext(), listStatusItems)
            seasonSpinner.adapter = makeSpinnerAdapter(requireContext(), seasonItems)

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


                val seasonYearItems = mutableListOf<SelectableSpinnerMenu>()
                yearList.map {
                    seasonYearItems.add(
                        SelectableSpinnerMenu(
                            it.toString(),
                            it == field.seasonYear
                        )
                    )
                }
                filterYearSpinner.adapter =
                    makeSelectableSpinnerAdapter(requireContext(), seasonYearItems)
                field.seasonYear?.takeIf { it < yearLesser }?.let {
                    filterYearSpinner.setSelection(yearList.indexOf(it))
                }
                filterYearSpinner.onItemSelected {
                    seasonYearItems.firstOrNull{ it.isSelected }?.isSelected = false
                    seasonYearItems[it].isSelected = true
                    field.seasonYear = yearList[it]
                }
            } else {
                seasonSortHeaderTv.visibility = View.GONE
                seasonSortCardView.visibility = View.GONE

                val yearItems = mutableListOf(
                    SelectableSpinnerMenu(
                        getString(R.string.none), field.year == null
                    )
                )
                yearList.map {
                    yearItems.add(
                        SelectableSpinnerMenu(
                            it.toString(),
                            it == field.year
                        )
                    )
                }
                filterYearSpinner.adapter =
                    makeSelectableSpinnerAdapter(requireContext(), yearItems)
                field.year?.takeIf { it < yearLesser }?.let {
                    filterYearSpinner.setSelection(yearList.indexOf(it) + 1)
                }
                filterYearSpinner.onItemSelected {
                    yearItems.firstOrNull{ it.isSelected }?.isSelected = false
                    yearItems[it].isSelected = true
                    field.year = it.takeIf { it > 0 }?.let { yearList[it - 1] }
                }
            }

            field.status?.let {
                seasonStatusSpinner.setSelection(it + 1)
            }
            field.season?.let {
                seasonSpinner.setSelection(if (field is SeasonField) it else it + 1)
            }
            seasonStatusSpinner.onItemSelected {
                field.status = it.minus(1).takeIf { it >= 0 }
            }
            seasonSpinner.onItemSelected {
                field.season = if (field is SeasonField) it else it.minus(1).takeIf { it >= 0 }
            }

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