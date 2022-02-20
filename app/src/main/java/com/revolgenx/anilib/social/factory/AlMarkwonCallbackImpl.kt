package com.revolgenx.anilib.social.factory

import android.content.Context
import android.text.Spanned
import com.revolgenx.anilib.common.event.OpenImageEvent
import com.revolgenx.anilib.common.event.OpenSpoilerContentEvent
import com.revolgenx.anilib.common.event.OpenUserProfileEvent
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback
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

}