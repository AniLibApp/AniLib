package com.revolgenx.anilib.common.ui.adapter

import android.view.View
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.revolgenx.anilib.app.about.binder.AppInfoBinder
import com.revolgenx.anilib.app.about.binder.AboutOtherInfoBinder

class AlAppInfoAdapter : DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder<*>>() {
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