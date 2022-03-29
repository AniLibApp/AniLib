package com.revolgenx.anilib.activity.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.saveBasicUserDetail
import com.revolgenx.anilib.common.viewmodel.BaseAndroidViewModel
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.repository.network.BaseGraphRepository
import com.revolgenx.anilib.common.repository.network.converter.toBasicUserModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.user.data.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers

class MainActivityViewModel(
    private val app: Application,
    private val repository: BaseGraphRepository
) : BaseAndroidViewModel(app) {
    private val basicUserLiveData = MutableLiveData<UserModel>()

    fun getUserLiveData(): LiveData<UserModel> {
        val disposable = repository.request(BasicUserQuery(id = Optional.presentIfNotNull(UserPreference.userId)))
            .map {
                it.data?.user!!.toBasicUserModel()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userModel ->
                saveBasicUserDetail(userModel)
                basicUserLiveData.value = userModel
            }, {
                app.makeToast(R.string.user_detail_fetch_failed)
            })
        compositeDisposable.add(disposable)
        return basicUserLiveData
    }
}