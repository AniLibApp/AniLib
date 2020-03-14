package com.revolgenx.anilib.fragment.browser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.core.net.toUri
import androidx.lifecycle.observe
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.pagers.PageSizePager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.MediaOverviewField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.field.overview.MediaRecommendationField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.model.MediaOverviewModel
import com.revolgenx.anilib.presenter.BrowserOverviewRecommendationPresenter
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.source.BrowserOverviewRecommendationSource
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSource
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.viewmodel.MediaOverviewViewModel
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.media_overview_fragment.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaOverviewFragment : BaseFragment() {

    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val viewModel by viewModel<MediaOverviewViewModel>()

    private val recommendationField by lazy {
        MediaRecommendationField()
    }

    private val recommendationSource by lazy {
        if (viewModel.browserOverviewRecommendationSource == null)
            createRecommendationSource()
        else viewModel.browserOverviewRecommendationSource!!
    }

    private val recommendationPresenter by lazy {
        BrowserOverviewRecommendationPresenter(viewLifecycleOwner, context!!, viewModel)
    }

    private var loadingPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forLoadingIndicator(
                context!!,
                R.layout.loading_layout
            )
            else field
        }

    private var errorPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forErrorIndicator(context!!, R.layout.error_layout)
            else field
        }

    private var emptyPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forEmptyIndicator(context!!, R.layout.empty_layout)
            else field
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return

        viewModel.mediaOverviewLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    res.data!!.let { overview ->
                        updateView(overview)
                    }

                }
                Status.ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE

                }
                Status.LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }

        if (savedInstanceState == null) {
            viewModel.getOverview(MediaOverviewField().also {
                it.mediaId = mediaBrowserMeta!!.mediaId
            })
        }

        recommendationField.mediaId = mediaBrowserMeta!!.mediaId

        invalidateRecommendationAdapter()
    }

    private fun updateView(overview: MediaOverviewModel) {
        if (context == null) return

        mediaDescriptionTv.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                overview.description?.let { Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT) } ?: ""
            } else {
                overview.description?.let { Html.fromHtml(it) } ?: ""
            }

        mediaFormatTv.descriptionTextView()
            .naText(overview.format?.let { context!!.resources.getStringArray(R.array.media_format)[it] })
        mediaSourceTv.descriptionTextView()
            .naText(overview.source?.let { context!!.resources.getStringArray(R.array.media_source)[it] })

        if (overview.type == MediaType.ANIME.ordinal) {
            mediaEpisodeTv.descriptionTextView()
                .naText(overview.episodes)
            mediaDurationTv.description =
                getString(R.string.min_s).format(naText(overview.duration))
        } else if (overview.type == MediaType.MANGA.ordinal) {
            mediaEpisodeTv.titleTextView().text = getString(R.string.chapters)
            mediaEpisodeTv.descriptionTextView().naText(overview.chapters)
            mediaDurationTv.titleTextView().text = getString(R.string.volumes)
            mediaDurationTv.descriptionTextView().naText(overview.volumes)
        }

        when (overview.trailer?.site) {
            YOUTUBE -> {
                overview.trailer?.thumbnail?.let { trailerSimpleDrawee.setImageURI(it) }
                trailerLayout.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            (YOUTUBE_URL + overview.trailer?.id).toUri()
                        )
                    )
                }
            }
            DAILYMOTION -> {
                overview.trailer?.thumbnail?.let { trailerSimpleDrawee.setImageURI(it) }
                trailerLayout.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            (DAILYMOTION_URL + overview.trailer?.id).toUri()
                        )
                    )
                }
            }
            else -> {
                let { trailerLayout.visibility = View.GONE }
            }
        }

        startEndDateTv.text = getString(R.string.start_s_end_s).format(
            naText(overview.startDate.toString()),
            naText(overview.endDate.toString())
        )

    }

    private fun createRecommendationSource(): BrowserOverviewRecommendationSource {
        return viewModel.createRecommendationSource(recommendationField, viewLifecycleOwner)
    }

    private fun invalidateRecommendationAdapter() {
        Adapter.builder(this, PAGE_SIZE_HINT)
            .setPager(PageSizePager(PAGE_SIZE))
            .addSource(recommendationSource)
            .addPresenter(recommendationPresenter)
            .addPresenter(loadingPresenter!!)
            .addPresenter(errorPresenter!!)
            .addPresenter(emptyPresenter!!)
            .build()
//            .into(overviewRecommendationRecyclerView)
    }


}