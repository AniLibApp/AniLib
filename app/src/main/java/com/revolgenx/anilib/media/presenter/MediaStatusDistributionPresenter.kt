package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.model.stats.StatusDistributionModel
import com.revolgenx.anilib.databinding.StatusDistributionLayoutBinding
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.prettyNumberFormat

class MediaStatusDistributionPresenter(context: Context):
    BasePresenter<StatusDistributionLayoutBinding, StatusDistributionModel>(context) {

    private val mediaListStatus by lazy {
        context.resources.getStringArray(R.array.anime_list_status)
    }
    private val mediaListStatusColors by lazy {
        context.resources.getStringArray(R.array.media_list_status_color)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): StatusDistributionLayoutBinding {
        return StatusDistributionLayoutBinding.inflate(inflater, parent, false)
    }

    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onBind(
        page: Page,
        holder: Holder,
        element: Element<StatusDistributionModel>
    ) {
        super.onBind(page, holder, element)
        val item = element.data?:return

        holder.getBinding()?.apply {
            statusScoreWrapper.color = Color.parseColor(mediaListStatusColors[item.status!!])
            mediaScoreStatusTv.text = context.getString(R.string.status_distribution_string).format(item.amount?.prettyNumberFormat(), mediaListStatus[item.status!!])

            root.setOnClickListener {
                context.makeToast(msg = item.amount?.toString())
            }
        }
    }

}