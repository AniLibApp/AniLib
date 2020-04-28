package com.revolgenx.anilib.presenter.stats

import android.content.Context
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.user.stats.*

class UserStatsPresenter(context: Context) : Presenter<BaseStatsModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return if (elementType == 0)
            Holder(
                getLayoutInflater().inflate(
                    R.layout.text_stats_presenter_layout,
                    parent,
                    false
                )
            )
        else
            Holder(
                getLayoutInflater().inflate(
                    R.layout.image_stats_presenter_layout,
                    parent,
                    false
                )
            )
    }


    override fun onBind(page: Page, holder: Holder, element: Element<BaseStatsModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.apply {
            this.updateCommonItem(item)
            when (item) {
                is StatsGenreModel -> {

                }
                is StatsTagModel -> {

                }
                is StatsStudioModel -> {

                }
                is StatsVoiceActorModel -> {

                }
                is StatsStaffModel -> {

                }
            }
        }
    }

    private fun Holder.updateCommonItem(item: BaseStatsModel) {
        itemView.apply {

        }
    }
}

