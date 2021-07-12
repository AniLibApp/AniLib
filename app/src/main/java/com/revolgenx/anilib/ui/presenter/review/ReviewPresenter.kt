package com.revolgenx.anilib.ui.presenter.review

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.databinding.ReviewPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenReviewEvent
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter
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
            item.userPrefModel?.let { user ->
                reviewByTv.text = context.getString(R.string.review_of_s_by_s).format(
                    item.mediaModel?.title?.title(context),
                    user.name
                )
                reviewByScoreTv.text =
                    context.getString(R.string.review_score_format).format(item.score)

                reviewByAvatar.setImageURI(user.avatar?.image)

                reviewByAvatar.setOnClickListener {
                    OpenUserProfileEvent(user.id).postEvent
                }
            }

            item.mediaModel?.let { media ->
                reviewMediaBannerImage.setImageURI(media.bannerImage)
                reviewSummaryTv.text = item.summary
                reviewByTv.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            media.mediaId,
                            media.type!!,
                            media.title!!.romaji!!,
                            media.coverImage!!.image(context),
                            media.coverImage!!.largeImage,
                            media.bannerImage
                        )
                    ).postEvent
                }
            }
            reviewLikeTv.text = item.rating?.toString().naText()

            root.setOnClickListener {
                OpenReviewEvent(item.reviewId!!).postEvent
            }
        }
    }
}
