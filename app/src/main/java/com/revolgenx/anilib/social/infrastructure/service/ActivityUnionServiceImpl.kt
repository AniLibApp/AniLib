package com.revolgenx.anilib.social.infrastructure.service

import com.revolgenx.anilib.ActivityInfoQuery
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ActivityUnionServiceImpl(private val graphRepository: BaseGraphRepository) :
    ActivityUnionService {

    companion object {
        private const val list_activity = "ListActivity"
        private const val text_activity = "TextActivity"
    }

    override fun getActivityUnion(
        field: ActivityUnionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<ActivityUnionModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Page()?.activities()
                    ?.filter { it.__typename() == list_activity || it.__typename() == text_activity }
                    ?.map {
                        when (it.__typename()) {
                            "ListActivity" -> {
                                (it as ActivityUnionQuery.AsListActivity).toModel()
                            }
                            else -> {
                                (it as ActivityUnionQuery.AsTextActivity).toModel()
                            }
                        }
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it ?: emptyList()))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun getActivityInfo(
        field: ActivityInfoField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<ActivityUnionModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data()?.Activity()?.let {
                    when (it.__typename()) {
                        "ListActivity" -> {
                            (it as ActivityInfoQuery.AsListActivity).toModel()
                        }
                        else -> {
                            (it as ActivityInfoQuery.AsTextActivity).toModel()
                        }
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })

        compositeDisposable.add(disposable)
    }

    override fun deleteActivity(
        field: ActivityDeleteField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data()?.DeleteActivity()?.deleted() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }


    override fun saveTextActivity(
        field: SaveTextActivityField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Int>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data()?.SaveTextActivity()?.id() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }

    override fun saveActivityReply(
        field: SaveActivityReplyField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Int>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data()?.SaveActivityReply()?.id() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }
}