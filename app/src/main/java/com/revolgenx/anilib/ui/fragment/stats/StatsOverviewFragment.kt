package com.revolgenx.anilib.ui.fragment.stats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.themeIt
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.data.meta.UserStatsMeta
import com.revolgenx.anilib.data.model.user.stats.StatsCountryDistributionModel
import com.revolgenx.anilib.data.model.user.stats.StatsFormatDistributionModel
import com.revolgenx.anilib.data.model.user.stats.StatsOverviewModel
import com.revolgenx.anilib.data.model.user.stats.StatsStatusDistributionModel
import com.revolgenx.anilib.databinding.StatsDistributionRecyclerLayoutBinding
import com.revolgenx.anilib.databinding.StatsOverviewFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.ui.viewmodel.stats.StatsOverviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//todo: add piechart in distribution
class StatsOverviewFragment : BaseLayoutFragment<StatsOverviewFragmentLayoutBinding>() {

    private val viewModel by viewModel<StatsOverviewViewModel>()
    private lateinit var userStatsMeta: UserStatsMeta

    private var overviewModel: StatsOverviewModel? = null

    private val mediaFormat by lazy {
        requireContext().resources.getStringArray(R.array.media_format)
    }
    private val mediaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.media_list_status)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): StatsOverviewFragmentLayoutBinding {
        return StatsOverviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onResume() {
        super.onResume()
        if(!visibleToUser){
            viewModel.getOverview()
        }
        visibleToUser = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userStatsMeta = arguments?.getParcelable(UserConstant.USER_STATS_META_KEY) ?: return

        binding.updateTheme()
        binding.initListener()


        val statusLayout = binding.resourceStatusLayout
        viewModel.statsLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    binding.updateView(res.data!!)
                }
                Status.ERROR -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.field.type = userStatsMeta.type
            viewModel.field.userName = userStatsMeta.userMeta.userName
            viewModel.field.userId = userStatsMeta.userMeta.userId

        }
    }

    private fun StatsOverviewFragmentLayoutBinding.updateView(data: StatsOverviewModel) {
        overviewModel = data
        with(data) {
            count?.let {
                totalCountTv.title = count?.toString()
            }

            episodesWatched?.let {
                episodeWatched.title = it.toString()
            }

            chaptersRead?.let {
                episodeWatched.title = it.toString()
            }

            daysWatched?.let {
                daysWatchedTv.title = "%.1f".format(it)
            }

            volumesRead?.let {
                daysWatchedTv.title = it.toString()
            }

            statusDistribution?.firstOrNull { it.status == MediaListStatus.PLANNING.ordinal }?.let {
                it.hoursWatched?.div(24)?.let {
                    dayPlannedTv.title =
                        it.toString()
                }
            }

            standardDeviation?.let {
                statsStandardDeviationTv.title = it.toString()
            }

            meanScore?.let {
                binding.statsMeanScoreTv.title = it.toString()
            }

            updateScoreChart()
            updateDistribution()
            updateReleaseChart()
            updateWatchChart()
        }
    }

    private fun StatsOverviewFragmentLayoutBinding.updateWatchChart() {
        overviewModel?.watchYear?.let {
            val entries = it.map {
                Entry(
                    it.year!!.toFloat(), when (watchYearToggleSwitch.getCheckedPosition()) {
                        0 -> {
                            it.count?.toFloat()
                        }
                        1 -> {
                            it.hoursWatched?.toFloat()
                        }
                        else -> {
                            it.meanScore?.toFloat()
                        }
                    } ?: 0f
                )
            }

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
                this.valueFormatter =  object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
            }.let { set ->
                watchYearLineChart.let { perYear ->
                    perYear.setTouchEnabled(true)
                    perYear.axisRight.isEnabled = false
                    perYear.setGridBackgroundColor(DynamicTheme.getInstance().get().tintAccentColor)
                    perYear.axisLeft.let { left ->
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

                    perYear.xAxis.apply {

                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                        position = XAxis.XAxisPosition.BOTTOM
                        gridLineWidth = 2f
                        typeface = ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
                        textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    }

                    perYear.description = null
                    perYear.data = LineData(set)
                    perYear.invalidate()
                }
            }
        } ?: let { watchYearLinearLayout.visibility = View.GONE }
    }

    private fun StatsOverviewFragmentLayoutBinding.updateReleaseChart() {
        overviewModel?.releaseYear?.let {
            val entries = it.map {
                Entry(
                    it.year!!.toFloat(), when (releaseYearToggleSwitch.getCheckedPosition()) {
                        0 -> {
                            it.count?.toFloat()
                        }
                        1 -> {
                            it.hoursWatched?.toFloat()
                        }
                        else -> {
                            it.meanScore?.toFloat()
                        }
                    } ?: 0f
                )
            }

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
                this.valueFormatter =  object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
            }.let { set ->
                releaseYearBarChart.let { perYear ->
                    perYear.setTouchEnabled(true)
                    perYear.axisRight.isEnabled = false
                    perYear.setGridBackgroundColor(DynamicTheme.getInstance().get().tintAccentColor)
                    perYear.axisLeft.let { left ->
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

                    perYear.xAxis.apply {
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return value.toInt().toString()
                            }
                        }
                        setDrawGridLines(false)
                        setDrawAxisLine(false)
                        position = XAxis.XAxisPosition.BOTTOM
                        gridLineWidth = 2f
                        typeface = ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
                        textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    }

                    perYear.description = null
                    perYear.data = LineData(set)
                    perYear.invalidate()
                }
            }
        } ?: let { releaseYearLinearLayout.visibility = View.GONE }
    }

    @SuppressLint("SetTextI18n")
    private fun StatsOverviewFragmentLayoutBinding.updateDistribution() {
        overviewModel?.formatDistribution?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<StatsFormatDistributionModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)
                    statsBind.distributionTv.text = dist.format?.let { mediaFormat[it] }
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count?.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched?.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore?.toString().naText()}"
                }).into(formatDistributionRecyclerView)
        }
        overviewModel?.statusDistribution?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<StatsStatusDistributionModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)

                    statsBind.distributionTv.text = dist.status?.let { mediaListStatus[it] }
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count?.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched?.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore?.toString().naText()}"
                }).into(statusDistributionRecyclerView)
        }

        overviewModel?.countryDistribution?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<StatsCountryDistributionModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)

                    statsBind.distributionTv.text = dist.country
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count?.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched?.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore?.toString().naText()}"
                }).into(countrysDistributionRecyclerView)
        }
    }

    private fun StatsOverviewFragmentLayoutBinding.updateScoreChart() {
        if (overviewModel == null) return

        overviewModel?.scoresDistribution?.map { model ->
                model.score?.let {
                    BarEntry(
                        model.score!!.toFloat(),
                        if (scoreToggleSwitch.getCheckedPosition() == 0) model.count?.toFloat()
                            ?: 0f else model.hoursWatched?.toFloat() ?: 0f
                    )
                }
        }?.let {
                if (it.isEmpty()) {
                    return
                }
                val dataSet = BarDataSet(it, getString(R.string.score_distribution)).also {
                    it.color = DynamicTheme.getInstance().get().tintAccentColor
                    it.valueTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    it.valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toInt().toString()
                        }
                    }
                }
                scoreBarChart.apply {
                    legend.textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    axisRight.isEnabled = false
                    axisLeft.isEnabled = false
                    xAxis.let { axis ->
                        axis.typeface =
                            ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
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
                    scoreBarChart.data = BarData(dataSet).apply {
                        xAxis.spaceMin = barWidth / 2f
                        xAxis.spaceMax = barWidth / 2f
                    }
                    invalidate()
                }

            }
    }


    private fun StatsOverviewFragmentLayoutBinding.initListener() {
        scoreToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.scoreTogglePos = position
                updateScoreChart()
            }
        }
        releaseYearToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.releaseYearTogglePos = position
                updateReleaseChart()
            }
        }
        watchYearToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.watchYearTogglePos = position
                updateWatchChart()
            }
        }
    }

    private fun StatsOverviewFragmentLayoutBinding.updateTheme() {
        scoreToggleSwitch?.let {
            it.themeIt()
            it.setEntries(
                if (userStatsMeta.type == MediaType.ANIME.ordinal)
                    R.array.score_toggle_list
                else
                    R.array.manga_score_toggle_list
            )
            it.setCheckedPosition(
                viewModel.scoreTogglePos
            )
        }


        releaseYearToggleSwitch?.let {
            it.themeIt()
            it.setEntries(
                if (userStatsMeta.type == MediaType.ANIME.ordinal)
                    R.array.stats_toggle_list
                else
                    R.array.manga_stats_toggle_list
            )
            it.setCheckedPosition(
                viewModel.releaseYearTogglePos
            )
        }


        watchYearToggleSwitch?.let {
            it.themeIt()
            it.setEntries(
                if (userStatsMeta.type == MediaType.ANIME.ordinal)
                    R.array.stats_toggle_list
                else
                    R.array.manga_stats_toggle_list
            )
            it.setCheckedPosition(
                viewModel.watchYearTogglePos
            )
        }

        if (userStatsMeta.type == MediaType.MANGA.ordinal) {
            totalCountTv.subtitle = getString(R.string.total_manga)
            episodeWatched.subtitle = getString(R.string.chapters_read)
            daysWatchedTv.subtitle = getString(R.string.volumes_read)
            watchYearTv.text = getString(R.string.read_year)
        }

    }
}
