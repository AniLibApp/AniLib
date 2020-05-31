package com.revolgenx.anilib.adapter.binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.view.DynamicHeader
import com.pranavpandey.android.dynamic.support.view.DynamicInfoView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.adapter.AppInfoAdapter
import com.revolgenx.anilib.util.openLink

class PrivacyPolicyBinder(binderAdapter: AppInfoAdapter) :
    DynamicRecyclerViewBinder<PrivacyPolicyBinder.ViewHolder>(binderAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.privacy_policy_info, parent, false)
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

        private val dynamicInfoView: DynamicHeader = view.findViewById(R.id.privacyPolicy)

        init {
            dynamicInfoView.setOnClickListener {
                it.context.openLink(it.context.getString(R.string.privacy_policy_url))
            }
        }
    }

}