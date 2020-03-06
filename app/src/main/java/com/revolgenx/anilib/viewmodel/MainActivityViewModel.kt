package com.revolgenx.anilib.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.BasicUserModel
import com.revolgenx.anilib.preference.saveBasicUserDetail
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.toBasicUserModel
import com.revolgenx.anilib.util.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MainActivityViewModel(
    private val context: Context,
    private val repository: BaseGraphRepository
) : ViewModel() {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    private val basicUserLiveData = MutableLiveData<BasicUserModel>()

    fun getUserLiveData(): MutableLiveData<BasicUserModel> {
        val disposable = repository.request(BasicUserQuery.builder().id(context.userId()).build())
            .map {
                it.data()?.User()!!.toBasicUserModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({userModel->
                context.saveBasicUserDetail(userModel)
                basicUserLiveData.value = userModel
            }, {
                Timber.e(it)
                context.makeToast(context.getString(R.string.user_detail_fetch_failed))
            })
        compositeDisposable.add(disposable)
        return basicUserLiveData
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}