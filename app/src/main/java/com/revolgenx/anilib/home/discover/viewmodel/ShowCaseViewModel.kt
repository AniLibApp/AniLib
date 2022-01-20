package com.revolgenx.anilib.home.discover.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.entry.service.MediaEntryService
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ShowCaseViewModel(
    private val mediaListEntryService: MediaEntryService
    ): BaseViewModel() {

    fun saveMediaListEntry(model: EntryListEditorMediaModel): LiveData<Resource<EntryListEditorMediaModel>> {
        return liveData {
            emit(Resource.loading<EntryListEditorMediaModel>(null))
            emit(suspendCoroutine { cont->
                mediaListEntryService.saveMediaListEntry(
                    model,
                    compositeDisposable
                ){
                    cont.resume(it)
                }
            })
        }
    }
}
