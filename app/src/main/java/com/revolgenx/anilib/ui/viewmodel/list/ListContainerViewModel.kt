package com.revolgenx.anilib.ui.viewmodel.list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.field.list.MediaListCountField
import com.revolgenx.anilib.data.model.list.MediaListCountTypeModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class ListContainerViewModel(
    private val mediaListService: MediaListService,
    private val context: Context
) : BaseViewModel() {

    val listCountLiveData: MutableLiveData<Resource<List<MediaListCountTypeModel>>> =
        MutableLiveData()

    private val field: MediaListCountField = MediaListCountField().also {
        it.userId = context.userId()
    }

    fun getListStatusCount() {
        mediaListService.getMediaListCount(field, compositeDisposable) {
            listCountLiveData.value = it
        }
    }
}