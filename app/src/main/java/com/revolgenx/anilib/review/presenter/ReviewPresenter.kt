package com.revolgenx.anilib.review.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.ReviewPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenReviewEvent
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.util.naText

class ReviewPresenter(context: Context) : BasePresenter<ReviewPresenterLayoutBinding, ReviewModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ReviewPresenterLayoutBinding {
        return ReviewPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<ReviewModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            item.user?.let { user ->
                reviewByTv.text = context.getString(R.string.review_of_s_by_s).format(
                    item.media?.title?.title(),
                    user.name
                )
                reviewByScoreTv.text =
                    context.getString(R.string.review_score_format).format(item.score)

                reviewByAvatar.setImageURI(user.avatar?.image)

                reviewByAvatar.setOnClickListener {
                    OpenUserProfileEvent(user.id).postEvent
                }
            }

            item.media?.let { media ->
                reviewMediaBannerImage.setImageURI(media.bannerImage)
                reviewSummaryTv.text = item.summary
                reviewByTv.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            media.id,
                            media.type!!,
                            media.title!!.romaji!!,
                            media.coverImage!!.image(),
                            media.coverImage!!.largeImage,
                            media.bannerImage
                        )
                    ).postEvent
                }
            }
            reviewLikeTv.text = item.rating?.toString().naText()

            root.setOnClickListener {
                OpenReviewEvent(item.id).postEvent
            }
        }
    }
}