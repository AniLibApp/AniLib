package com.revolgenx.anilib.service

import com.apollographql.apollo.api.Query
import com.revolgenx.anilib.MediaSearchQuery
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.TitleModel
import com.revolgenx.anilib.model.search.AnimeAdvanceSearchModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class AdvanceSearchServiceImpl(private val baseGraphRepository: BaseGraphRepository) :
    AdvanceSearchService {

    override fun search(
        field: BaseAdvanceSearchField,
        compositeDisposable: CompositeDisposable,
        callback: (Resource<List<BaseModel>>) -> Unit
    ) {
        val disposable = baseGraphRepository.request(field.toQueryOrMutation() as Query<*, *, *>)
            .map {
                val data = it.data()
                if ((data is MediaSearchQuery.Data)) {
                    data.Page()?.media()?.map {
                        AnimeAdvanceSearchModel().also { model ->
                            model.baseId = it.id()
                            model.mediaId = it.id()
                            model.title = it.title()?.let {
                                TitleModel().also { ti ->
                                    ti.romaji = it.romaji()
                                    ti.english = it.english()
                                    ti.native = it.native_()
                                    ti.userPreferred = it.userPreferred()
                                }
                            }
                            model.coverImage = it.coverImage()?.let {
                                CoverImageModel().also { img ->
                                    img.extraLarge = it.extraLarge()
                                    img.medium = it.medium()
                                    img.large = it.large()
                                }
                            }
                            model.bannerImage = it.bannerImage() ?: model.coverImage?.largeImage
                            model.type = it.type()?.ordinal
                            model.format = it.format()?.ordinal
                            model.status = it.status()?.ordinal
                            model.seasonYear = it.seasonYear()
                            model.season = it.season()?.ordinal
                            model.averageScore = it.averageScore()
                        }
                    }
                } else {
                    emptyList<BaseModel>()
                }
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.w(it)
                callback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}