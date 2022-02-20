package com.revolgenx.anilib.user.fragment

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
import com.revolgenx.anilib.databinding.StatsDistributionRecyclerLayoutBinding
import com.revolgenx.anilib.databinding.UserStatisticOverviewFragmentLayoutBinding
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.user.data.model.stats.*
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.user.viewmodel.StatsOverviewViewModel
import com.revolgenx.anilib.user.viewmodel.UserStatsContainerSharedVM
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel

//todo: add piechart in distribution
abstract class UserStatisticOverviewFragment :
    BaseLayoutFragment<UserStatisticOverviewFragmentLayoutBinding>() {

    private val viewModel by viewModel<StatsOverviewViewModel>()
    private val sharedViewModel by viewModel<UserStatsContainerSharedVM>(owner = {
        ViewModelOwner.from(
            this.parentFragment ?: this,
            this.parentFragment
        )
    })

    private val mediaFormat by lazy {
        requireContext().resources.getStringArray(R.array.media_format)
    }
    private val mediaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.anime_list_status)
    }

    protected open val type:Int = MediaType.ANIME.ordinal
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserStatisticOverviewFragmentLayoutBinding {
        return UserStatisticOverviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            viewModel.getOverview()
        }
        visibleToUser = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.hasUserData ?: return



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
            with(viewModel.field) {
                userId = sharedViewModel.userId
                userName = sharedViewModel.userName
            }
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateView(data: UserStatisticsModel) {
        data.apply {
            totalCountTv.title = count.toString()
            statsStandardDeviationTv.title = standardDeviation.toString()
            binding.statsMeanScoreTv.title = meanScore.toString()
            statuses?.firstOrNull { it.status == MediaListStatus.PLANNING.ordinal }?.let {
                dayPlannedTv.title = it.hoursWatched.div(24).toString()
            }

            updateScoreChart(scores)
            updateReleaseChart(releaseYears)
            updateWatchChart(startYears)
            updateDistribution(this)
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateView(data: UserModel) {
        data.statistics?.anime?.apply {
            episodeWatched.title = episodesWatched.toString()
            daysWatchedTv.title = "%.1f".format(daysWatched)
            updateView(this)
        }

        data.statistics?.manga?.apply {
            episodeWatched.title = chaptersRead.toString()
            daysWatchedTv.title = volumesRead.toString()
            updateView(this)
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateWatchChart(list: List<UserStartYearStatisticModel>?) {
        if (list.isNullOrEmpty()) {
            watchYearLinearLayout.visibility = View.GONE
            return
        }
        val entries = list.map {
            Entry(
                it.startYear?.toFloat() ?: 0.0f, when (watchYearToggleSwitch.getCheckedPosition()) {
                    0 -> {
                        it.count.toFloat()
                    }
                    1 -> {
                        it.hoursWatched.toFloat()
                    }
                    else -> {
                        it.meanScore.toFloat()
                    }
                }
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
            this.valueFormatter = object : ValueFormatter() {
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
                    typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
                    textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                }

                perYear.description = null
                perYear.data = LineData(set)
                perYear.invalidate()
            }
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateReleaseChart(list: List<UserReleaseYearStatisticModel>?) {
        if (list.isNullOrEmpty()) {
            releaseYearLinearLayout.visibility = View.GONE
            return
        }

        val entries = list.map {
            Entry(
                it.year!!.toFloat(), when (releaseYearToggleSwitch.getCheckedPosition()) {
                    0 -> {
                        it.count.toFloat()
                    }
                    1 -> {
                        it.hoursWatched.toFloat()
                    }
                    else -> {
                        it.meanScore.toFloat()
                    }
                }
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
            this.valueFormatter = object : ValueFormatter() {
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
                    typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.cabincondensed_regular)
                    textColor = DynamicTheme.getInstance().get().tintSurfaceColor
                }

                perYear.description = null
                perYear.data = LineData(set)
                perYear.invalidate()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun UserStatisticOverviewFragmentLayoutBinding.updateDistribution(data: UserStatisticsModel) {
        data.formats?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<UserFormatStatisticModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)
                    statsBind.distributionTv.text = dist.format?.let { mediaFormat[it] }
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore.toString().naText()}"
                }).into(formatDistributionRecyclerView)
        }
        data.statuses?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<UserStatusStatisticModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)

                    statsBind.distributionTv.text = dist.status?.let { mediaListStatus[it] }
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore.toString().naText()}"
                }).into(statusDistributionRecyclerView)
        }

        data.countries?.let {
            Adapter.builder(viewLifecycleOwner).addSource(Source.fromList(it)).addPresenter(
                Presenter.simple<UserCountryStatisticModel>(
                    requireContext(),
                    R.layout.stats_distribution_recycler_layout,
                    0
                ) { view, dist ->
                    val statsBind = StatsDistributionRecyclerLayoutBinding.bind(view)

                    statsBind.distributionTv.text = dist.country
                    statsBind.distributionMetaTv.text =
                        "Count: ${dist.count.toString().naText()}" +
                                "\nHour Watched: ${dist.hoursWatched.toString().naText()}" +
                                "\nMean Score: ${dist.meanScore.toString().naText()}"
                }).into(countrysDistributionRecyclerView)
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateScoreChart(list: List<UserScoreStatisticModel>?) {
        list ?: let { scoreBarChart.visibility = View.GONE; return }
        list.map { model ->
            model.score?.let {
                BarEntry(
                    model.score!!.toFloat(),
                    if (scoreToggleSwitch.getCheckedPosition() == 0) model.count.toFloat() else model.hoursWatched.toFloat()
                )
            }
        }.let {
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


    private fun UserStatisticOverviewFragmentLayoutBinding.initListener() {
        scoreToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.scoreTogglePos = position

                viewModel.statsLiveData.value?.data?.statistics?.let {
                    val media = it.anime ?: it.manga
                    updateScoreChart(media?.scores)
                }

            }
        }
        releaseYearToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.releaseYearTogglePos = position
                viewModel.statsLiveData.value?.data?.statistics?.let {
                    val media = it.anime ?: it.manga
                    updateReleaseChart(media?.releaseYears)
                }
            }
        }
        watchYearToggleSwitch.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                viewModel.watchYearTogglePos = position
                viewModel.statsLiveData.value?.data?.statistics?.let {
                    val media = it.anime ?: it.manga
                    updateWatchChart(media?.startYears)
                }
            }
        }
    }

    private fun UserStatisticOverviewFragmentLayoutBinding.updateTheme() {
        scoreToggleSwitch.let {
            it.themeIt()
            it.setEntries(
                if (type == MediaType.ANIME.ordinal)
                    R.array.score_toggle_list
                else
                    R.array.manga_score_toggle_list
            )
            it.setCheckedPosition(
                viewModel.scoreTogglePos
            )
        }


        releaseYearToggleSwitch.let {
            it.themeIt()
            it.setEntries(
                if (type == MediaType.ANIME.ordinal)
                    R.array.stats_toggle_list
                else
                    R.array.manga_stats_toggle_list
            )
            it.setCheckedPosition(
                viewModel.releaseYearTogglePos
            )
        }


        watchYearToggleSwitch.let {
            it.themeIt()
            it.setEntries(
                if (type == MediaType.ANIME.ordinal)
                    R.array.stats_toggle_list
                else
                    R.array.manga_stats_toggle_list
            )
            it.setCheckedPosition(
                viewModel.watchYearTogglePos
            )
        }

        if (type == MediaType.MANGA.ordinal) {
            totalCountTv.subtitle = getString(R.string.total_manga)
            episodeWatched.subtitle = getString(R.string.chapters_read)
            daysWatchedTv.subtitle = getString(R.string.volumes_read)
            watchYearTv.text = getString(R.string.read_year)
        }

    }
}
