package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.UpdateRecommendationField
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.type.RecommendationRating
import com.revolgenx.anilib.util.getAverageScore
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.viewmodel.MediaBrowserViewModel
import com.revolgenx.anilib.viewmodel.MediaOverviewViewModel
import kotlinx.android.synthetic.main.overview_recommendation_card_layout.view.*

class BrowserOverviewRecommendationPresenter(
    private val lifecycleOwner: LifecycleOwner,
    context: Context,
    private val viewModel: MediaOverviewViewModel
) :
    Presenter<MediaRecommendationModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.overview_recommendation_card_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaRecommendationModel>) {
        super.onBind(page, holder, element)
        val data = element.data!!
        holder.itemView.apply {
            mediaRecommendationTitleTv.text = data.title?.title(context)
            recommendationCoverImage.setImageURI(data.coverImage?.image)
            mediaRatingTv.text = getAverageScore(data.averageScore)
            mediaRecommendationRating.text = data.rating?.toString()

            data.userRating?.let {
                when (RecommendationRating.values()[it]) {
                    RecommendationRating.NO_RATING -> {
                        mediaRecommendationDislikeTv.color = Color.WHITE
                        mediaRecommendationLikeTv.color = Color.WHITE
                    }
                    RecommendationRating.RATE_UP -> {
                        mediaRecommendationLikeTv.color =
                            DynamicTheme.getInstance().get().accentColor
                    }
                    RecommendationRating.RATE_DOWN -> {
                        mediaRecommendationDislikeTv.color =
                            DynamicTheme.getInstance().get().accentColor
                    }
                    else -> {
                        mediaRecommendationDislikeTv.color = Color.WHITE
                        mediaRecommendationLikeTv.color = Color.WHITE
                    }
                }
            }

            mediaRecommendationLikeTv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    saveRecommendation(mediaRecommendationLikeTv, data,
                        data.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_UP
                    )
                }
            }

            mediaRecommendationDislikeTv.setOnClickListener {
                if (it.checkLoggedIn()) {
                    saveRecommendation(mediaRecommendationLikeTv, data,
                        data.userRating?.let { RecommendationRating.values()[it] }
                            ?: RecommendationRating.NO_RATING,
                        RecommendationRating.RATE_DOWN
                    )
                }
            }

        }
    }

    private fun saveRecommendation(
        imageView: DynamicImageView,
        data: MediaRecommendationModel,
        currentRating: RecommendationRating,
        newRating: RecommendationRating
    ) {
        viewModel.updateRecommendation(UpdateRecommendationField().also { field ->
            field.mediaId = data.mediaId
            field.mediaRecommendationId = data.recommendationId
            field.rating = if (currentRating == newRating) {
                RecommendationRating.NO_RATING.ordinal
            } else {
                newRating.ordinal
            }
        }).observe(lifecycleOwner)
        {
            when (it.status) {
                Status.SUCCESS -> {
                    if (data.recommendationId == it.data!!.recommendationId) {
                        data.rating = it.data.rating
                        data.userRating = it.data.userRating
                        imageView.color = if (currentRating == newRating) {
                            Color.WHITE
                        } else {
                            DynamicTheme.getInstance().get().accentColor
                        }
                    }
                }
                Status.ERROR -> {
                    imageView.context.makeToast(R.string.operation_failed)
                }
            }
        }
    }

    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}