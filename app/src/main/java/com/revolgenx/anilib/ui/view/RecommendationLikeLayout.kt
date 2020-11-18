package com.revolgenx.anilib.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.recommendation_like_layout.view.*
import timber.log.Timber

class RecommendationLikeLayout : DynamicCardView {

    private var mListener: ((state: RecommendationState) -> Unit)? = null

    var recommendationRatingView: TextView? = null
        set(value) {
            field = value
            value?.setTextColor(Color.WHITE)
        }

    var recommendationLikeView: AppCompatImageView? = null
    var recommendationDisLikeView: AppCompatImageView? = null

    var recommendationRating: Int = 0
        set(value) {
            field = value
            recommendationRatingView?.text = value.toString()
        }

    var recommendationState: RecommendationState = RecommendationState.NEUTRAL
        set(value) {
            neutralizeLike()
            when (value) {
                RecommendationState.LIKE -> {
                    if (field == RecommendationState.LIKE) return
                    recommendationLikeView?.imageTintList =
                        ColorStateList.valueOf(DynamicTheme.getInstance().get().accentColor)
                }
                RecommendationState.DISLIKE -> {
                    if (field == RecommendationState.DISLIKE) return
                    recommendationDisLikeView?.imageTintList =
                        ColorStateList.valueOf(DynamicTheme.getInstance().get().accentColor)
                }
            }
            field = value
        }

    private fun neutralizeLike() {
        recommendationLikeView?.imageTintList = ColorStateList.valueOf(Color.WHITE)
        recommendationDisLikeView?.imageTintList = ColorStateList.valueOf(Color.WHITE)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val v = LayoutInflater.from(context).inflate(R.layout.recommendation_like_layout, null)
        recommendationRatingView = v.recommendationRating
        recommendationLikeView = v.recommendationLikeIv
        recommendationDisLikeView = v.recommendationDislikeIv

        recommendationLikeView?.setOnClickListener {
            mListener?.invoke(RecommendationState.LIKE)
        }

        recommendationDisLikeView?.setOnClickListener {
            mListener?.invoke(RecommendationState.DISLIKE)
        }
        Timber.d("update update update")
        addView(v)
    }

    fun onLikedDisLiked(callback: (state: RecommendationState) -> Unit) {
        mListener = callback
    }

    enum class RecommendationState {
        LIKE, DISLIKE, NEUTRAL
    }
}