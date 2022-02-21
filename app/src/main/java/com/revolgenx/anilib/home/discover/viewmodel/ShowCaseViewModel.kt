package com.revolgenx.anilib.home.discover.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.entry.data.field.SaveMediaListEntryField
import com.revolgenx.anilib.entry.service.MediaListEntryService
import com.revolgenx.anilib.list.data.model.MediaListModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ShowCaseViewModel(private val entryService: MediaListEntryService) : BaseViewModel() {
    fun changeListEntryStatus(mediaId: Int, status: Int): LiveData<Resource<MediaListModel>> {
        return liveData {
            emit(Resource.loading(null))
            val saveEntryField = SaveMediaListEntryField().also {
                it.mediaId = mediaId
                it.status = status
            }
            emit(suspendCoroutine { cont ->
                entryService.saveMediaListEntry(
                    saveEntryField,
                    compositeDisposable
                ) {
                    cont.resume(it)
                }
            })
        }
    }
}
