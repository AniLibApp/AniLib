package com.revolgenx.anilib.fragment.browse

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.field.media.MediaStatsField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.user.stats.MediaStatsModel
import com.revolgenx.anilib.presenter.RankingsPresenter
import com.revolgenx.anilib.repository.util.Status.*
import com.revolgenx.anilib.viewmodel.MediaStatsViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.media_stats_fragment_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class MediaStatsFragment : BaseFragment() {
    val viewModel by viewModel<MediaStatsViewModel>()
    private var mediaBrowserMeta: MediaBrowserMeta? = null

    private val field by lazy {
        MediaStatsField().also { f ->
            f.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    private var visibleToUser = false

    private val mediaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.media_list_status)
    }

    private val rankingsPresenter by lazy {
        RankingsPresenter(requireContext(), mediaBrowserMeta!!.type)
    }

    private val listOfScores by lazy {
        listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
    }

    companion object {
        const val visibleToUserKey = "visibleToUserKey"
        private val barColors by lazy {
            listOf(
                Color.parseColor("#ff8a65"),
                Color.parseColor("#ffab91"),
                Color.parseColor("#ffccbc"),
                Color.parseColor("#ffe0b2"),
                Color.parseColor("#ffe082"),
                Color.parseColor("#ffcc80"),
                Color.parseColor("#e6ee9c"),
                Color.parseColor("#c5e1a5"),
                Color.parseColor("#a5d6a7"),
                Color.parseColor("#81c784")
            )
        }
        private val pieColors by lazy {
            listOf(
                Color.parseColor("#bbdefb"),//current
                Color.parseColor("#d1c4e9"),//planning
                Color.parseColor("#dcedc8"),//completed
                Color.parseColor("#f8bbd0"),//dropped
                Color.parseColor("#fbe9e7"),//paused
                Color.parseColor("#FFD28C")//repeating
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_stats_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankingRecyclerView.layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY;
            manager.alignItems = AlignItems.FLEX_START;
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)

        visibleToUser = savedInstanceState?.getBoolean(visibleToUserKey) ?: false

        viewModel.statsLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    updateView(res.data!!)
                    errorLayout.visibility = View.GONE
                }
                ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            viewModel.getStats(field)
        }
        visibleToUser = true
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(visibleToUserKey, visibleToUser)
        super.onSaveInstanceState(outState)
    }

    private fun updateView(data: MediaStatsModel) {
        context ?: return


        data.rankings?.let {
            Adapter.builder(viewLifecycleOwner)
                .addSource(Source.fromList(it))
                .addPresenter(rankingsPresenter)
                .into(rankingRecyclerView)
        }

        data.trendsEntry?.let { entries ->
            LineDataSet(entries, "").apply {
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                lineWidth = 2f
                valueTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
                color = DynamicTheme.getInstance().get().tintAccentColor
                fillColor = DynamicTheme.getInstance().get().tintAccentColor
                fillAlpha = 255
                setDrawCircleHole(false)
                setDrawCircles(false)
                setDrawFilled(true)
            }.let { set ->
                activityPerDayLineChart.let { perDay ->
                    perDay.setTouchEnabled(false)
                    perDay.axisRight.isEnabled = false
                    perDay.setGridBackgroundColor(DynamicTheme.getInstance().get().tintAccentColor)
                    perDay.axisLeft.let { left ->
                        left.setDrawLabels(true); // no axis labels
                        left.setDrawAxisLine(false); // no axis line
                        left.setDrawGridLines(true); // no grid lines
                        left.setDrawZeroLine(false)
                        left.isGranularityEnabled = false
                        left.axisMinimum = 0f
                        left.labelCount = 4
                        left.typeface =
                            ResourcesCompat.getFont(requireContext(), R.font.open_sans_light)
                        left.textSize = 10f
                        left.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    }

                    perDay.xAxis.apply {
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return LocalDateTime.ofInstant(
                                    Instant.ofEpochSecond(value.toLong()),
                                    ZoneId.systemDefault()
                                ).dayOfMonth.toString()
                            }
                        }
                        position = XAxis.XAxisPosition.BOTTOM
                        gridLineWidth = 2f
                        typeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_light)
                        textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    }

                    perDay.description = null

                    perDay.data = LineData(set)
                    perDay.invalidate()
                }
            }
        }


        val statusList = data.statusDistribution?.map { it.status!! } ?: emptyList()

        mediaListStatus.mapIndexed { index, s ->
            if (statusList.contains(index)) {
                PieEntry(data.statusDistribution?.get(index)?.amount?.toFloat() ?: 0f, s)
            } else {
                PieEntry(0f, s)
            }
        }.let {
            val dataSet = PieDataSet(it, getString(R.string.status_distribution)).also {
                it.colors = pieColors
            }
            dataSet.sliceSpace = 3f

            val color = DynamicTheme.getInstance().get().backgroundColor
            statusDistributionPieChart.setHoleColor(color)
            statusDistributionPieChart.setTransparentCircleColor(color)
            statusDistributionPieChart.setTransparentCircleAlpha(110)
            statusDistributionPieChart.holeRadius = 58f;
            statusDistributionPieChart.transparentCircleRadius = 61f;
            statusDistributionPieChart.rotationAngle = 0f
            statusDistributionPieChart.setDrawEntryLabels(false)
            statusDistributionPieChart.legend?.let { l ->
                l.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                l.verticalAlignment = Legend.LegendVerticalAlignment.TOP;
                l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT;
                l.orientation = Legend.LegendOrientation.VERTICAL;
                l.setDrawInside(false)
                l.xEntrySpace = 7f
                l.yEntrySpace = 0f
                l.yOffset = 0f
            }

            statusDistributionPieChart.setExtraOffsets(-40f, 0f, 0f, 0f)
            statusDistributionPieChart.setEntryLabelTypeface(
                ResourcesCompat.getFont(
                    requireContext(),
                    R.font.open_sans_light
                )
            )
            statusDistributionPieChart.description = null
            statusDistributionPieChart.setEntryLabelTextSize(12f)
            statusDistributionPieChart.data = PieData(dataSet)
            statusDistributionPieChart.invalidate()
        }

        val scores = data.scoreDistribution?.map { it.score!! } ?: emptyList()

        listOfScores.mapIndexed { index, score ->
            if (scores.contains(score)) {
                BarEntry(
                    score.toFloat(),
                    data.scoreDistribution?.get(scores.indexOf(score))?.amount?.toFloat() ?: 0f
                )
            } else {
                BarEntry(score.toFloat(), 0f)
            }
        }.let {
            val dataSet = BarDataSet(it, getString(R.string.score_distribution)).also {
                it.colors = barColors
                it.valueTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
            }
            scoreDistributionBarChart.apply {
                legend.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                xAxis.let { axis ->
                    axis.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.open_sans_regular)
                    axis.position = XAxis.XAxisPosition.BOTTOM
                    axis.setDrawGridLines(false)
                    axis.setDrawAxisLine(false)
                    axis.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.open_sans_light)
                    axis.textSize = 10f
                    axis.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    axis.labelCount = 10
                }

                setTouchEnabled(false)
                description = null
                scoreDistributionBarChart.data = BarData(dataSet).apply {
                    barWidth = 4f
                    xAxis.spaceMin = barWidth / 2f
                    xAxis.spaceMax = barWidth / 2f
                }
                invalidate()
            }

        }
    }
}
