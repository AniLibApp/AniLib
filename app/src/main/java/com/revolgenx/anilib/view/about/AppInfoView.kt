package com.revolgenx.anilib.view.about

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.adapter.AppInfoAdapter

class AppInfoView : DynamicRecyclerViewFrame {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null)
            : super(context, attrs) {
        setAdapter()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        setAdapter()
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager? {
        return DynamicLayoutUtils.getLinearLayoutManager(context, LinearLayoutManager.VERTICAL)
    }

    override fun getLayoutRes(): Int {
        return R.layout.ads_recycler_view_raw
    }

    private fun setAdapter(): AppInfoView {
        // Set adapter for the recycler view.
        adapter = AppInfoAdapter()

        return this
    }


}
