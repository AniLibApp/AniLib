package com.revolgenx.anilib.ui.viewmodel.home.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.revolgenx.anilib.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.media.MediaListEntryService
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class ShowCaseViewModel(
    private val mediaListEntryService: MediaListEntryService
    ):BaseViewModel() {

    fun saveMediaListEntry(model: EntryListEditorMediaModel): LiveData<Resource<EntryListEditorMediaModel>> {
        return liveData {
            emit(Resource.loading<EntryListEditorMediaModel>(null))
            emitSource(
            mediaListEntryService.saveMediaListEntry(
                model,
                compositeDisposable
            ))
        }
    }
}
