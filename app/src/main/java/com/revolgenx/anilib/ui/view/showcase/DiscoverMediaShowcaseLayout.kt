package com.revolgenx.anilib.ui.view.showcase

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.home.SelectableCommonMediaModel
import com.revolgenx.anilib.util.dp

class DiscoverMediaShowcaseLayout : LinearLayout {

    val showcaseRecyclerView: RecyclerView
    val draweeView: SimpleDraweeView

    private var mediaModel: SelectableCommonMediaModel? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        orientation = VERTICAL

        val showcaseCardView = DynamicCardView(context).also { vi ->
            vi.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelSize(
                    R.dimen.discover_media_height
                )
            ).also { params ->
                params.setMargins(dp(10f))
            }
        }


        //add view inside cardview

        val frameLayout = DynamicFrameLayout(context).also { vi ->
            vi.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
        }

        draweeView = SimpleDraweeView(context).also { vi ->
            vi.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
        }

        frameLayout.setBackgroundResource(R.drawable.transparent_gradient_background_black)
        frameLayout.addView(draweeView)




        showcaseCardView.addView(frameLayout)


        showcaseRecyclerView = DynamicRecyclerView(context).also {
            it.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                it.setMargins(0, dp(10f), 0, dp(10f))
            }
            it.isNestedScrollingEnabled = false
        }

        addView(showcaseCardView)
        addView(showcaseRecyclerView)
    }


    fun bindShowCaseMedia(media: SelectableCommonMediaModel) {
        if (this.mediaModel == media) return
        this.mediaModel?.let {
            it.isSelected = false
            it.selectionListener?.invoke(false)
        }

        this.mediaModel = media

        draweeView.setImageURI(media.bannerImage)
    }
}