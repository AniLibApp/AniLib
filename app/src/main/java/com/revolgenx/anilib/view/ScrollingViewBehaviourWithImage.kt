package com.revolgenx.anilib.view

import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import timber.log.Timber

class ScrollingViewBehaviourWithImage :AppBarLayout.ScrollingViewBehavior(){
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        if(dependency is LinearLayout){
            Timber.d("scrolling behav%s", dependency.layoutParams.height)
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }
}