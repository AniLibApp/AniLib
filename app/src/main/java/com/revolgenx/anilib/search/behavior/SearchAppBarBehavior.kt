package com.revolgenx.anilib.search.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.revolgenx.anilib.R

class SearchAppBarBehavior : AppBarLayout.Behavior {
    constructor():super()
    constructor(context:Context, attrs: AttributeSet): super(context, attrs)
    private var isSheetTouched = false
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        isSheetTouched = target.id == R.id.filter_nested_scroll_view
        return !isSheetTouched
                && super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            isSheetTouched = false;
        }
        return !isSheetTouched && super.onInterceptTouchEvent(parent, child, ev)
    }
}