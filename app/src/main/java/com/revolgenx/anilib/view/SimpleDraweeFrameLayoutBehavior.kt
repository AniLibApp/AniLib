package com.revolgenx.anilib.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.ViewGroupUtils
import com.google.android.material.appbar.AppBarLayout

class SimpleDraweeFrameLayoutBehavior(context: Context, attributeSet: AttributeSet?) :
    CoordinatorLayout.Behavior<ViewGroup?>(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    private var viewPropertyAnimator: ViewPropertyAnimator? = null
    private var animationStateHolder = true
    private var mTmpRect: Rect? = null


    fun layoutDependsOn(
        parent: CoordinatorLayout?,
        child: ViewGroup?,
        dependency: View?
    ): Boolean { // check that our dependency is the AppBarLayout
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: ViewGroup,
        dependency: View
    ): Boolean {
        return if (dependency is AppBarLayout) {
            updateFrameLayoutVisibility(parent, dependency, child)
        } else false
    }

    @SuppressLint("RestrictedApi")
    private fun updateFrameLayoutVisibility(
        parent: CoordinatorLayout,
        appBarLayout: AppBarLayout, child: ViewGroup
    ): Boolean {
        val lp =
            child.layoutParams as CoordinatorLayout.LayoutParams
        if (lp.anchorId != appBarLayout.id) {
            return false
        }
        if (mTmpRect == null) {
            mTmpRect = Rect()
        }
        // First, let's get the visible rect of the dependency
        val rect: Rect = mTmpRect!!
        ViewGroupUtils.getDescendantRect(
            parent,
            appBarLayout,
            rect
        )
        if (rect.bottom <= appBarLayout.minimumHeightForVisibleOverlappingContent) {
            animate(child)
        } else
            animate(child, true)

        return true
    }

    private fun animate(child: ViewGroup, visible: Boolean = false) {
        if (animationStateHolder == visible) {
            return
        }
        animationStateHolder = visible

        if (visible) {
            child.visibility = View.VISIBLE
            viewPropertyAnimator = child.animate().alpha(1f)
                .setDuration(200)
                .withEndAction {
                    child.alpha = 1f
                }
        } else {
            viewPropertyAnimator = child.animate().alpha(0f)
                .setDuration(200)
                .withEndAction {
                    child.alpha = 0f
                    child.visibility = View.GONE
                }
        }
        viewPropertyAnimator?.start()
    }
}