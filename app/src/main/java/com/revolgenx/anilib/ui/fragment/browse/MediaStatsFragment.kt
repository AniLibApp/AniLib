package com.revolgenx.anilib.ui.fragment.browse

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.data.field.media.MediaStatsField
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.user.stats.MediaStatsModel
import com.revolgenx.anilib.databinding.MediaStatsFragmentLayoutBinding
import com.revolgenx.anilib.ui.presenter.RankingsPresenter
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.ui.viewmodel.media.MediaStatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class MediaStatsFragment : BaseLayoutFragment<MediaStatsFragmentLayoutBinding>() {
    val viewModel by viewModel<MediaStatsViewModel>()
    private var mediaBrowserMeta: MediaBrowserMeta? = null

    private val field by lazy {
        MediaStatsField().also { f ->
            f.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    private val mediaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.media_list_status)
    }

    private val rankingsPresenter by lazy {
        RankingsPresenter(requireContext(), mediaBrowserMeta!!.type)
    }

    private var rankingAdapter:Adapter? = null

    private val listOfScores by lazy {
        listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
    }

    private val barColors by lazy {
        requireContext().resources.getStringArray(R.array.bar_color).map { Color.parseColor(it) }
    }

    private val pieColors by lazy {
        requireContext().resources.getStringArray(R.array.media_list_status_color).map { Color.parseColor(it) }
    }
    companion object {
        const val visibleToUserKey = "visibleToUserKey"


    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaStatsFragmentLayoutBinding {
        return MediaStatsFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        binding.rankingRecyclerView.layoutManager =  GridLayoutManager(
            this.context,
            span
        ).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (rankingAdapter?.getItemViewType(position) == 0) {
                        1
                    } else {
                        span
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)

        visibleToUser = savedInstanceState?.getBoolean(visibleToUserKey) ?: false

        val statusLayout = binding.resourceStatusLayout
        viewModel.statsLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    binding.updateView(res.data!!)
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
                ERROR -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
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

    private fun MediaStatsFragmentLayoutBinding.updateView(data: MediaStatsModel) {
        context ?: return


        data.rankings?.let {
            rankingAdapter = Adapter.builder(viewLifecycleOwner)
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
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
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
                            ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
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
                        typeface = ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
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
                    R.font.cabincondensed_regular
                )
            )
            statusDistributionPieChart.description = null
            statusDistributionPieChart.setEntryLabelTextSize(12f)
            statusDistributionPieChart.data = PieData(dataSet)
            statusDistributionPieChart.invalidate()
        }

        val scores = data.scoreDistribution?.map { it.score!! } ?: emptyList()

        listOfScores.map { score ->
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
                it.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
            }
            scoreDistributionBarChart.apply {
                legend.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                xAxis.let { axis ->
                    axis.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.cabin_regular)
                    axis.position = XAxis.XAxisPosition.BOTTOM
                    axis.setDrawGridLines(false)
                    axis.setDrawAxisLine(false)
                    axis.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
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
