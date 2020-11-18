package com.revolgenx.anilib.ui.viewmodel

abstract class SourceViewModel<S, F> : BaseViewModel() {
    var source: S? = null
    abstract var field: F
    abstract fun createSource(): S
}
