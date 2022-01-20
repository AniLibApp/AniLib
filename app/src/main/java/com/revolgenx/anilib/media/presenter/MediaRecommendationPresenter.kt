package com.revolgenx.anilib.media.presenter

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
import com.revolgenx.anilib.constant.HTTP_TOO_MANY_REQUEST
import com.revolgenx.anilib.home.recommendation.data.field.UpdateRecommendationField
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.data.model.MediaRecommendationModel
import com.revolgenx.anilib.home.recommendation.data.model.UpdateRecommendationModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.OverviewRecommendationPresnterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.media.viewmodel.MediaOverviewVM

class MediaRecommendationPresenter(
    private val lifecycleOwner: LifecycleOwner,
    context: Context,
    private val viewModel: MediaOverviewVM
) :
    BasePresenter<OverviewRecommendationPresnterLayoutBinding, MediaRecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color).map { Color.parseColor(it) }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): OverviewRecommendationPresnterLayoutBinding {
        return OverviewRecommendationPresnterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaRecommendationModel>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return
        viewModel.mediaRecommendedList[data.id] = data
        holder.getBinding()?.updateView(data)
    }

    private fun OverviewRecommendationPresnterLayoutBinding.updateView(data: MediaRecommendationModel) {
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

        mediaEpisodeFormatTv.status = item.mediaEntryListModel?.status
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
                    item.mediaId!!,
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
                OpenMediaListEditorEvent(
                    EntryEditorMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.title(context)!!,
                        item.coverImage!!.image(context),
                        item.bannerImage
                    )
                ).postEvent
            } else {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
            true
        }

        mediaRecommendationLikeIv.setOnClickListener {
            if (it.checkLoggedIn()) {
                val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                    override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                        if (it != null) {
                            viewModel.removeUpdateRecommendationObserver(this)
                        }

                        when (it?.status) {
                            Status.SUCCESS -> {
                                viewModel.mediaRecommendedList[it.data?.id]?.let { cache ->
                                    cache.rating = it.data?.rating
                                    cache.userRating = it.data?.userRating
                                }
                                if (data.id == it.data!!.id) {
                                    updateRecommendationLikeView(data)
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
                            else -> {
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

        mediaRecommendationDislikeIv.setOnClickListener {
            if (it.checkLoggedIn()) {
                val observer = object : Observer<Resource<UpdateRecommendationModel>> {
                    override fun onChanged(it: Resource<UpdateRecommendationModel>?) {
                        if (it != null) {
                            viewModel.removeUpdateRecommendationObserver(this)
                        }

                        when (it?.status) {
                            Status.SUCCESS -> {
                                viewModel.mediaRecommendedList[it.data?.id]?.let { cache ->
                                    cache.rating = it.data?.rating
                                    cache.userRating = it.data?.userRating
                                }
                                if (data.id == it.data!!.id) {
                                    updateRecommendationLikeView(data)
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
                            else -> {
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


    private fun OverviewRecommendationPresnterLayoutBinding.updateRecommendationLikeView(item: MediaRecommendationModel) {
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
        return context.loggedIn().also {
            if (!it) {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

}