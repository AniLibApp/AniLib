package com.revolgenx.anilib.adapter

import android.view.View
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.revolgenx.anilib.adapter.binder.AppInfoBinder
import com.revolgenx.anilib.adapter.binder.BmcInfoBinder
import com.revolgenx.anilib.adapter.binder.PatreonInfoBinder
import com.revolgenx.anilib.adapter.binder.PrivacyPolicyBinder

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
            PatreonInfoBinder(this),
            BmcInfoBinder(this),
            PrivacyPolicyBinder(this)
        )
    }

}