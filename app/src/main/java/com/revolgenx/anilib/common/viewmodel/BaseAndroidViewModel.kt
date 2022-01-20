package com.revolgenx.anilib.common.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseAndroidViewModel(app: Application): AndroidViewModel(app){
    protected val compositeDisposable = CompositeDisposable()
}