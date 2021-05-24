package com.revolgenx.anilib.social.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.databinding.ListActivityPresenterLayoutBinding
import com.revolgenx.anilib.databinding.TextActivityPresenterLayoutBinding
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.ListActivityModel
import com.revolgenx.anilib.social.data.model.TextActivityModel
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.ui.presenter.BasePresenter

class ActivityUnionPresenter(context: Context) :
    BasePresenter<ViewBinding, ActivityUnionModel>(context) {
    override val elementTypes: Collection<Int> =
        listOf(
            ActivityType.TEXT.ordinal,
            ActivityType.MEDIA_LIST.ordinal,
            ActivityType.ANIME_LIST.ordinal,
            ActivityType.MANGA_LIST.ordinal
        )

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (elementType) {
            ActivityType.MEDIA_LIST.ordinal,
            ActivityType.ANIME_LIST.ordinal,
            ActivityType.MANGA_LIST.ordinal -> {
                ListActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            else -> {
                TextActivityPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<ActivityUnionModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        val type = element.type

        if (type == ActivityType.TEXT.ordinal) {
            (holder.getBinding() as TextActivityPresenterLayoutBinding).bind(item as TextActivityModel)
        } else {
            (holder.getBinding() as ListActivityPresenterLayoutBinding).bind(item as ListActivityModel)
        }
    }

    private fun TextActivityPresenterLayoutBinding.bind(item: TextActivityModel) {
        AlMarkwonFactory.getMarkwon().setParsedMarkdown(textActivityTv, item.textSpanned)
    }

    private fun ListActivityPresenterLayoutBinding.bind(item: ListActivityModel) {
        listActivityTv.text = item.getProgressStatus
    }

}