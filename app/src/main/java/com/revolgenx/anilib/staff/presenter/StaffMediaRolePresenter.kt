package com.revolgenx.anilib.staff.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.StaffMediaRolePresenterBinding
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

//staff roles
class StaffMediaRolePresenter(context: Context) : BasePresenter<StaffMediaRolePresenterBinding, MediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): StaffMediaRolePresenterBinding {
        return StaffMediaRolePresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return

        binding.apply {
            staffMediaRoleImageView.setImageURI(item.coverImage?.image())
            staffMediaRoleRatingTv.text = item.averageScore
            staffMediaRoleTitleTv.text = item.title?.title()
            staffMediaRoleTv.text = item.staffRole
            staffMediaRoleFormatYearTv.text =
                context.getString(R.string.media_format_year_s).format(
                    item.format?.let { mediaFormats[it] }.naText(),
                    item.seasonYear?.toString().naText()
                )
            staffMediaRoleFormatYearTv.status = item.mediaListEntry?.status
            staffMediaRoleContainer.setOnClickListener {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        item.id,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    )
                ).postEvent
            }

            staffMediaRoleContainer.setOnLongClickListener {
                context.loginContinue {
                    OpenMediaListEditorEvent(item.id).postEvent
                }
                true
            }
        }
    }
}