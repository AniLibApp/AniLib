package com.revolgenx.anilib.character.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.CharacterMediaPresenterBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

class CharacterMediaPresenter(context: Context) : BasePresenter<CharacterMediaPresenterBinding, MediaModel>(context) {
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

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): CharacterMediaPresenterBinding {
        return CharacterMediaPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {
            characterMediaImageView.setImageURI(item.coverImage?.image(context))
            characterMediaTitleTv.text = item.title?.title(context)
            characterMediaFormatTv.text =
                context.getString(R.string.media_format_year_s).format(item.format?.let {
                    mediaFormatList[it]
                }.naText(), item.seasonYear?.toString().naText())
            characterMediaFormatTv.status = item.mediaListEntry?.status
            characterMediaStatusTv.text = item.status?.let {
                characterMediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()

            characterMediaRatingTv.text = item.averageScore
            root.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        item.id,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    )
                ).postEvent
            }

            root.setOnLongClickListener {
                context.loginContinue {
                    OpenMediaListEditorEvent(item.id).postEvent
                }
                true
            }
        }

        item.title?.title(context)
    }
}