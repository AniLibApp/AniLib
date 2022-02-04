package com.revolgenx.anilib.social.infrastructure.service

import com.revolgenx.anilib.ActivityInfoQuery
import com.revolgenx.anilib.ActivityUnionQuery
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toModel
import com.revolgenx.anilib.infrastructure.repository.util.ERROR
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.*
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class ActivityUnionServiceImpl(private val graphRepository: BaseGraphRepository) :
    ActivityUnionService {

    companion object {
        private const val LIST_ACTIVITY = "ListActivity"
        private const val MESSAGE_ACTIVITY = "MessageActivity"
    }

    override fun getActivityUnion(
        field: ActivityUnionField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<List<ActivityUnionModel>>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.page?.activities?.mapNotNull {
                    when (it?.__typename) {
                        LIST_ACTIVITY -> {
                            it.onListActivity?.toModel()
                        }
                        MESSAGE_ACTIVITY -> {
                            it.onMessageActivity?.toModel()
                        }
                        else -> {
                            it?.onTextActivity?.toModel()
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
                it.data?.activity?.let {
                    when (it.__typename) {
                        LIST_ACTIVITY -> {
                            it.onListActivity?.toModel()
                        }
                        MESSAGE_ACTIVITY -> {
                            it.onMessageActivity?.toModel()
                        }
                        else -> {
                            it.onTextActivity?.toModel()
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
            .map { it.data?.deleteActivity?.deleted }
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
            .map { it.data?.saveTextActivity?.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                resourceCallback.invoke(Resource.success(it))
            }, {
                Timber.e(it)
                resourceCallback.invoke(Resource.error(it.message ?: ERROR, null, it))
            })
        compositeDisposable.add(disposable)
    }

    override fun saveMessageActivity(
        field: SaveMessageActivityField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Int>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data?.saveMessageActivity?.id }
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
        resourceCallback: (Resource<ActivityReplyModel>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map {
                it.data?.saveActivityReply?.let {
                    ActivityReplyModel().also { model ->
                        model.id = it.id
                        model.activityId = it.activityId
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

    override fun deleteActivityReply(
        field: DeleteActivityReplyField,
        compositeDisposable: CompositeDisposable,
        resourceCallback: (Resource<Boolean>) -> Unit
    ) {
        val disposable = graphRepository.request(field.toQueryOrMutation())
            .map { it.data?.deleteActivityReply?.deleted }
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