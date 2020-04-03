package com.revolgenx.anilib.presenter.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.AdvanceSearchTypes
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.search.*
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.advance_search_media_layout.view.*

class AdvanceSearchPresenter(context: Context) : Presenter<BaseModel>(context) {
    override val elementTypes: Collection<Int>
        get() = AdvanceSearchTypes.values().map { it.ordinal }

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }


    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        val holderRes = when (elementType) {
            AdvanceSearchTypes.ANIME.ordinal -> {
                R.layout.advance_search_media_layout
            }
            AdvanceSearchTypes.CHARACTER.ordinal -> {
                R.layout.advance_search_character_layout
            }
            AdvanceSearchTypes.STAFF.ordinal -> {
                R.layout.advance_search_staff_layout
            }
            AdvanceSearchTypes.STUDIO.ordinal -> {
                R.layout.advance_search_studio_layout
            }
            else -> {
                R.layout.empty_layout
            }
        }

        return Holder(LayoutInflater.from(parent.context).inflate(holderRes, parent, false))
    }

    override fun onBind(page: Page, holder: Holder, element: Element<BaseModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        if (holder.elementType == AdvanceSearchTypes.ANIME.ordinal) {
            holder.itemView.updateAnime(item)
        }
    }


    private fun View.updateAnime(item: BaseModel) {
        val data = item as AnimeAdvanceSearchModel
        searchMediaImageView.setImageURI(data.coverImage?.image)
        searchMediaTitleTv.text = data.title?.title(context)
        searchMediaRatingTv.text = data.averageScore?.toString().naText()
        searchMediaFormatTv.text =
            context.getString(R.string.media_format_year_s).format(data.format?.let {
                mediaFormats[it]
            }.naText(), data.seasonYear?.toString().naText())
        searchMediaStatusTv.text = data.status?.let {
            searchMediaStatusTv.color = Color.parseColor(statusColors[it])
            mediaStatus[it]
        }
    }

    private fun View.updateManga(item: BaseModel) {
        val data = item as MangaAdvanceSearchModel
    }

    private fun View.updateCharacter(item: BaseModel) {
        val data = item as CharacterAdvanceSearchModel
    }

    private fun View.updateStaff(item: BaseModel) {
        val data = item as StaffAdvanceSearchModel
    }

    private fun View.updateStudio(item: BaseModel) {
        val data = item as StudioAdvanceSearchModel
    }

}
