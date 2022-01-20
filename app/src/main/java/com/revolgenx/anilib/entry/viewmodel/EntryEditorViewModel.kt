package com.revolgenx.anilib.entry.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.entry.data.model.EntryListEditorMediaModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.favourite.FavouriteService
import com.revolgenx.anilib.media.service.MediaInfoService
import com.revolgenx.anilib.entry.service.MediaEntryService
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.media.data.model.MediaInfoModel

class EntryEditorViewModel(
    private val mediaListEntryService: MediaEntryService,
    private val mediaBrowseService: MediaInfoService,
    private val favouriteService: FavouriteService,
    private val toggleService: ToggleService
) : BaseViewModel() {
    val queryMediaListEntryLiveData = MutableLiveData<Resource<EntryListEditorMediaModel>>()
    val deleteMediaListEntryLiveData = MutableLiveData<Resource<Boolean>>()
    val saveMediaListEntryLiveData = MutableLiveData<Resource<EntryListEditorMediaModel>>()
    val toggleFavMediaLiveData = MutableLiveData<Resource<Boolean>>()
    val mediaLiveData = MutableLiveData<Resource<MediaInfoModel>>()
    val isFavLiveData = MutableLiveData<Resource<Boolean>>()

    var apiModelEntry: EntryListEditorMediaModel = EntryListEditorMediaModel()

    fun queryMediaListEntry(mediaId: Int?) {
        queryMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.queryMediaListEntry(mediaId, compositeDisposable) {
            queryMediaListEntryLiveData.value = it
        }
    }

    fun saveMediaListEntry(model: EntryListEditorMediaModel) {
        saveMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.saveMediaListEntry(model,compositeDisposable){
            saveMediaListEntryLiveData.value = it
        }
    }

    fun deleteMediaListEntry(listId: Int) {
        deleteMediaListEntryLiveData.value = Resource.loading(null)
        mediaListEntryService.deleteMediaListEntry(listId,compositeDisposable){
            deleteMediaListEntryLiveData.value = it
        }
    }

    fun toggleMediaFavourite(toggleFavouriteField: ToggleFavouriteField) {
        toggleFavMediaLiveData.value = Resource.loading(null)
        toggleService.toggleFavourite(toggleFavouriteField,compositeDisposable){
            toggleFavMediaLiveData.value = it
        }
    }

    fun getMediaInfo(mediaId: Int?) {
        mediaBrowseService.getSimpleMedia(mediaId, compositeDisposable) {
            mediaLiveData.value = it
        }
    }

    fun isFavouriteQuery(mediaId: Int?){
        favouriteService.isFavourite(mediaId, compositeDisposable){
            isFavLiveData.value = it
        }
    }

}