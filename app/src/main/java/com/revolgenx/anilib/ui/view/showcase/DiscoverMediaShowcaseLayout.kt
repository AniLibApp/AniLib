package com.revolgenx.anilib.ui.view.showcase

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.pranavpandey.android.dynamic.support.widget.DynamicCardView
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.disableCardStyleInHomeScreen
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.home.SelectableCommonMediaModel
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.ui.view.widgets.DynamicDrawableTextView
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.util.naText

class DiscoverMediaShowcaseLayout : LinearLayout {

    val showcaseRecyclerView: RecyclerView
    var draweeView: SimpleDraweeView? = null
    var titleTv: TextView? = null
    var descriptionTv: TextView? = null
    var popularityTv: DynamicDrawableTextView? = null
    var favouriteTv: DynamicDrawableTextView? = null

    private var mediaModel: SelectableCommonMediaModel? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        orientation = VERTICAL
        clipChildren = false
        if (!disableCardStyleInHomeScreen()) {
            val showcaseCardView = DynamicCardView(context).also { vi ->
                vi.id = R.id.showCaseCardView
                vi.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, context.resources.getDimensionPixelSize(
                        R.dimen.discover_media_height
                    )
                ).also { params ->
                    params.setMargins(dp(10f),0,dp(10f),0)
                }
            }


            //add view inside cardview

