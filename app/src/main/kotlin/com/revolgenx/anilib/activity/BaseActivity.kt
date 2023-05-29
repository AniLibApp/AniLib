package com.revolgenx.anilib.activity

import android.text.Spanned
import androidx.activity.ComponentActivity
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.social.factory.AlMarkdownCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity : ComponentActivity(), AlMarkdownCallback {
    protected val viewModel by viewModel<MainActivityViewModel>()
    protected var navigator: Navigator? = null


    override fun onYoutubeClick(link: String) {
    }

    override fun onVideoClick(link: String) {
    }

    override fun onImageClick(link: String) {
        viewModel.openSpoilerBottomSheet.value = false
        navigator?.imageViewerScreen(link)
    }

    override fun onSpoilerClick(spanned: Spanned) {
        with(viewModel) {
            spoilerSpanned = spanned
            openSpoilerBottomSheet.value = true
        }
    }

    override fun onUserMentionClicked(username: String) {

    }

    override fun onAniListLinkClick(id: Int, type: String) {
    }
}