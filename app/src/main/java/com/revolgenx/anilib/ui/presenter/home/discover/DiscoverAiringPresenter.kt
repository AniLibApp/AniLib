package com.revolgenx.anilib.ui.presenter.home.discover

import android.content.Context
import android.view.LayoutInflater
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
import com.revolgenx.anilib.data.model.airing.AiringMediaModel
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.DiscoverAiringPresenterLayoutBinding
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText

class DiscoverAiringPresenter(context: Context) : BasePresenter<DiscoverAiringPresenterLayoutBinding,AiringMediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): DiscoverAiringPresenterLayoutBinding {
        val binding =  DiscoverAiringPresenterLayoutBinding.inflate(inflater, parent, false)

        binding.mediaMetaBackground.setBackgroundColor(
            ColorUtils.setAlphaComponent(
                DynamicTheme.getInstance().get().backgroundColor, 220
            )
        )

        return binding
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<AiringMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding()?:return

        binding.apply {
            airingMediaSimpleDrawee.setImageURI(item.coverImage?.image(context))
            mediaRatingTv.text = item.averageScore?.toString().naText()
            airingMediaTitleTv.text = item.title?.title(context)
            airingFormatTv.text = context.getString(R.string.format_episode_s).format(
                item.format?.let { mediaFormats[it] }.naText(),
                item.episodes.naText()
            )

            airingFormatTv.status = item.mediaEntryListModel?.status

            airingTimeTv.setAiringText(item.airingTimeModel)
            airingGenreLayout.addGenre(item.genres?.take(3)) { genre ->
                BrowseGenreEvent(MediaSearchFilterModel().also {
                    it.genre = listOf(genre.trim())
                }).postEvent
            }

            root.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    ), airingMediaSimpleDrawee
                ).postEvent
            }

            root.setOnLongClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        ), airingMediaSimpleDrawee
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }
        }
    }

}
