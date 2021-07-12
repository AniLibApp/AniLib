package com.revolgenx.anilib.ui.presenter.staff

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.staff.StaffMediaRoleModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.StaffMediaRolePresenterBinding
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText

//staff roles
class StaffMediaRolePresenter(context: Context) : BasePresenter<StaffMediaRolePresenterBinding, StaffMediaRoleModel>(context) {
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

    override fun onBind(page: Page, holder: Holder, element: Element<StaffMediaRoleModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return

        binding.apply {
            staffMediaRoleImageView.setImageURI(item.coverImage?.image(context))
            staffMediaRoleRatingTv.text = item.averageScore
            staffMediaRoleTitleTv.text = item.title?.title(context)
            staffMediaRoleTv.text = item.staffRole
            staffMediaRoleFormatYearTv.text =
                context.getString(R.string.media_format_year_s).format(
                    item.format?.let { mediaFormats[it] }.naText(),
                    item.seasonYear?.toString().naText()
                )
            staffMediaRoleFormatYearTv.status = item.mediaEntryListModel?.status
            staffMediaRoleContainer.setOnClickListener {
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

            staffMediaRoleContainer.setOnLongClickListener {
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
