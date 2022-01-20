package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.model.MediaExternalLinkModel
import com.revolgenx.anilib.databinding.ExternalLinkPresenterBinding
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.util.colorsMap
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.openLink

class MediaExternalLinkPresenter(context: Context) :
    BasePresenter<ExternalLinkPresenterBinding, MediaExternalLinkModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ExternalLinkPresenterBinding {
        return ExternalLinkPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaExternalLinkModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
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
                        0

                }

                mediaExternalLinkTv.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    0,
                    0,
                    0
                )
                externalLinkWrapper.setOnClickListener {
                    context.openLink(item.url)
                }

                externalLinkWrapper.setOnLongClickListener {
                    context.copyToClipBoard(item.url)
                    true
                }

                st.toLowerCase().let {
                    colorsMap[it]?.let {
                        val linkColor = Color.parseColor(it)
                        externalLinkWrapper.color = linkColor
                    }?:let {
                        externalLinkWrapper.color = Color.BLACK
                    }
                }

            }
        }
    }

}