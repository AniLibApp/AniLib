package com.revolgenx.anilib.social.factory

import android.content.Context
import android.text.Spanned
import com.revolgenx.anilib.data.meta.ImageMeta
import com.revolgenx.anilib.infrastructure.event.ImageClickedEvent
import com.revolgenx.anilib.infrastructure.event.OpenSpoilerContentEvent
import com.revolgenx.anilib.infrastructure.event.OpenUserProfileEvent
import com.revolgenx.anilib.social.markwon.AlMarkwonCallback
import com.revolgenx.anilib.social.ui.bottomsheet.SpoilerBottomSheet
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink

class AlMarkwonCallbackImpl(private val context: Context) : AlMarkwonCallback {

    override fun onYoutubeClick(link: String) {
        val uLink = "https://www.youtube.com/watch?v=$link"
        context.makeToast(msg = uLink)
        context.openLink(uLink)
    }

    override fun onVideoClick(link: String) {
        context.openLink(link)
    }

    override fun onImageClick(link: String) {
        ImageClickedEvent(ImageMeta(link)).postEvent
    }

    override fun onSpoilerClick(spanned: Spanned) {
        OpenSpoilerContentEvent(spanned).postEvent
    }

    override fun onUserMentionClicked(username: String) {
        OpenUserProfileEvent(null, username).postEvent
    }

}