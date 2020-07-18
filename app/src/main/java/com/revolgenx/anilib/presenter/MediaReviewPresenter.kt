package com.revolgenx.anilib.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseReviewEvent
import com.revolgenx.anilib.event.UserBrowseEvent
import com.revolgenx.anilib.model.MediaReviewModel
import kotlinx.android.synthetic.main.media_review_presenter_layout.view.*

class MediaReviewPresenter(context: Context) : Presenter<MediaReviewModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(
                R.layout.media_review_presenter_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("RestrictedApi")
    override fun onBind(page: Page, holder: Holder, element: Element<MediaReviewModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            reviewUserIv.setImageURI(item.userPrefModel?.avatar?.large)

            reviewSummaryTv.text = item.summary
            likedReviewTv.text = item.rating?.toString()
            likedReviewTv.supportCompoundDrawablesTintList = ColorStateList.valueOf(
                DynamicTheme.getInstance().get().tintSurfaceColor
            )
            var bgColor = DynamicTheme.getInstance().get().surfaceColor
            if (!DynamicColorUtils.isColorDark(DynamicTheme.getInstance().get().surfaceColor)) {
                bgColor = DynamicColorUtils.getLighterColor(
                    DynamicTheme.getInstance().get().surfaceColor,
                    0.3f
                )
            }
            reviewContainer.backgroundTintList = ColorStateList.valueOf(
                bgColor
            )

            reviewUserIv.setOnClickListener {
                UserBrowseEvent(item.userPrefModel?.userId ?: -1).postEvent
            }

            reviewContainer.setOnClickListener{
                BrowseReviewEvent(item.reviewId).postEvent
            }
        }
    }
}
