package com.revolgenx.anilib.ui.presenter.staff

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.BrowseCharacterEvent
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.data.meta.CharacterMeta
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.StaffMediaCharacterModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.StaffMediaCharacterPresenterBinding
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText

//voice roles
class StaffMediaCharacterPresenter(context: Context) :
    BasePresenter<StaffMediaCharacterPresenterBinding, StaffMediaCharacterModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)


    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): StaffMediaCharacterPresenterBinding {
        return StaffMediaCharacterPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<StaffMediaCharacterModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        holder.getBinding()?.apply {

            staffMediaImageView.setImageURI(item.coverImage?.image(context))
            staffMediaTitleTv.text = item.title?.title(context)
            staffMediaRatingTv.text = item.averageScore?.toString().naText()
            staffMediaStatusTv.text = item.status?.let {
                staffMediaStatusTv.color = Color.parseColor(statusColors[it])
                mediaStatus[it]
            }.naText()
            staffMediaFormatYearTv.text =
                context.getString(R.string.media_format_year_s).format(
                    item.format?.let { mediaFormats[it] }.naText(),
                    item.seasonYear?.toString().naText()
                )
            staffMediaFormatYearTv.status = item.mediaEntryListModel?.status
            staffMediaContainer.setOnClickListener {
                BrowseMediaEvent(
                    MediaBrowserMeta(
                        item.mediaId,
                        item.type!!,
                        item.title!!.romaji!!,
                        item.coverImage!!.image(context),
                        item.coverImage!!.largeImage,
                        item.bannerImage
                    ), staffMediaImageView
                ).postEvent
            }

            staffMediaContainer.setOnLongClickListener {
                if (context.loggedIn()) {
                    ListEditorEvent(
                        ListEditorMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.title(context)!!,
                            item.coverImage!!.image(context),
                            item.bannerImage
                        ), staffMediaImageView
                    ).postEvent
                } else {
                    context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                }
                true
            }

            staffCharacterImageView.setImageURI(item.characterImageModel?.image)
            staffCharacterNameTv.text = item.characterName?.full
            staffCharacterRoleTv.text = item.mediaRole?.let { characterRoles[it] }.naText()

            staffCharacterContainer.setOnClickListener {
                BrowseCharacterEvent(
                    CharacterMeta(
                        item.characterId ?: -1,
                        item.characterImageModel?.image
                    ),
                    staffCharacterImageView
                ).postEvent
            }
        }
    }

}
