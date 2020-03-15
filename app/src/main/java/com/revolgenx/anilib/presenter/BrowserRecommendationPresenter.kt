package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.HTTP_TOO_MANY_REQUEST
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.UpdateRecommendationField
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.model.UpdateRecommendationModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.type.MediaStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.util.getAverageScore
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.viewmodel.MediaOverviewViewModel
import kotlinx.android.synthetic.main.overview_recommendation_presnter_layout.view.*

class BrowserRecommendationPresenter(
    private val lifecycleOwner: LifecycleOwner,
    context: Context,
    private val viewModel: MediaOverviewViewModel
) :
    Presenter<MediaRecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    companion object {
        private const val holder_key = "holder_key"
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.overview_recommendation_presnter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaRecommendationModel>) {
        super.onBind(page, holder, element)
        val data = element.data!!
        holder.updateView(data)
        holder.set(holder_key, data)
    }

    private fun Holder.updateView(data: MediaRecommendationModel) {
        itemView.apply {
            mediaRecommendationTitleTv.text = data.title?.title(context)
            recommendationCoverImage.setImageURI(data.coverImage?.image)

            mediaRatingTv.text = getAverageScore(data.averageScore)

            if (data.type == MediaType.ANIME.ordinal) {
                mediaEpisodeFormatTv.text = context.getString(R.string.episode_format_s).format(
                    naText(data.episodes),
                    naText(data.format?.let { context.resources.getStringArray(R.array.media_format)[it] })
                )
            } else {
                mediaEpisodeFormatTv.text = context.getString(R.string.chapter_format_s).format(
                    naText(data.chapters),
                    naText(data.format?.let { context.resources.getStringArray(R.array.media_format)[it] })
                )
            }
            mediaSeasonYearTv.text = naText(data.seasonYear?.toString())
            mediaRecommendationRating.text = data.rating?.toString()
            mediaRecommendationRating.setTextColor(Color.WHITE)

            val statusColor = when (data.status) {
                MediaStatus.RELEASING.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorReleasing)
                }
                MediaStatus.FINISHED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorFinished)
                };
                MediaStatus.NOT_YET_RELEASED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorNotReleased)
                }
                MediaStatus.CANCELLED.ordinal -> {
                    ContextCompat.getColor(context!!, R.color.colorCancelled)
                }
                else -> {
                    ContextCompat.getColor(context!!, R.color.colorUnknown)
                }
            }
            statusDivider.setBackgroundColor(statusColor)



            data.userRating?.let {
                when (RecommendationRating.values()[it]) {
                    RecommendationRating.NO_RATING -> {
                        mediaRecommendationDislikeTv.setColorFilter(Color.WHITE)
                        mediaRecommendationLikeTv.setColorFilter(Color.WHITE)
                    }
                    RecommendationRating.RATE_UP -> {
                        mediaRecommendationLikeTv.setColorFilter(
                            DynamicTheme.getInstance().get().accentColor
                        )
                        mediaRecommendationDislikeTv.setColorFilter(Color.WHITE)
                    }
                    RecommendationRating.RATE_DOWN -> {
                        mediaRecommendationLikeTv.setColorFilter(Color.WHITE)
                        mediaRecommendationDislikeTv.setColorFilter(
                            DynamicTheme.getInstance().get().accentColor
                        )
                    }
                    else -> {
                        mediaRecommendationLikeTv.setColorFilter(Color.WHITE)
                        mediaRecommendationDislikeTv.setColorFilter(Color.WHITE)
                    }
                }
            }

            mediaRecommendationLikeTv.setOnClickListener(null)
            mediaRecommendationDislikeTv.setOnClickListener(null)

            this.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        data.mediaRecommendationId!!,
                        data.type!!,
                        data.title!!.romaji!!,
                        data.coverImage!!.image,
                        data.bannerImage
                    ), recommendationCoverImage
                ).postEvent
            }

            mediaRecommendationLikeTv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                        override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                            if (it != null) {
                                viewModel.removeUpdateRecommendationObserver(this)
                            }

                            when (it?.status) {
                                Status.SUCCESS -> {
                                    if (data.recommendationId == it.data!!.recommendationId) {
                                        data.rating = it.data.rating
                                        data.userRating = it.data.userRating
                                        if ((this@updateView.get(holder_key) as MediaRecommendationModel).recommendationId == it.data.recommendationId) {
                                            this@updateView.updateView(data)
                                        }
                                    }
                                }
                                Status.ERROR -> {
                                    when (it.code) {
                                        HTTP_TOO_MANY_REQUEST -> {
                                            this@updateView.itemView.context.makeToast(R.string.too_many_request)
                                        }
                                        -1 -> {
                                            this@updateView.itemView.context.makeToast(R.string.operation_failed)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    updateRecommendation(observer, data,
                        data.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_UP
                    )
                }
            }

            mediaRecommendationDislikeTv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                        override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                            if (it != null) {
                                viewModel.removeUpdateRecommendationObserver(this)
                            }

                            when (it?.status) {
                                Status.SUCCESS -> {
                                    if (data.recommendationId == it.data!!.recommendationId) {
                                        data.rating = it.data.rating
                                        data.userRating = it.data.userRating
                                        if ((this@updateView.get(holder_key) as MediaRecommendationModel).recommendationId == it.data.recommendationId) {
                                            this@updateView.updateView(data)
                                        }
                                    }
                                }
                                Status.ERROR -> {
                                    this@updateView.itemView.context.makeToast(R.string.operation_failed)
                                }
                            }
                        }
                    }

                    updateRecommendation(observer, data,
                        data.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_DOWN
                    )
                }
            }

        }
    }


    private fun updateRecommendation(
        observer: Observer<Resource<UpdateRecommendationModel>>,
        data: MediaRecommendationModel,
        currentRating: RecommendationRating,
        newRating: RecommendationRating
    ) {
        viewModel.updateRecommendation(UpdateRecommendationField().also { field ->
            field.mediaId = data.mediaId
            field.mediaRecommendationId = data.mediaRecommendationId

            field.rating = if (currentRating == newRating) {
                RecommendationRating.NO_RATING.ordinal
            } else {
                newRating.ordinal
            }
        }).observe(lifecycleOwner, observer)
    }

    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}