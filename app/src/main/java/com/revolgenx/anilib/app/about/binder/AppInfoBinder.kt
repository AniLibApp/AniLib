package com.revolgenx.anilib.app.about.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicItemsAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.base.DynamicInfoView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.AlAppInfoAdapter
import com.revolgenx.anilib.util.openLink

class AppInfoBinder(binderAdapter: AlAppInfoAdapter) : DynamicRecyclerViewBinder<AppInfoBinder.ViewHolder>(binderAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_info_app, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 1
    }

    /**
     * Holder class to hold view holder item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val dynamicInfoView: DynamicInfoView = view.findViewById(R.id.info_app)

        init {
            dynamicInfoView.linksView?.layoutManager =
                DynamicLayoutUtils.getGridLayoutManager(
                    dynamicInfoView.context,
                    2
                )

            dynamicInfoView.setOnClickListener {
                view.context.openLink(view.context.getString(R.string.site_url))
            }
            (dynamicInfoView.linksView?.adapter as? DynamicItemsAdapter)?.data?.forEach { it.colorType =
                Theme.ColorType.TINT_SURFACE
            }

        }
    }
}