package com.revolgenx.anilib.ui.presenter.list.binding

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.ui.view.makeToast

object ListBindingHelper {
    fun openMediaBrowse(context: Context, item: MediaListModel, view: View){
        BrowseMediaEvent(
            MediaBrowserMeta(
                item.mediaId,
                item.type!!,
                item.title!!.userPreferred,
                item.coverImage!!.image(context),
                item.coverImage!!.largeImage,
                item.bannerImage
            ), view
        ).postEvent
    }

    fun openMediaListEditor(context: Context, item: MediaListModel, view: View, openInWithSupportFragment:Boolean, @IdRes containerLayout:Int){
        if (context.loggedIn()) {
            ListEditorEvent(
                ListEditorMeta(
                    item.mediaId,
                    item.type!!,
                    item.title!!.userPreferred,
                    item.coverImage!!.image(context),
                    item.bannerImage,
                    !openInWithSupportFragment
                ), view,
                openInWithSupportFragment,
                containerLayout
            ).postEvent
        } else {
            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }

}