package com.revolgenx.anilib.social.factory

import android.text.Spanned
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.activity.MainActivityViewModel

class AlMarkdownCallbackImpl : AlMarkdownCallback {
    var navigator: Navigator? = null
    var viewModel: MainActivityViewModel? = null
    override fun onYoutubeClick(link: String) {

    }

    override fun onVideoClick(link: String) {
    }

    override fun onImageClick(link: String) {
    }

    override fun onSpoilerClick(spanned: Spanned) {
        viewModel?.let {
            it.spoilerSpanned = spanned
            it.openSpoilerBottomSheet.value = true
        }
    }

    override fun onUserMentionClicked(username: String) {
    }

    override fun onAniListLinkClick(id: Int, type: String) {
    }
}
