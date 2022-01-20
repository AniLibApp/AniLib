package com.revolgenx.anilib.activity.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.saveBasicUserDetail
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.viewmodel.BaseAndroidViewModel
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.infrastructure.repository.network.BaseGraphRepository
import com.revolgenx.anilib.infrastructure.repository.network.converter.toBasicUserModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.user.data.model.UserPrefModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivityViewModel(
    private val app: Application,
    private val repository: BaseGraphRepository
) : BaseAndroidViewModel(app) {
    var genreTagFields: MutableList<TagField> = mutableListOf()
    var tagTagFields: MutableList<TagField> = mutableListOf()
    var streamTagFields: MutableList<TagField> = mutableListOf()

    private val basicUserLiveData = MutableLiveData<UserPrefModel>()

    fun getUserLiveData(): LiveData<UserPrefModel> {
        val disposable = repository.request(BasicUserQuery.builder().id(app.userId()).build())
            .map {
                it.data()?.User()!!.toBasicUserModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userModel ->
                app.saveBasicUserDetail(userModel)
                basicUserLiveData.value = userModel
            }, {
                app.makeToast(R.string.user_detail_fetch_failed)
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