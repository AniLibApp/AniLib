package com.revolgenx.anilib.ui.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseRankEvent
import com.revolgenx.anilib.data.model.user.stats.MediaStatsRankingModel
import com.revolgenx.anilib.databinding.RankingPresenterLayoutBinding
import com.revolgenx.anilib.type.MediaRankType

class RankingsPresenter(context: Context, private val mediaType: Int?) :
    BasePresenter<RankingPresenterLayoutBinding, MediaStatsRankingModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val ratedDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_star)?.mutate()?.also {
            it.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.rated_color
                )
            )
        }
    }

    private val popularDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_favourite)?.mutate()?.also {
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

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): RankingPresenterLayoutBinding {
        return RankingPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onBind(page: Page, holder: Holder, element: Element<MediaStatsRankingModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            rankingTv.text =
                (item.rank?.let { "#$it " } ?: "") +
                        (item.context?.trim()?.split(" ")?.joinToString(separator = " ") { it.capitalize() }
                        + " ") +
                        (item.season?.let { seasons[it] + " " } ?: "") +
                        (item.year ?: "")

            when (item.rankType) {
                MediaRankType.POPULAR.ordinal -> {
                    rankingIv.setImageDrawable(popularDrawable)
                    root.setOnClickListener {
                        BrowseRankEvent(item.rankType!!, mediaType)
                    }
                }
                MediaRankType.RATED.ordinal -> {
                    rankingIv.setImageDrawable(ratedDrawable)
                    root.setOnClickListener {
                        BrowseRankEvent(item.rankType!!, mediaType)
                    }
                }
            }
        }
    }
}