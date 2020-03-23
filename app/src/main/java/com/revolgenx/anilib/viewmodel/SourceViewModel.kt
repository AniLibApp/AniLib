package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class SourceViewModel<S, F> : ViewModel() {
    abstract var source: S?
    abstract fun createSource(field: F): S

    protected val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
