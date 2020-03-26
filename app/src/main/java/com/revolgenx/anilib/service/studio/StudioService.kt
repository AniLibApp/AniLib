package com.revolgenx.anilib.service.studio

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.field.studio.StudioField
import com.revolgenx.anilib.field.studio.StudioMediaField
import com.revolgenx.anilib.model.studio.StudioMediaModel
import com.revolgenx.anilib.model.studio.StudioModel
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.disposables.CompositeDisposable

interface StudioService {
    val studioInfoLivData: MutableLiveData<Resource<StudioModel>>
    fun getStudioInfo(
        field: StudioField,
        compositeDisposable: CompositeDisposable
    ): MutableLiveData<Resource<StudioModel>>

    fun getStudioMedia(
        field: StudioMediaField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: ((Resource<List<StudioMediaModel>>) -> Unit)
    )
}
