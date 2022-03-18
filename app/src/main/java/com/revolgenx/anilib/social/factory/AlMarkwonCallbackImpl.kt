package com.revolgenx.anilib.social.factory

import android.content.Context
import android.text.Spanned
import com.revolgenx.anilib.common.event.*
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.openLink

class AlMarkwonCallbackImpl(private val context: Context) : AlMarkwonCallback {

    override fun onYoutubeClick(link: String) {
        val uLink = "https://www.youtube.com/watch?v=$link"
        context.openLink(uLink)
    }

    override fun onVideoClick(link: String) {
        context.openLink(link)
    }

    override fun onImageClick(link: String) {
        OpenImageEvent(link).postEvent
    }

    override fun onSpoilerClick(spanned: Spanned) {
        OpenSpoilerContentEvent(spanned).postEvent
    }

    override fun onUserMentionClicked(username: String) {
        OpenUserProfileEvent(null, username).postEvent
    }

    override fun onAniListLinkClick(id: Int, type: String) {
        when (type) {
            "anime", "manga" -> {
                OpenMediaInfoEvent(
                    MediaInfoMeta(
                        id,
                        if (type == "anime") MediaType.ANIME.ordinal else MediaType.MANGA.ordinal,
                        null,
                        null,
                        null,
                        null
                    )
                ).postEvent
            }
            "character" -> {
                OpenCharacterEvent(id).postEvent
            }
        }
    }

}