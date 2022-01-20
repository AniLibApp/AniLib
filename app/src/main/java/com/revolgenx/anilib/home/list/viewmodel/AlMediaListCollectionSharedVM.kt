package com.revolgenx.anilib.home.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlMediaListCollectionSharedVM : ViewModel() {
    var toggleSearchView = MutableLiveData<Int>()
    var showListGroupSelector = MutableLiveData<Int>()
}