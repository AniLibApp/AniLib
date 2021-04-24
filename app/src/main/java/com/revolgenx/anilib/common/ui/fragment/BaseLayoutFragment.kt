package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor

abstract class BaseLayoutFragment<T : ViewBinding> : BaseFragment() {

    @StringRes
    protected open val titleRes: Int? = null
    @StringRes
    protected open val subTitleRes: Int? = null

    protected open val setHomeAsUp = false

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(activity as AppCompatActivity){
            getBaseToolbar()?.let {
                setSupportActionBar(it)
            }

            supportActionBar?.let {actionBar->
                subtitle?.let {sub->
                    actionBar.subtitle = sub
                }

                titleRes?.let { title ->
                    actionBar.setTitle(title)
                }
                subTitleRes?.let {subtitle->
                    actionBar.setSubtitle(subtitle)
                }

                if (setHomeAsUp) {
                    actionBar.setDisplayShowHomeEnabled(true)
                    actionBar.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}