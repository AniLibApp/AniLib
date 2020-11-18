package com.revolgenx.anilib.ui.presenter.review

import android.content.Context
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.BrowseReviewEvent
import com.revolgenx.anilib.infrastructure.event.UserBrowseEvent
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.review_presenter_layout.view.*

class ReviewPresenter(context: Context) : Presenter<ReviewModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(R.layout.review_presenter_layout, parent, false))
    }

    override fun onBind(page: Page, holder: Holder, element: Element<ReviewModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            item.userPrefModel?.let { user ->
                reviewByTv.text = context.getString(R.string.review_of_s_by_s).format(
                    item.mediaModel?.title?.title(context),
                    user.userName
                )
                reviewByScoreTv.text =
                    context.getString(R.string.review_score_format).format(item.score)

                reviewByAvatar.setImageURI(user.avatar?.image)

                reviewByAvatar.setOnClickListener {
                    UserBrowseEvent(user.userId).postEvent
                }
            }

            item.mediaModel?.let { media ->
                reviewMediaBannerImage.setImageURI(media.bannerImage)
                reviewSummaryTv.text = item.summary
                reviewByTv.setOnClickListener {
                    BrowseMediaEvent(
                        MediaBrowserMeta(
                            media.mediaId,
                            media.type!!,
                            media.title!!.romaji!!,
                            media.coverImage!!.image(context),
                            media.coverImage!!.largeImage,
                            media.bannerImage
                        ), null
                    ).postEvent
                }
            }
            reviewLikeTv.text = item.rating?.toString().naText()

            setOnClickListener {
                BrowseReviewEvent(item.reviewId).postEvent
            }
        }
    }
}
