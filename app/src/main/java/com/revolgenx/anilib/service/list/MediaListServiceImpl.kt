package com.revolgenx.anilib.service.list

import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.TitleModel
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.util.ERROR
import com.revolgenx.anilib.repository.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MediaListServiceImpl(private val graphRepository: BaseGraphRepository) : MediaListService {
    override fun getMediaList(
        field: MediaListField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<MediaListModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.MediaListCollection()?.lists()?.firstOrNull()?.entries()?.filter { if (field.canShowAdult) true else it.media()?.isAdult == false }?.map {
                    MediaListModel().also { model ->
                        model.mediaListId = it.id()
                        model.progress = it.progress()?.toString()
                        model.score = it.score()
                        model.scoreFormat = it.user()?.mediaListOptions()?.scoreFormat()?.ordinal
                        it.media()?.let { media ->
                            model.mediaId = media.id()
                            model.episodes = media.episodes()?.toString()
                            model.chapters = media.chapters()?.toString()
                            model.title = media.title()?.userPreferred()?.let {
                                TitleModel().apply { userPreferred = it }
                            }
                            model.coverImage = media.coverImage()?.large()?.let {
                                CoverImageModel().apply { large = it }
                            }
                            model.type = field.type
                            model.genres = media.genres()
                            model.format = media.format()?.ordinal
                            model.status = media.status()?.ordinal
                        }
                    }
                } ?: emptyList()
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.w(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }
}