package com.revolgenx.anilib.app.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingField
import com.revolgenx.anilib.app.setting.data.field.MediaListSettingMutationField
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.service.SettingService
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.common.repository.util.Resource

class MediaListSettingVM(private val settingService: SettingService) : BaseViewModel() {
    val field = MediaListSettingField()
    val saveField = MediaListSettingMutationField()

    val listLiveData = MutableLiveData<Resource<MediaListOptionModel>>()
    val saveListLiveData = MutableLiveData<Resource<Boolean>>()

    fun getMediaListSetting() {
        listLiveData.value = Resource.loading(null)
        settingService.getMediaListSetting(field, compositeDisposable) {
            it.data?.toSaveField()
            listLiveData.value = it
        }
    }

    fun saveMediaListSetting(){
        saveListLiveData.value = Resource.loading(null)
        settingService.saveMediaListSetting(saveField, compositeDisposable){
            saveListLiveData.value = it
        }
    }

    private fun MediaListOptionModel.toSaveField() {
        saveField.advancedScoring = animeList?.advancedScoring ?: mutableListOf()
        saveField.animeCustomLists = animeList?.customLists?.toMutableList() ?: mutableListOf()
        saveField.mangaCustomLists = mangaList?.customLists?.toMutableList() ?: mutableListOf()
        saveField.rowOrder = rowOrder
        saveField.scoreFormat = scoreFormat
        saveField.advancedScoringEnabled = animeList?.advancedScoringEnabled == true
        saveField.splitCompletedAnimeListByFormat = animeList?.splitCompletedSectionByFormat
        saveField.splitCompletedMangaListByFormat = mangaList?.splitCompletedSectionByFormat
    }
}