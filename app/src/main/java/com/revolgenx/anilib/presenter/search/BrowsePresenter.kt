package com.revolgenx.anilib.presenter.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.event.*
import com.revolgenx.anilib.meta.*
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.search.CharacterSearchModel
import com.revolgenx.anilib.model.search.MediaSearchModel
import com.revolgenx.anilib.model.search.StaffSearchModel
import com.revolgenx.anilib.model.search.StudioSearchModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.browse_character_layout.view.*
import kotlinx.android.synthetic.main.browse_media_layout.view.*
import kotlinx.android.synthetic.main.browse_staff_layout.view.*
import kotlinx.android.synthetic.main.browse_studio_layout.view.*

class BrowsePresenter(context: Context, private val lifecycleOwner: LifecycleOwner) :
    Presenter<BaseModel>(context) {
    override val elementTypes: Collection<Int>
        get() = BrowseTypes.values().map { it.ordinal }

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
            BrowseTypes.ANIME.ordinal, BrowseTypes.MANGA.ordinal -> {
                R.layout.browse_media_layout
            }
            BrowseTypes.CHARACTER.ordinal -> {
                R.layout.browse_character_layout
            }
            BrowseTypes.STAFF.ordinal -> {
                R.layout.browse_staff_layout
            }
            BrowseTypes.STUDIO.ordinal -> {
                R.layout.browse_studio_layout
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
        when (holder.elementType) {
            BrowseTypes.ANIME.ordinal, BrowseTypes.MANGA.ordinal -> {
                holder.itemView.updateMedia(item)
            }
            BrowseTypes.CHARACTER.ordinal -> {
                holder.itemView.updateCharacter(item)
            }
            BrowseTypes.STAFF.ordinal -> {
                holder.itemView.updateStaff(item)
            }
            BrowseTypes.STUDIO.ordinal -> {
                holder.itemView.updateStudio(item)
            }
        }
    }


    private fun View.updateMedia(item: BaseModel) {
        val data = item as MediaSearchModel
        searchMediaImageView.setImageURI(data.coverImage?.image(context))
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

        setOnClickListener {
            BrowseMediaEvent(
                MediaBrowserMeta(
                    data.mediaId,
                    data.type!!,
                    data.title!!.romaji!!,
                    data.coverImage!!.image(context),
                    data.coverImage!!.largeImage,
                    data.bannerImage
                ), searchMediaImageView
            ).postEvent
        }

        setOnLongClickListener {
            if (context.loggedIn()) {
                ListEditorEvent(
                    ListEditorMeta(
                        data.mediaId,
                        data.type!!,
                        data.title!!.title(context)!!,
                        data.coverImage!!.image(context),
                        data.bannerImage
                    ), searchMediaImageView
                ).postEvent
            } else {
                (parent as View).makeSnakeBar(R.string.please_log_in)
            }
            true
        }

    }

    private fun View.updateCharacter(item: BaseModel) {
        val data = item as CharacterSearchModel
        browseCharacterImageView.setImageURI(data.characterImageModel?.image)
        browseCharacterNameTv.text = data.name?.full
        setOnClickListener {
            BrowseCharacterEvent(
                CharacterMeta(
                    item.characterId ?: -1,
                    item.characterImageModel?.image
                ),
                browseCharacterImageView
            ).postEvent
        }
    }

    private fun View.updateStaff(item: BaseModel) {
        val data = item as StaffSearchModel
        browseStaffImageView.setImageURI(data.staffImage?.image)
        browseStaffNameTv.text = data.staffName?.full
        setOnClickListener {
            BrowseStaffEvent(StaffMeta(item.staffId ?: -1, item.staffImage?.image)).postEvent
        }
    }

    private fun View.updateStudio(item: BaseModel) {
        val data = item as StudioSearchModel
        browseStudioHeader.text = data.studioName + " >>"
        browseStudioMedia.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        browseStudioHeader.setOnClickListener {
            BrowseStudioEvent(StudioMeta(data.studioId)).postEvent
        }

        Adapter.builder(lifecycleOwner)
            .addSource(Source.fromList(data.studioMedia!!))
            .addPresenter(BrowseStudioMediaPresenter())
            .into(browseStudioMedia)
    }


    inner class BrowseStudioMediaPresenter : Presenter<MediaSearchModel>(context) {
        override val elementTypes: Collection<Int>
            get() = listOf(0)

        override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
            return Holder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.browse_studio_media_presenter,
                    parent,
                    false
                )
            )
        }

        override fun onBind(page: Page, holder: Holder, element: Element<MediaSearchModel>) {
            super.onBind(page, holder, element)
            val item = element.data ?: return
            holder.itemView.updateMedia(item)
        }
    }
}
