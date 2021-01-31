package com.revolgenx.anilib.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Presenter

abstract class BasePresenter<T : ViewBinding, M : Any>(context: Context) : Presenter<M>(context) {
    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        val binding = bindView(getLayoutInflater(), parent, elementType)
        return Holder(binding.root).also {
            it[Constant.PRESENTER_BINDING_KEY] = binding
        }
    }

    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null, elementType: Int): T
    protected fun Holder.getBinding():T? = this[Constant.PRESENTER_BINDING_KEY]

}