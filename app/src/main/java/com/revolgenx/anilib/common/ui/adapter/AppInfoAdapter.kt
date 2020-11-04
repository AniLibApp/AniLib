package com.revolgenx.anilib.common.ui.adapter

import android.view.View
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.revolgenx.anilib.ui.adapter.binder.AppInfoBinder
import com.revolgenx.anilib.ui.adapter.binder.AboutOtherInfoBinder

class AppInfoAdapter : DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder<*>>() {

    var onClickListener: View.OnClickListener? = null
        set(onClickListener) {
            field = onClickListener

            if (!isComputingLayout) {
                notifyDataSetChanged()
            }
        }

    init {
        addDataBinders(
            AppInfoBinder(this),
            AboutOtherInfoBinder(this)
        )
    }

}