package com.revolgenx.anilib.media.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.model.MediaSocialFollowingModel
import com.revolgenx.anilib.databinding.MediaSocialFollowingPresenterLayoutBinding
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.common.presenter.BasePresenter

class MediaSocialFollowingPresenter(context: Context) :
    BasePresenter<MediaSocialFollowingPresenterLayoutBinding, MediaSocialFollowingModel>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    private val animeListStatus by lazy {
        context.resources.getStringArray(R.array.anime_list_status)
    }

    private val mangaListStatus by lazy {
        context.resources.getStringArray(R.array.manga_list_status)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MediaSocialFollowingPresenterLayoutBinding {
        return MediaSocialFollowingPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<MediaSocialFollowingModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.getBinding()?.apply {
            item.user?.let { user ->
                userAvatarIv.setImageURI(user.avatar?.image)
                userNameTv.text = user.name
                user.mediaListOptions?.let {

                }
            }
            userListStatusTv.text = if (item.type == MediaType.MANGA.ordinal) {
                mangaListStatus[item.status!!]
            } else {
                animeListStatus[item.status!!]
            }

            var userScore = item.score?.toInt()?.toString()
            val scoreFormat = when (ScoreFormat.values()[item.user?.mediaListOptions?.scoreFormat ?: 0]) {
                ScoreFormat.POINT_100 -> "100"
                ScoreFormat.POINT_10_DECIMAL -> {
                    userScore = item.score?.toString()
                    "10.0"
                }
                ScoreFormat.POINT_10 -> "10"
                ScoreFormat.POINT_5 -> "5"
                ScoreFormat.POINT_3 -> "3"
                else -> "?"
            }

            userListScoreTv.text = "$userScore/$scoreFormat"

            root.setOnClickListener {
                OpenUserProfileEvent(item.user?.id).postEvent
            }
        }
    }


}