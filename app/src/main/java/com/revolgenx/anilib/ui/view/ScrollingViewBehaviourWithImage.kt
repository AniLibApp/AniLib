package com.revolgenx.anilib.ui.view

//todo: delete if not used


//class ScrollingViewBehaviourWithImage(context:Context, attributeSet: AttributeSet?):AppBarLayout.ScrollingViewBehavior(){
//
//    constructor(context: Context):this(context, null)
//
//    override fun onDependentViewChanged(
//        parent: CoordinatorLayout,
//        child: View,
//        dependency: View
//    ): Boolean {
//
//        if(dependency is DynamicLinearLayout){
//            Timber.d("scrolling behav%s", dependency.layoutParams.height)
//            if(child is NestedScrollView){
//                (child.layoutParams as CoordinatorLayout.LayoutParams).setMargins(0,dependency.layoutParams.height, 0,0)
//            }
//        }
//
//        return super.onDependentViewChanged(parent, child, dependency)
//    }
//}