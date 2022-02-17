package com.revolgenx.anilib.user.fragment

import androidx.viewbinding.ViewBinding
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.user.viewmodel.UserContainerSharedVM
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseUserFragment<VB:ViewBinding>: BaseLayoutFragment<VB>() {
    protected val sharedViewModel by viewModel<UserContainerSharedVM>(owner = {
        ViewModelOwner.from(
            this.parentFragment ?: this,
            this.parentFragment
        )
    })
}