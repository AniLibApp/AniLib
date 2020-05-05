package com.revolgenx.anilib.presenter.home.discover

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseMediaEvent
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.meta.ListEditorMeta
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.airing.AiringMediaModel
import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.discover_airing_presenter_layout.view.*

class DiscoverAiringPresenter(context: Context) : Presenter<AiringMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                R.layout.discover_airing_presenter_layout,
                parent,
                false
            )
        )
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<AiringMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            airingMediaSimpleDrawee.setImageURI(item.coverImage?.image)
            mediaRatingTv.text = item.averageScore?.toString().naText()
            airingMediaTitleTv.text = item.title?.title(context)
            airingFormatTv.text = context.getString(R.string.format_episode_s).format(
                item.format?.let { mediaFormats[it] }.naText(),
                item.episodes.naText()
            )
            airingTimeTv.setAiringText(item.airingTimeModel)
            airingGenreLayout.addGenre(item.genres?.take(3)) { genre ->
                BrowseGenreEvent(MediaBrowseFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image,
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    ), airingMediaSimpleDrawee
                ).postEvent
            }

            setOnLongClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image,
                            item.bannerImage
                        ), airingMediaSimpleDrawee
                    ).postEvent
                } else {
                    (parent as View).makeSnakeBar(R.string.please_log_in)
                }
                true
            }
        }
    }

}
