package com.revolgenx.anilib.adapter.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.adapter.AppInfoAdapter


class PatreonInfoBinder(binderAdapter: AppInfoAdapter) : DynamicRecyclerViewBinder<PatreonInfoBinder.ViewHolder>(binderAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.patreon_info_layout, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        // Return item count.
        return 1
    }

    /**
     * Holder class to hold view holder item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val dynamicInfoView: DynamicInfoView = view.findViewById(R.id.info_app)

        init {
            dynamicInfoView.linksView.layoutManager =
                DynamicLayoutUtils.getGridLayoutManager(
                    dynamicInfoView.context,
                    DynamicLayoutUtils.getGridCount(dynamicInfoView.context)
                )
        }
    }
}