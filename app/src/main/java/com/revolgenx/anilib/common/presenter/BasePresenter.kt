package com.revolgenx.anilib.common.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Presenter

abstract class BasePresenter<T : ViewBinding, M : Any>(context: Context) : Presenter<M>(context) {

    companion object{
        const val PRESENTER_BINDING_KEY = "PRESENTER_BINDING_KEY"
        const val PRESENTER_HOLDER_ITEM_KEY = "PRESENTER_HOLDER_ITEM_KEY"
    }

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        val binding = bindView(getLayoutInflater(), parent, elementType)
        return Holder(binding.root).also {
            it[PRESENTER_BINDING_KEY] = binding
        }
    }

    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null, elementType: Int): T
    protected fun Holder.getBinding():T? = this[PRESENTER_BINDING_KEY]

}