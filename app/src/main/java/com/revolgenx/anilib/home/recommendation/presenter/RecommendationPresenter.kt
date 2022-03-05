package com.revolgenx.anilib.home.recommendation.presenter

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
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.RecommendationPresenterLayoutBinding
import com.revolgenx.anilib.home.recommendation.data.field.SaveRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

class RecommendationPresenter(
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: RecommendationViewModel
) :
    BasePresenter<RecommendationPresenterLayoutBinding, RecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }


    companion object {
        private const val RECOMMENDATION_RATE_OBSERVER_KEY = "RECOMMENDATION_RATE_OBSERVER_KEY"
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): RecommendationPresenterLayoutBinding {
        return RecommendationPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<RecommendationModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        viewModel.recommendedList[item.id] = item

        holder.getBinding()?.apply {
            item.recommendationFrom?.let { from ->
                recommendedFromTitleTv.text = from.title?.title(context)
                recommendedFromImageView.setImageURI(from.coverImage?.image(context))
                recommendedFromRatingTv.text = from.averageScore
                recommendedFromStatusTv.text = from.status?.let {
                    recommendedFromStatusTv.color = Color.parseColor(statusColors[it])
                    mediaStatus[it]
                }.naText()
                recommendedFromFormatYearTv.text =
                    context.getString(R.string.media_format_year_s).format(
                        from.format?.let { mediaFormats[it] }.naText(),
                        from.seasonYear?.toString().naText()
                    )
                recommendedFromFormatYearTv.status = from.mediaListEntry?.status

                recommendedFromImageConstraintLayout.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            from.id,
                            from.type!!,
                            from.title!!.romaji!!,
                            from.coverImage!!.image(context),
                            from.coverImage!!.largeImage,
                            from.bannerImage
                        )
                    ).postEvent
                }

                recommendedFromImageConstraintLayout.setOnLongClickListener {
                    context.loginContinue {
                        OpenMediaListEditorEvent(from.id).postEvent
                    }
                    true
                }
            }

            item.recommended?.let { rec ->
                recommendedTitleTv.text = rec.title?.title(context)
                recommendedImageView.setImageURI(rec.coverImage?.image(context))
                recommendedMediaRatingTv.text = rec.averageScore
                recommendedStatusTv.text = rec.status?.let {
                    recommendedStatusTv.color = Color.parseColor(statusColors[it])
                    mediaStatus[it]
                }.naText()
                recommendedFormatYearTv.text =
                    context.getString(R.string.media_format_year_s).format(
                        rec.format?.let { mediaFormats[it] }.naText(),
                        rec.seasonYear?.toString().naText()
                    )
                recommendedFormatYearTv.status = rec.mediaListEntry?.status
                recommendedImageConstraintLayout.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            rec.id,
                            rec.type!!,
                            rec.title!!.romaji!!,
                            rec.coverImage!!.image(context),
                            rec.coverImage!!.largeImage,
                            rec.bannerImage
                        )
                    ).postEvent
                }

                recommendedImageConstraintLayout.setOnLongClickListener {
                    context.loginContinue {
                        OpenMediaListEditorEvent(rec.id).postEvent
                    }
                    true
                }
            }

            updateRecommendationLikeView(item)

            recommendationLikeIv.setOnClickListener {
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

            recommendationDislikeIv.setOnClickListener {
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

    private fun RecommendationPresenterLayoutBinding.updateRecommendationLikeView(item: RecommendationModel) {
        recommendationLikeIv.colorType = Theme.ColorType.TEXT_PRIMARY
        recommendationDislikeIv.colorType = Theme.ColorType.TEXT_PRIMARY
        recommendationRatingTv.text = item.rating?.toString()

        when (item.userRating) {
            RecommendationRating.RATE_UP.ordinal -> {
                recommendationLikeIv.colorType = Theme.ColorType.ACCENT
            }
            RecommendationRating.RATE_DOWN.ordinal -> {
                recommendationDislikeIv.colorType = Theme.ColorType.ACCENT
            }
        }
    }

}