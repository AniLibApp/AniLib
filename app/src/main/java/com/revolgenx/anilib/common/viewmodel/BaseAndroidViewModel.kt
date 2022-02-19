package com.revolgenx.anilib.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseAndroidViewModel(app: Application) : AndroidViewModel(app) {
    private var _compositeDisposable = CompositeDisposable()
    protected val compositeDisposable
        get() = if (_compositeDisposable.isDisposed) {
            _compositeDisposable = CompositeDisposable()
            _compositeDisposable
        } else _compositeDisposable


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}