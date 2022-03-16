package com.revolgenx.anilib.entry.service

import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.list.data.model.MediaListModel
import io.reactivex.disposables.CompositeDisposable

fun MediaListEntryService.increaseProgress(
    item: MediaListModel,
    compositeDisposable: CompositeDisposable
) {
    val newProgress = (item.progress ?: 0) + 1
    val progressSaveField = SaveMediaListEntryField().also {
        it.id = item.id
        it.progress = newProgress
    }
    saveMediaListEntry(progressSaveField, compositeDisposable) {
        if (it is Resource.Success) {
            item.progress = it.data?.progress ?: newProgress
        }
        item.onDataChanged?.invoke(it)
    }
}