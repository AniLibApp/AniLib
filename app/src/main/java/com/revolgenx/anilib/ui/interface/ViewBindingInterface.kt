package com.revolgenx.anilib.ui.`interface`

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface ViewBindingInterface<T : ViewBinding> {

    var _binding: T?
    val binding: T get() = _binding!!

    fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null): T
}