package com.revolgenx.anilib.social.markwon

import android.text.Spanned

interface AlMarkwonCallback {
    fun onYoutubeClick(link:String)
    fun onVideoClick(link: String)
    fun onImageClick(link:String)
    fun onSpoilerClick(spanned: Spanned)
    fun onUserMentionClicked(username:String)
}