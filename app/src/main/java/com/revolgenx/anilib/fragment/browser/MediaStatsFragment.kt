package com.revolgenx.anilib.fragment.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.overview.MediaStatsField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment
import com.revolgenx.anilib.model.stats.MediaStatsModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.repository.util.Status.*
import com.revolgenx.anilib.util.pmap
import com.revolgenx.anilib.viewmodel.MediaStatsViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.media_stats_fragment_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MediaStatsFragment : BaseFragment() {
    val viewModel by viewModel<MediaStatsViewModel>()
    private var mediaBrowserMeta: MediaBrowserMeta? = null

    private val field by lazy {
        MediaStatsField().also { f ->
            f.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_stats_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)

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

        viewModel.getStats(field)
    }

    private fun updateView(data: MediaStatsModel) {
        context ?: return

        data.trendsEntry?.let { entries ->
            LineDataSet(entries, "").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                lineWidth = 2f
                valueTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
                color = DynamicTheme.getInstance().get().tintAccentColor
                circleRadius = 0f
                circleHoleRadius = 0f
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
                        position = XAxis.XAxisPosition.BOTTOM
                        gridLineWidth = 2f
                        typeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_light)
                        textColor =  DynamicTheme.getInstance().get().tintSurfaceColor
                    }

                    perDay.description = null

                    perDay.data = LineData(set)
                    perDay.invalidate()
                }
            }
        }
    }
}
