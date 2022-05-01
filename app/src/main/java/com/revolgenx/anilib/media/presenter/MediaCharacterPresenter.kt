package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.databinding.AnimeCharacterPresenterLayoutBinding
import com.revolgenx.anilib.databinding.MangaCharacterPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenCharacterEvent
import com.revolgenx.anilib.common.event.OpenStaffEvent
import com.revolgenx.anilib.common.preference.mediaCharacterDisplayModePref
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.constant.MediaCharacterDisplayMode
import com.revolgenx.anilib.databinding.AnimeCharacterNormalPresenterLayoutBinding
import com.revolgenx.anilib.databinding.MangaCharacterNormalPresenterLayoutBinding

class MediaCharacterPresenter(context: Context) :
    BasePresenter<ViewBinding, CharacterEdgeModel>(context) {

    private val displayMode = MediaCharacterDisplayMode.values()[mediaCharacterDisplayModePref]

    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    private val characterRoles by lazy {
        context.resources.getStringArray(R.array.character_role)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (elementType) {
            1 -> {
                when (displayMode) {
                    MediaCharacterDisplayMode.COMPACT -> {
                        MangaCharacterPresenterLayoutBinding.inflate(inflater, parent, false)
                    }
                    MediaCharacterDisplayMode.NORMAL -> {
                        MangaCharacterNormalPresenterLayoutBinding.inflate(inflater, parent, false)
                    }
                }
            }
            else -> {
                when (displayMode) {
                    MediaCharacterDisplayMode.COMPACT -> {
                        AnimeCharacterPresenterLayoutBinding.inflate(inflater, parent, false)
                    }
                    MediaCharacterDisplayMode.NORMAL -> {
                        AnimeCharacterNormalPresenterLayoutBinding.inflate(inflater, parent, false)
                    }
                }
            }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<CharacterEdgeModel>) {
        super.onBind(page, holder, element)
        val edge = element.data ?: return
        val item = edge.node ?: return
        val binding = holder.getBinding() ?: return

        var characterIv: SimpleDraweeView? = null
        var characterNameTv: TextView? = null
        var characterRoleTv: TextView? = null
        var staffIv: SimpleDraweeView? = null
        var staffNameTv: TextView? = null
        var staffLanguageTv: TextView? = null


        when (binding) {
            is AnimeCharacterNormalPresenterLayoutBinding -> {
                characterIv = binding.mediaCharacterIv
                characterNameTv = binding.mediaCharacterNameTv
                characterRoleTv = binding.mediaCharacterRoleTv
                staffIv = binding.mediaStaffIv
                staffNameTv = binding.mediaStaffNameTv
                staffLanguageTv = binding.mediaStaffLanguageTv
            }
            is MangaCharacterNormalPresenterLayoutBinding -> {
                characterIv = binding.mediaCharacterIv
                characterNameTv = binding.mediaCharacterNameTv
                characterRoleTv = binding.mediaCharacterRoleTv
            }
            is AnimeCharacterPresenterLayoutBinding -> {
                characterIv = binding.mediaCharacterIv
                characterNameTv = binding.mediaCharacterNameTv
                characterRoleTv = binding.mediaCharacterRoleTv
                staffIv = binding.mediaStaffIv
                staffNameTv = binding.mediaStaffNameTv
                staffLanguageTv = binding.mediaStaffLanguageTv
            }
            is MangaCharacterPresenterLayoutBinding -> {
                characterIv = binding.mediaCharacterIv
                characterNameTv = binding.mediaCharacterNameTv
                characterRoleTv = binding.mediaCharacterRoleTv
            }
        }

        characterIv?.setImageURI(item.image?.image)
        characterNameTv?.text = item.name?.full
        characterRoleTv?.text = edge.role?.let { characterRoles[it] }

        characterIv?.setOnClickListener {
            OpenCharacterEvent(item.id).postEvent
        }

        edge.voiceActors?.firstOrNull()?.let { actorModel ->
            staffIv?.setImageURI(actorModel.image?.image)
            staffNameTv?.text = actorModel.name?.full
            staffLanguageTv?.text = actorModel.languageV2
            staffIv?.setOnClickListener {
                OpenStaffEvent(actorModel.id).postEvent
            }
        }

    }
}
