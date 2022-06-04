package com.revolgenx.anilib.staff.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.StaffMediaCharacterPresenterBinding
import com.revolgenx.anilib.common.event.OpenCharacterEvent
import com.revolgenx.anilib.common.event.OpenMediaInfoEvent
import com.revolgenx.anilib.common.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.common.preference.staffMediaCharacterDisplayModePref
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.constant.StaffMediaCharacterDisplayMode
import com.revolgenx.anilib.databinding.StaffMediaCharacterNormalPresenterBinding
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.ui.view.IndicatorTextView
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.view.score.MediaScoreBadge
import com.revolgenx.anilib.util.loginContinue
import com.revolgenx.anilib.util.naText

//voice roles
class StaffMediaCharacterPresenter(context: Context) :
    BasePresenter<ViewBinding, MediaModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private val displayMode = StaffMediaCharacterDisplayMode.values()[staffMediaCharacterDisplayModePref]


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
    ): ViewBinding {
        return when (displayMode) {
            StaffMediaCharacterDisplayMode.COMPACT -> {
                StaffMediaCharacterPresenterBinding.inflate(inflater, parent, false)
            }
            StaffMediaCharacterDisplayMode.NORMAL -> {
                StaffMediaCharacterNormalPresenterBinding.inflate(inflater, parent, false)
            }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return

        var staffMediaImageView: SimpleDraweeView? = null
        var staffMediaTitleTv: TextView? = null
        var staffMediaRatingTv: MediaScoreBadge? = null
        var staffMediaStatusTv: DynamicTextView? = null
        var staffMediaFormatYearTv: IndicatorTextView? = null

        var staffCharacterImageView: SimpleDraweeView? = null
        var staffCharacterNameTv: TextView? = null
        var staffCharacterRoleTv: TextView? = null

        var staffMediaContainer: View? = null
        var staffCharacterContainer: View? = null

        when (binding) {
            is StaffMediaCharacterPresenterBinding -> {
                staffMediaImageView = binding.staffMediaImageView
                staffMediaTitleTv = binding.staffMediaTitleTv
                staffMediaRatingTv = binding.staffMediaRatingTv
                staffMediaStatusTv = binding.staffMediaStatusTv
                staffMediaFormatYearTv = binding.staffMediaFormatYearTv
                staffCharacterImageView = binding.staffCharacterImageView
                staffCharacterNameTv = binding.staffCharacterNameTv
                staffCharacterRoleTv = binding.staffCharacterRoleTv
                staffMediaContainer = binding.staffMediaContainer
                staffCharacterContainer = binding.staffCharacterContainer
            }
            is StaffMediaCharacterNormalPresenterBinding -> {
                staffMediaImageView = binding.staffMediaImageView
                staffMediaTitleTv = binding.staffMediaTitleTv
                staffMediaRatingTv = binding.staffMediaRatingTv
                staffMediaStatusTv = binding.staffMediaStatusTv
                staffMediaFormatYearTv = binding.staffMediaFormatYearTv
                staffCharacterImageView = binding.staffCharacterImageView
                staffCharacterNameTv = binding.staffCharacterNameTv
                staffCharacterRoleTv = binding.staffCharacterRoleTv
                staffMediaContainer = binding.staffMediaContainer
                staffCharacterContainer = binding.staffCharacterContainer
            }
        }


        staffMediaImageView?.setImageURI(item.coverImage?.image())
        staffMediaTitleTv?.text = item.title?.title()
        staffMediaRatingTv?.text = item.averageScore
        staffMediaStatusTv?.text = item.status?.let {
            staffMediaStatusTv?.color = Color.parseColor(statusColors[it])
            mediaStatus[it]
        }.naText()
        staffMediaFormatYearTv?.text =
            context.getString(R.string.media_format_year_s).format(
                item.format?.let { mediaFormats[it] }.naText(),
                item.seasonYear?.toString().naText()
            )
        staffMediaFormatYearTv?.status = item.mediaListEntry?.status
        staffMediaContainer?.setOnClickListener {
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

        staffMediaContainer?.setOnLongClickListener {
            context.loginContinue {
                OpenMediaListEditorEvent(item.id).postEvent
            }
            true
        }

        val character = item.character ?: return
        staffCharacterImageView?.setImageURI(character.image?.image)
        staffCharacterNameTv?.text = character.name?.full
        staffCharacterRoleTv?.text = item.characterRole?.let { characterRoles[it] }.naText()

        staffCharacterContainer?.setOnClickListener {
            OpenCharacterEvent(
                character.id
            ).postEvent
        }
    }
}