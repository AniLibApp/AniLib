package com.revolgenx.anilib.ui.presenter.home

import android.content.Context
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseGenreEvent
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import kotlinx.android.synthetic.main.media_presenter_layout.view.*

class MediaPresenter(context: Context) : Presenter<CommonMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            getLayoutInflater().inflate(
                R.layout.media_presenter_layout,
                parent,
                false
            ).also {
                it.mediaMetaBackground.setBackgroundColor(
                    ColorUtils.setAlphaComponent(
                        DynamicTheme.getInstance().get().backgroundColor,
                        220
                    )
                )
            })
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<CommonMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            mediaSimpleDrawee.setImageURI(item.coverImage?.image(context))
            mediaRatingTv.text = item.averageScore?.toString().naText()
            mediaTitleTv.text = item.title?.title(context)
            mediaFormatTv.text = context.getString(R.string.format_episode_s).format(
                item.format?.let { mediaFormats[it] }.naText(),
                item.episodes.naText()
            )
            mediaFormatTv.status = item.mediaEntryListModel?.status

            mediaGenreLayout.addGenre(item.genres?.take(3)) { genre ->
                BrowseGenreEvent(MediaSearchFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    ), mediaSimpleDrawee
                ).postEvent
            }

            setOnLongClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        ), mediaSimpleDrawee
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }
        }
    }


}
