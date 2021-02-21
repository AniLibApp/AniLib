package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseLayoutFragment<T : ViewBinding> : BaseFragment() {
    protected open var titleRes: Int? = null
    protected open var setHomeAsUp = false


    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    var visibleToUser = false

    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null): T


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindView(inflater, container)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.let {
            titleRes?.let { title ->
                it.setTitle(title)
            }
            if (setHomeAsUp) {
                it.setDisplayShowHomeEnabled(true)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}