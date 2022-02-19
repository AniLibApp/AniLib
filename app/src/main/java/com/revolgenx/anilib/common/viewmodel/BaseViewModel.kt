package com.revolgenx.anilib.common.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    private var _compositeDisposable = CompositeDisposable()
    protected val compositeDisposable
        get() = if (_compositeDisposable.isDisposed) {
            _compositeDisposable = CompositeDisposable()
            _compositeDisposable
        } else _compositeDisposable


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}