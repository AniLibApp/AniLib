package com.revolgenx.anilib.ui.presenter.media

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.MediaViewPresentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast

class MediaViewPresenter(requireContext: Context):BasePresenter<MediaViewPresentLayoutBinding,CommonMediaModel>(requireContext) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaViewPresentLayoutBinding {
        return MediaViewPresentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<CommonMediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return

        binding.apply {
            mediaTitleTv.text = item.title!!.title(context)
            mediaCoverImageIv.setImageURI(item.coverImage?.image)

            root.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    )
                ).postEvent
            }

            root.setOnLongClickListener {
                if (context.loggedIn()) {
                    OpenMediaListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        )
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }
        }
    }
}