            val constraintLayout = ConstraintLayout(context).also { vi ->
                vi.id = R.id.showCaseConstraintLayout
                vi.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                )
            }




            draweeView = SimpleDraweeView(context).also { vi ->
                vi.id = R.id.showCaseImageView
                vi.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                )
            }

            showcaseCardView.setOnClickListener {
                mediaModel?.let { item ->
                    BrowseMediaEvent(
                        MediaBrowserMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.romaji!!,
                            item.coverImage!!.image(context),
                            item.coverImage!!.largeImage,
                            item.bannerImage
                        ), draweeView
                    ).postEvent
                }
            }

            val translucentBg = FrameLayout(context).also { vi ->
                vi.id = R.id.showCaseImageView
                vi.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
                )
                vi.setBackgroundResource(R.drawable.translucent_dark_background)
            }


            titleTv = TextView(context).also { vi ->
                vi.id = R.id.showCaseTitleView
                vi.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                )
                vi.setTextColor(Color.WHITE)
                vi.typeface = ResourcesCompat.getFont(context, R.font.cabin_medium)
                vi.maxLines = 1
                vi.textSize = 16f
                vi.ellipsize = TextUtils.TruncateAt.END
                vi.setPadding(dp(10f))
            }


            popularityTv = DynamicDrawableTextView(context).also { vi ->
                vi.id = R.id.showCasePopularTv
                vi.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                )
                vi.setTextColor(Color.WHITE)
                vi.typeface = ResourcesCompat.getFont(context, R.font.cabin_medium)
                vi.maxLines = 1
                vi.textSize = 16f
                vi.setPadding(dp(10f), 0, dp(10f), dp(10f))
                vi.setDrawables(R.drawable.ic_popular, null, Color.YELLOW)
            }


            favouriteTv = DynamicDrawableTextView(context).also { vi ->
                vi.id = R.id.showCaseFavouriteTv
                vi.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                )
                vi.setTextColor(Color.WHITE)
                vi.typeface = ResourcesCompat.getFont(context, R.font.cabin_medium)
                vi.maxLines = 1
                vi.textSize = 16f
                vi.setPadding(dp(10f), 0, dp(10f), dp(10f))
                vi.setDrawables(R.drawable.ic_favourite, null, Color.RED)
            }



            descriptionTv = TextView(context).also { vi ->
                vi.id = R.id.showCaseDescriptionView
                vi.layoutParams = LayoutParams(
                    0, LayoutParams.WRAP_CONTENT
                )
                vi.setTextColor(Color.WHITE)
                vi.typeface = ResourcesCompat.getFont(context, R.font.cabin_regular)
                vi.maxLines = 5
                vi.textSize = 16f
                vi.ellipsize = TextUtils.TruncateAt.END
                vi.setPadding(dp(10f))
            }


            constraintLayout.addView(draweeView)
            constraintLayout.addView(translucentBg)
            constraintLayout.addView(descriptionTv)
            constraintLayout.addView(titleTv)
            constraintLayout.addView(favouriteTv)
            constraintLayout.addView(popularityTv)

            val constraintSet = ConstraintSet().also { it.clone(constraintLayout) }


            val descriptionTv = descriptionTv!!
            val titleTv = titleTv!!
            val favouriteTv = favouriteTv!!
            val popularityTv = popularityTv!!

            constraintSet.connect(
                descriptionTv.id,
                ConstraintSet.START,
                constraintLayout.id,
                ConstraintSet.START
            )
            constraintSet.connect(
                descriptionTv.id,
                ConstraintSet.TOP,
                constraintLayout.id,
                ConstraintSet.TOP
            )

            constraintSet.connect(
                descriptionTv.id,
                ConstraintSet.END,
                constraintLayout.id,
                ConstraintSet.END
            )


            constraintSet.connect(
                titleTv.id,
                ConstraintSet.START,
                constraintLayout.id,
                ConstraintSet.START
            )
            constraintSet.connect(
                titleTv.id,
                ConstraintSet.END,
                constraintLayout.id,
                ConstraintSet.END
            )
            constraintSet.connect(
                titleTv.id,
                ConstraintSet.TOP,
                descriptionTv.id,
                ConstraintSet.BOTTOM
            )
            constraintSet.connect(
                titleTv.id,
                ConstraintSet.BOTTOM,
                favouriteTv.id,
                ConstraintSet.TOP
            )



            constraintSet.connect(
                popularityTv.id,
                ConstraintSet.START,
                constraintLayout.id,
                ConstraintSet.START
            )
            constraintSet.connect(
                popularityTv.id,
                ConstraintSet.BOTTOM,
                constraintLayout.id,
                ConstraintSet.BOTTOM
            )
            constraintSet.connect(
                favouriteTv.id,
                ConstraintSet.BOTTOM,
                constraintLayout.id,
                ConstraintSet.BOTTOM
            )

            constraintSet.connect(
                favouriteTv.id,
                ConstraintSet.END,
                constraintLayout.id,
                ConstraintSet.END
            )

            constraintSet.connect(
                favouriteTv.id,
                ConstraintSet.START,
                popularityTv.id,
                ConstraintSet.END
            )


            constraintSet.setHorizontalBias(descriptionTv.id, 0f)
            constraintSet.setHorizontalBias(titleTv.id, 0f)
            constraintSet.setHorizontalBias(favouriteTv.id, 0f)
            constraintSet.setVerticalBias(titleTv.id, 1f)


            constraintSet.applyTo(constraintLayout)

            showcaseCardView.addView(constraintLayout)
            addView(showcaseCardView)
        }

        showcaseRecyclerView = DynamicRecyclerView(context).also {
            it.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                context.resources.getDimensionPixelSize(
                    R.dimen.discover_recycler_height
                )
            )
            it.isNestedScrollingEnabled = false
        }

        addView(showcaseRecyclerView)
    }


    fun bindShowCaseMedia(media: SelectableCommonMediaModel) {
        if (this.mediaModel == media || disableCardStyleInHomeScreen() ) return
        this.mediaModel?.let {
            it.isSelected = false
            it.selectionListener?.invoke(false)
        }

        this.mediaModel = media

        draweeView?.setImageURI(media.bannerImage)
        titleTv?.text = media.title?.title(context)
        descriptionTv?.text =
            HtmlCompat.fromHtml(media.description!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
        popularityTv?.text = media.popularity?.toString().naText()
        favouriteTv?.text = media.favourites?.toString().naText()
    }
}