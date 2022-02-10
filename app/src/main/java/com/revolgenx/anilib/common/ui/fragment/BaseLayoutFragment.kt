package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor

abstract class BaseLayoutFragment<T : ViewBinding> : BaseFragment() {

    @StringRes
    protected open val titleRes: Int? = null

    @StringRes
    protected open val subTitleRes: Int? = null

    protected open val setHomeAsUp = false

    @MenuRes
    protected open val menuRes: Int? = null

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    fun isBindingEmpty() = _binding == null

    var visibleToUser = false

    abstract fun bindView(inflater: LayoutInflater, parent: ViewGroup? = null): T

    protected open fun getBaseToolbar(): Toolbar? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindView(inflater, container)
        binding.root.setBackgroundColor(dynamicBackgroundColor)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateToolbar()
    }


    protected open fun updateToolbar() {
        getBaseToolbar()?.let { t ->
            t.menu.clear()
            menuRes?.let {
                t.inflateMenu(it)
                onToolbarInflated()
            }

            subtitle?.let {
                t.subtitle = it
            }

            titleRes?.let {
                t.setTitle(it)
            }
            subTitleRes?.let {
                t.setSubtitle(it)
            }

            if (setHomeAsUp) {
                t.setNavigationIcon(R.drawable.ic_back)
                t.setNavigationOnClickListener {
                    popBackStack()
                }
            }

            t.setOnMenuItemClickListener {
                onToolbarMenuSelected(it)
            }
        }
    }


    protected open fun onToolbarMenuSelected(item: MenuItem): Boolean = false
    protected open fun onToolbarInflated(){}


    protected fun popBackStack() = requireActivity().supportFragmentManager.popBackStack()
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}