package com.revolgenx.anilib.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseRankEvent
import com.revolgenx.anilib.model.stats.MediaStatsRankingModel
import com.revolgenx.anilib.type.MediaRankType
import com.revolgenx.anilib.util.dp
import kotlinx.android.synthetic.main.ranking_presenter_layout.view.*

class RankingsPresenter(context: Context, private val mediaType: Int = -1) :
    Presenter<MediaStatsRankingModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val ratedDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_star)?.also {
            it.setTint(ContextCompat.getColor(context, R.color.rated_color))
        }
    }

    private val popularDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_favorite)?.also {
            it.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.popular_color
                )
            )
        }

    }

    private val seasons by lazy {
        context.resources.getStringArray(R.array.media_season)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ranking_presenter_layout,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(page: Page, holder: Holder, element: Element<MediaStatsRankingModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            rankingTv.text =
                (item.rank?.let { "#$it " } ?: "") +
                        (item.context?.trim()?.split(" ")?.joinToString(separator = " ") { it.capitalize() }
                        + " "
                            ?: "") +
                        (item.season?.let { seasons[it] + " " } ?: "") +
                        (item.year ?: "")

            when (item.rankType) {
                MediaRankType.POPULAR.ordinal -> {
                    rankingIv.setImageDrawable(popularDrawable)
                    setOnClickListener {
                        BrowseRankEvent(item.rankType!!, mediaType)
                    }
                }
                MediaRankType.RATED.ordinal -> {
                    rankingIv.setImageDrawable(ratedDrawable)
                    setOnClickListener {
                        BrowseRankEvent(item.rankType!!, mediaType)
                    }
                }
            }
        }
    }
}