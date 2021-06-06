package com.revolgenx.anilib.ui.presenter.list.binding

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.infrastructure.event.OpenMediaInfoEvent
import com.revolgenx.anilib.infrastructure.event.OpenMediaListEditorEvent
import com.revolgenx.anilib.ui.view.makeToast

object ListBindingHelper {
    fun openMediaBrowse(context: Context, item: MediaListModel){
        OpenMediaInfoEvent(
            MediaInfoMeta(
                item.mediaId,
                item.type!!,
                item.title!!.userPreferred,
                item.coverImage!!.image(context),
                item.coverImage!!.largeImage,
                item.bannerImage
            )
        ).postEvent
    }

    fun openMediaListEditor(context: Context, item: MediaListModel){
        if (context.loggedIn()) {
            OpenMediaListEditorEvent(
                ListEditorMeta(
                    item.mediaId,
                    item.type!!,
                    item.title!!.userPreferred,
                    item.coverImage!!.image(context),
                    item.bannerImage
                )
            ).postEvent
        } else {
            context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
        }
    }

}