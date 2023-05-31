package com.revolgenx.anilib.social.factory

import android.text.Spanned
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.activity.MainActivityViewModel
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.type.MediaType

class AlMarkdownCallbackImpl : AlMarkdownCallback {
    override fun onYoutubeClick(link: String) {

    }

    override fun onVideoClick(link: String) {
    }

    override fun onImageClick(link: String) {
        OpenImageEvent(link).postEvent
    }

    override fun onSpoilerClick(spanned: Spanned) {
        OpenSpoilerEvent(spanned).postEvent
    }

    override fun onUserMentionClicked(username: String) {
        OpenUserScreenEvent(username = username).postEvent
    }

    override fun onAniListLinkClick(id: Int, type: String) {
        when (type) {
            "anime", "manga" -> {
                OpenMediaScreenEvent(
                    id,
                    if (type == "anime") MediaType.ANIME else MediaType.MANGA,
                ).postEvent
            }

            "character" -> {
                OpenCharacterScreenEvent(id).postEvent
            }
        }
    }
}
