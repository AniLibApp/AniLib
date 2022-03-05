package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.OverviewRecommendationPresnterLayoutBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.presenter.RecommendationPresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.media.viewmodel.MediaOverviewVM
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.util.loginContinue

class MediaRecommendationPresenter(
    private val lifecycleOwner: LifecycleOwner,
    context: Context,
    private val viewModel: MediaOverviewVM
) :
    BasePresenter<OverviewRecommendationPresnterLayoutBinding, RecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color).map { Color.parseColor(it) }
    }

    companion object {
        private const val RECOMMENDATION_RATE_OBSERVER_KEY = "RECOMMENDATION_RATE_OBSERVER_KEY"
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): OverviewRecommendationPresnterLayoutBinding {
        return OverviewRecommendationPresnterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<RecommendationModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        viewModel.mediaRecommendedList[item.id] = item
        holder.getBinding()?.apply {
            updateView(item)

            mediaRecommendationLikeIv.setOnClickListener {
                context.loginContinue {
                    val liveData = viewModel.upVoteRecommendation(item)
                    holder.get<LiveData<*>?>(RECOMMENDATION_RATE_OBSERVER_KEY)
                        ?.removeObservers(lifecycleOwner)
                    holder[RECOMMENDATION_RATE_OBSERVER_KEY] = liveData
                    liveData.observe(lifecycleOwner,
                        object : Observer<Resource<RecommendationModel>> {
                            override fun onChanged(it: Resource<RecommendationModel>) {
                                when (it) {
                                    is Resource.Error -> {
                                        context.makeErrorToast(R.string.operation_failed)
                                    }
                                    is Resource.Success -> {
                                        updateRecommendationLikeView(item)
                                    }
                                    is Resource.Loading -> {}
                                }
                                liveData.removeObserver(this)
                                holder[RECOMMENDATION_RATE_OBSERVER_KEY] = null
                            }
                        })
                }
            }

            mediaRecommendationDislikeIv.setOnClickListener {
                context.loginContinue {
                    val liveData = viewModel.downVoteRecommendation(item)
                    holder.get<LiveData<*>?>(RECOMMENDATION_RATE_OBSERVER_KEY)
                        ?.removeObservers(lifecycleOwner)
                    holder[RECOMMENDATION_RATE_OBSERVER_KEY] = liveData
                    liveData.observe(lifecycleOwner,
                        object : Observer<Resource<RecommendationModel>> {
                            override fun onChanged(it: Resource<RecommendationModel>) {
                                when (it) {
                                    is Resource.Error -> {
                                        context.makeErrorToast(R.string.operation_failed)
                                    }
                                    is Resource.Success -> {
                                        updateRecommendationLikeView(item)
                                    }
                                    is Resource.Loading -> {}
                                }
                                liveData.removeObserver(this)
                            }
                        })
                }

            }
        }
    }


    override fun onUnbind(holder: Holder) {
        super.onUnbind(holder)
        holder.get<LiveData<*>?>(RECOMMENDATION_RATE_OBSERVER_KEY)?.removeObservers(lifecycleOwner)
    }

    private fun OverviewRecommendationPresnterLayoutBinding.updateView(data: RecommendationModel) {
        val item = data.recommended ?: return
        mediaRecommendationTitleTv.text = item.title?.title(context)
        recommendationCoverImage.setImageURI(item.coverImage?.image(context))

        mediaRatingTv.text = item.averageScore

        if (item.type == MediaType.ANIME.ordinal) {
            mediaEpisodeFormatTv.text = context.getString(R.string.episode_format_s).format(
                item.episodes.naText(),
                item.format?.let { context.resources.getStringArray(R.array.media_format)[it] }
                    .naText()
            )
        } else {
            mediaEpisodeFormatTv.text = context.getString(R.string.chapter_format_s).format(
                item.chapters.naText(),
                item.format?.let { context.resources.getStringArray(R.array.media_format)[it] }
                    .naText()
            )
        }

        mediaEpisodeFormatTv.status = item.mediaListEntry?.status
        mediaSeasonYearTv.text = item.seasonYear?.toString().naText()
        mediaRecommendationRating.text = data.rating?.toString()

        item.status?.let {
            statusDivider.setBackgroundColor(statusColors[it])
        }

        updateRecommendationLikeView(data)

        mediaRecommendationLikeIv.setOnClickListener(null)
        mediaRecommendationDislikeIv.setOnClickListener(null)

        root.setOnClickListener {
            OpenMediaInfoEvent(
                MediaInfoMeta(
                    item.id,
                    item.type!!,
                    item.title!!.romaji!!,
                    item.coverImage!!.image(context),
                    item.coverImage!!.largeImage,
                    item.bannerImage
                )
            ).postEvent
        }

        root.setOnLongClickListener {
            if (context.loggedIn()) {
                OpenMediaListEditorEvent(item.id).postEvent
            } else {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
            true
        }


    }


    private fun OverviewRecommendationPresnterLayoutBinding.updateRecommendationLikeView(item: RecommendationModel) {
        mediaRecommendationLikeIv.colorType = Theme.ColorType.TEXT_PRIMARY
        mediaRecommendationDislikeIv.colorType = Theme.ColorType.TEXT_PRIMARY
        mediaRecommendationRating.text = item.rating?.toString()

        when (item.userRating) {
            RecommendationRating.RATE_UP.ordinal -> {
                mediaRecommendationLikeIv.colorType = Theme.ColorType.ACCENT
                mediaRecommendationLikeIv.contrastWithColorType = Theme.ColorType.BACKGROUND
            }
            RecommendationRating.RATE_DOWN.ordinal -> {
                mediaRecommendationDislikeIv.colorType = Theme.ColorType.ACCENT
                mediaRecommendationDislikeIv.contrastWithColorType = Theme.ColorType.BACKGROUND

            }
        }
    }


    private fun updateRecommendation(
        observer: Observer<Resource<RecommendationModel>>,
        data: RecommendationModel,
        currentRating: RecommendationRating,
        newRating: RecommendationRating
    ) {
//        viewModel.saveRecommendation(SaveRecommendationField().also { field ->
//            field.mediaId = data.recommendedFromId
//            field.mediaRecommendationId = data.recommendedId
//
//            field.rating = if (currentRating == newRating) {
//                RecommendationRating.NO_RATING.ordinal
//            } else {
//                newRating.ordinal
//            }
//        }).observe(lifecycleOwner, observer)
    }

    private fun View.checkLoggedIn(): Boolean {
        return context.loggedIn().also {
            if (!it) {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

}