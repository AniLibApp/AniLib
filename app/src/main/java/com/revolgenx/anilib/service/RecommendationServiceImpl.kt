package com.revolgenx.anilib.service

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.model.MediaRecommendationModel
import com.revolgenx.anilib.model.field.overview.MediaRecommendationField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import com.revolgenx.anilib.util.pmap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class RecommendationServiceImpl(graphRepository: BaseGraphRepository) :
    RecommendationService(graphRepository) {

    override fun mediaRecommendation(
        field: MediaRecommendationField,
        compositeDisposable: CompositeDisposable?
    ): MutableLiveData<Resource<List<MediaRecommendationModel>>> {
        val disposable = graphRepository.request(field.toQuery())
            .map {
                runBlocking {
                    it.data()?.Media()!!.recommendations()!!.nodes()!!.pmap {
                        MediaRecommendationModel()
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Timber.e(it)
                mediaRecommendationLiveData.value = Resource.error(it.message ?: ERROR, null)
            })
        compositeDisposable?.add(disposable)
        return mediaRecommendationLiveData
    }

}