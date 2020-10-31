package com.revolgenx.anilib.presenter.list

import android.content.Context
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.DisplayMode.*
import com.revolgenx.anilib.meta.MediaListMeta
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.preference.getMediaListGridPresenter
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.preference.userName
import com.revolgenx.anilib.presenter.list.binding.CardHolderBinding
import com.revolgenx.anilib.presenter.list.binding.CompactHolderBinding
import com.revolgenx.anilib.presenter.list.binding.NormalHolderBinding
import com.revolgenx.anilib.viewmodel.media_list.MediaListCollectionViewModel
import kotlinx.android.synthetic.main.media_list_collection_card_presenter_layout.view.*

class MediaListCollectionPresenter(
    context: Context,
    mediaListMeta: MediaListMeta,
    private val viewModel: MediaListCollectionViewModel
) :
    Presenter<MediaListModel>(context) {

    companion object{
        val tintSurfaceColor by lazy {
            DynamicTheme.getInstance().get().tintSurfaceColor
        }
    }

    override val elementTypes: Collection<Int> = listOf(0)
    private val mediaFormats =
        context.resources.getStringArray(R.array.media_format)
    private val mediaStatus =
        context.resources.getStringArray(R.array.media_status)
    private val statusColors =
        context.resources.getStringArray(R.array.status_color)
    private val isLoggedInUser =
        mediaListMeta.userId == context.userId() || mediaListMeta.userName == context.userName()
    private val displayMode = values()[getMediaListGridPresenter()]


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                when (displayMode) {
                    COMPACT -> {
                        R.layout.media_list_collection_compact_presenter_layout
                    }
                    NORMAL -> {
                        R.layout.media_list_collection_normal_presenter_layout
                    }
                    else -> {
                        R.layout.media_list_collection_card_presenter_layout
                    }
                },
                parent,
                false
            ).also {
                if(displayMode == CARD){
                    it.mediaMetaBackground.setBackgroundColor(
                        ColorUtils.setAlphaComponent(
                            DynamicTheme.getInstance().get().backgroundColor,
                            200
                        )
                    )
                }
            }
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return


        when(displayMode){
            COMPACT -> CompactHolderBinding.bind(
                holder.itemView,
                item,
                mediaFormats,
                mediaStatus,
                statusColors,
                isLoggedInUser,
                viewModel
            )
            NORMAL -> NormalHolderBinding.bind(
                holder.itemView,
                item,
                mediaFormats,
                mediaStatus,
                statusColors,
                isLoggedInUser,
                viewModel
            )
            CARD -> {
                CardHolderBinding.bind(
                    holder.itemView,
                    item,
                    mediaFormats,
                    statusColors,
                    isLoggedInUser,
                    viewModel
                )
            }
        }

    }

}
