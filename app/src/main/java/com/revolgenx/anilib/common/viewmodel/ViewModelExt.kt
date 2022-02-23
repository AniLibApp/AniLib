package com.revolgenx.anilib.common.viewmodel

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ViewModelOwnerDefinition


fun Fragment.getViewModelOwner() : ViewModelOwnerDefinition = {
    ViewModelOwner.from(
        this.parentFragment ?: this,
        this.parentFragment
    )
}