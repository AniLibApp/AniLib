package com.revolgenx.anilib.home.recommendation.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.apollographql.apollo.exception.ApolloHttpException
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.constant.HTTP_TOO_MANY_REQUEST
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.RecommendationPresenterLayoutBinding
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
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

    private val updateRecommendationField by lazy {
        UpdateRecommendationField()
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
                recommendedFromFormatYearTv.status = from.mediaEntryListModel?.status

                recommendedFromImageConstraintLayout.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            from.mediaId,
                            from.type!!,
                            from.title!!.romaji!!,
                            from.coverImage!!.image(context),
                            from.coverImage!!.largeImage,
                            from.bannerImage
                        )
                    ).postEvent
                }

                recommendedFromImageConstraintLayout.setOnLongClickListener {
                    if (context.loggedIn()) {
                        OpenMediaListEditorEvent(
                            EntryEditorMeta(
                                from.mediaId,
                                from.type!!,
                                from.title!!.title(context)!!,
                                from.coverImage!!.image(context),
                                from.bannerImage
                            )
                        ).postEvent
                    } else {
                        context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
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
                recommendedFormatYearTv.status = rec.mediaEntryListModel?.status
                recommendedImageConstraintLayout.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            rec.mediaId,
                            rec.type!!,
                            rec.title!!.romaji!!,
                            rec.coverImage!!.image(context),
                            rec.coverImage!!.largeImage,
                            rec.bannerImage
                        )
                    ).postEvent
                }

                recommendedImageConstraintLayout.setOnLongClickListener {
                    if (context.loggedIn()) {
                        OpenMediaListEditorEvent(
                            EntryEditorMeta(
                                rec.mediaId,
                                rec.type!!,
                                rec.title!!.title(context)!!,
                                rec.coverImage!!.image(context),
                                rec.bannerImage
                            )
                        ).postEvent
                    } else {
                        context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                    }
                    true
                }
            }

            updateRecommendationLikeView(item)

            recommendationLikeIv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                        override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                            if (it != null) {
                                viewModel.removeUpdateRecommendationObserver(this)
                            }
                            when (it?.status) {
                                Status.SUCCESS -> {
                                    viewModel.recommendedList[it.data?.id]?.let { cache ->
                                        cache.rating = it.data?.rating
                                        cache.userRating = it.data?.userRating
                                    }

                                    if (item.id == it.data?.id) {
                                        updateRecommendationLikeView(item)
                                    }
                                }
                                Status.ERROR -> {
                                    if (it.exception is ApolloHttpException) {
                                        when (it.exception.code()) {
                                            HTTP_TOO_MANY_REQUEST -> {
                                                context.makeToast(R.string.too_many_request)
                                            }
                                            else -> {
                                                context.makeToast(R.string.operation_failed)
                                            }
                                        }
                                    } else {
                                        context.makeToast(R.string.operation_failed)
                                    }
                                }
                                else->{}
                            }
                        }
                    }

                    updateRecommendation(observer, item,
                        item.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_UP
                    )
                }
            }

            recommendationDislikeIv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                        override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                            if (it != null) {
                                viewModel.removeUpdateRecommendationObserver(this)
                            }

                            when (it?.status) {
                                Status.SUCCESS -> {
                                    viewModel.recommendedList[it.data?.id]?.let { cache ->
                                        cache.rating = it.data?.rating
                                        cache.userRating = it.data?.userRating
                                    }

                                    if (item.id == it.data?.id) {
                                        updateRecommendationLikeView(item)
                                    }
                                }
                                Status.ERROR -> {
                                    context.makeToast(R.string.operation_failed)
                                }
                                else->{}
                            }
                        }
                    }

                    updateRecommendation(observer, item,
                        item.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_DOWN
                    )
                }
            }
        }
    }


    private fun updateRecommendation(
        observer: Observer<Resource<UpdateRecommendationModel>>,
        data: RecommendationModel,
        currentRating: RecommendationRating,
        newRating: RecommendationRating
    ) {
        viewModel.updateRecommendation(updateRecommendationField.also { field ->
            field.mediaId = data.recommendationFrom?.mediaId
            field.mediaRecommendationId = data.recommended?.mediaId
            field.rating = if (currentRating == newRating) {
                RecommendationRating.NO_RATING.ordinal
            } else {
                newRating.ordinal
            }
        }).observe(lifecycleOwner, observer)
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

    private fun View.checkLoggedIn(): Boolean {
        return context.loggedIn().also {
            if (!it) {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }
}