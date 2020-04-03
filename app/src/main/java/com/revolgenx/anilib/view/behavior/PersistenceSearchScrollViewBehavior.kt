package com.revolgenx.anilib.view.behavior

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.paulrybitskyi.persistentsearchview.PersistentSearchView
import kotlin.math.max
import kotlin.math.min

class PersistenceSearchScrollViewBehavior(context: Context, attributeSet: AttributeSet?) :
    CoordinatorLayout.Behavior<ViewGroup?>(context, attributeSet) {

    constructor(context: Context) : this(context, null)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ViewGroup,
        dependency: View
    ): Boolean {
        return dependency is PersistentSearchView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout, child: ViewGroup,
        dependency: View
    ): Boolean {
        return if (dependency is PersistentSearchView) {
            (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = (dependency.height + dependency.translationY).toInt()
            true
        } else false
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ViewGroup,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: ViewGroup,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(-child.height.toFloat(), min(0f, child.translationY - dy))
    }

}