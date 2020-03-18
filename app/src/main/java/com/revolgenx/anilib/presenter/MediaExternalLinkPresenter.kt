package com.revolgenx.anilib.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.MediaExternalLinkModel
import com.revolgenx.anilib.util.colorsMap
import com.revolgenx.anilib.util.openLink
import kotlinx.android.synthetic.main.external_link_presenter.view.*
import timber.log.Timber

class MediaExternalLinkPresenter(context: Context) : Presenter<MediaExternalLinkModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.external_link_presenter,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaExternalLinkModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            mediaExternalLinkTv.text = item.site

            item.site?.toLowerCase()?.let { st ->
                val drawable = when (st) {
                    "twitter" ->
                        R.drawable.ic_twitter
                    "funimation" ->
                        R.drawable.ic_funimation

                    "crunchyroll" ->
                        R.drawable.ic_crunchyroll_logo
                    "animelab" ->
                        R.drawable.ic_animelab
                    "netflix" ->
                        R.drawable.ic_netflix
                    "youtube" ->
                        R.drawable.ic_youtube
                    else ->
                        R.drawable.ic_link

                }

                mediaExternalLinkTv.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    0,
                    0,
                    0
                )
            }

            setOnClickListener {
                context.openLink(item.url)
            }
            colorsMap[item.site?.toLowerCase()]?.let {
                mediaExternalLinkTv.color = Color.parseColor(it)
            }
        }
    }

}