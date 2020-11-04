package com.revolgenx.anilib.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.model.UserPrefModel
import com.revolgenx.anilib.common.preference.saveBasicUserDetail
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toBasicUserModel
import com.revolgenx.anilib.ui.view.makeToast
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivityViewModel(
    private val context: Context,
    private val repository: BaseGraphRepository
) : BaseViewModel() {
    var genreTagFields: MutableList<TagField> = mutableListOf()
    var tagTagFields: MutableList<TagField> = mutableListOf()
    var streamTagFields: MutableList<TagField> = mutableListOf()

    private val basicUserLiveData = MutableLiveData<UserPrefModel>()

    fun getUserLiveData(): LiveData<UserPrefModel> {
        val disposable = repository.request(BasicUserQuery.builder().id(context.userId()).build())
            .map {
                it.data()?.User()!!.toBasicUserModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userModel ->
                context.saveBasicUserDetail(userModel)
                basicUserLiveData.value = userModel
            }, {
                context.makeToast(R.string.user_detail_fetch_failed)
            })
        compositeDisposable.add(disposable)
        return basicUserLiveData
    }

    override fun onCleared() {
        super.onCleared()
        genreTagFields.clear()
        tagTagFields.clear()
        streamTagFields.clear()
    }
}