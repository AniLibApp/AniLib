package com.revolgenx.anilib.social.factory

import android.content.Context
import android.text.Spanned
import com.revolgenx.anilib.data.meta.ImageMeta
import com.revolgenx.anilib.infrastructure.event.ImageClickedEvent
import com.revolgenx.anilib.infrastructure.event.UserBrowseEvent
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback
import com.revolgenx.anilib.util.openLink

class AlMarkwonCallbackImpl(private val context: Context):AlMarkwonCallback {

    override fun onYoutubeClick(link: String) {
        context.openLink(link)
    }

    override fun onVideoClick(link: String) {
        context.openLink(link)
    }

    override fun onImageClick(link: String) {
        ImageClickedEvent(ImageMeta(link)).postEvent
    }

    override fun onSpoilerClick(spanned: Spanned) {

    }

    override fun onUserMentionClicked(username: String) {
        UserBrowseEvent(null, username).postEvent
    }

}