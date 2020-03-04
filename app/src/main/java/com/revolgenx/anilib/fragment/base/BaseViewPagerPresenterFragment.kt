package com.revolgenx.anilib.fragment.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseViewPagerPresenterFragment<S : Any> : BasePresenterFragment<S>() {
    protected var alreadyVisible: Boolean = false
    private val visibleTag = "already_visible_tag"


    open var title: String? = null
    open var subTitle: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.let { bar ->
            title?.let { bar.title = it }
            subTitle?.let { bar.subtitle = it }
        }

        if (savedInstanceState != null) {
            alreadyVisible = savedInstanceState.getBoolean(visibleTag)
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!alreadyVisible && isVisibleToUser) {
            alreadyVisible = true
            requestOnVisible()
        }
    }

    /*called once fragment is visible*/
    abstract fun requestOnVisible()

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(visibleTag, alreadyVisible)
        super.onSaveInstanceState(outState)
    }
}