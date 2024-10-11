package com.revolgenx.anilib.social.factory

import android.text.Spanned

interface MarkdownCallback {
    fun onYoutubeClick(link: String)
    fun onVideoClick(link: String)
    fun onImageClick(link: String)
    fun onSpoilerClick(spanned: Spanned)
    fun onUserMentionClicked(username: String)
    fun onAniListLinkClick(id: Int, type: String)
}