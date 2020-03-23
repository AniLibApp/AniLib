package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.character.CharacterMediaModel
import kotlinx.android.synthetic.main.character_media_presenter.view.*

class CharacterMediaPresenter(context: Context) : Presenter<CharacterMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val mediaFormatList by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }
    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.character_media_presenter,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<CharacterMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.itemView.apply {
            characterMediaImageView.setImageURI(item.coverImage?.image)
            characterMediaTitleTv.text = item.title?.title(context)
            characterMediaFormatTv.text = item.format?.let {
                mediaFormatList[it]
            }

            characterMediaStatusTv.text  = item.status?.let {
                characterMediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }
        }
        item.title?.title(context)

    }
}
