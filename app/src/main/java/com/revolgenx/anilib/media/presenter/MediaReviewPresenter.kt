package com.revolgenx.anilib.media.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.media.data.model.MediaReviewModel
import com.revolgenx.anilib.databinding.MediaReviewPresenterLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenReviewEvent
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.common.presenter.BasePresenter

class MediaReviewPresenter(context: Context) : BasePresenter<MediaReviewPresenterLayoutBinding, MediaReviewModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaReviewPresenterLayoutBinding {
        return MediaReviewPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onBind(page: Page, holder: Holder, element: Element<MediaReviewModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {
            reviewUserIv.setImageURI(item.user?.avatar?.large)

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
                OpenUserProfileEvent(item.user?.id ?: -1).postEvent
            }

            reviewContainer.setOnClickListener{
                OpenReviewEvent(item.id).postEvent
            }
        }
    }
}
