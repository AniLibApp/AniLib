package com.revolgenx.anilib.social.factory

import android.text.Spanned
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenLinkEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenStaffScreenEvent
import com.revolgenx.anilib.common.data.event.OpenStudioScreenEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.type.MediaType

class MarkdownCallbackImpl : MarkdownCallback {
    override fun onYoutubeClick(link: String) {
        OpenLinkEvent(link).postEvent
    }

    override fun onVideoClick(link: String) {
        OpenLinkEvent(link).postEvent
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

            "staff" -> {
                OpenStaffScreenEvent(id).postEvent
            }

            "studio" -> {
                OpenStudioScreenEvent(id).postEvent
            }
        }
    }
}
