package com.revolgenx.anilib.app.setting.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getHomePageOrderFromType
import com.revolgenx.anilib.common.preference.setHomePageOrderFromType
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.home.data.meta.HomePageOrderType
import com.revolgenx.anilib.app.setting.data.meta.HomePageOrderAdapterItem
import com.revolgenx.anilib.databinding.HomePageOrderAdapterDialogLayoutBinding
import com.revolgenx.anilib.databinding.HomePageOrderDialogLayoutBinding
import com.woxthebox.draglistview.DragItemAdapter

class HomePageOrderDialog : BaseDialogFragment<HomePageOrderDialogLayoutBinding>() {

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override var titleRes: Int? = R.string.home_order_drag_message

    private val homePageListOrder by lazy {
        requireContext().resources.getStringArray(R.array.home_page_list_order)
    }

    override fun bindView(): HomePageOrderDialogLayoutBinding {
        return HomePageOrderDialogLayoutBinding.inflate(provideLayoutInflater)
    }

    private lateinit var adapterPage: HomePageOrderRecyclerAdapter

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        with(binding) {
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))

            val homePageOrderTypes = HomePageOrderType.values();
            val orderList = homePageListOrder.mapIndexed { index, s ->
                Pair(
                    index.toLong(),
                    HomePageOrderAdapterItem(
                        s,
                        getHomePageOrderFromType(requireContext(), homePageOrderTypes[index]),
                        homePageOrderTypes[index]
                    )
                )
            }.sortedBy { it.second.order }.toMutableList()

            adapterPage = HomePageOrderRecyclerAdapter(orderList);
            mDragListView.setAdapter(adapterPage, true)
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        adapterPage.itemList.forEachIndexed { index, pair ->
            setHomePageOrderFromType(requireContext(), pair.second.orderType, index)
        }
        dismiss()
    }


    internal class HomePageOrderRecyclerAdapter(
        list: MutableList<Pair<Long, HomePageOrderAdapterItem>>
    ) : DragItemAdapter<Pair<Long, HomePageOrderAdapterItem>, HomePageOrderRecyclerAdapter.ViewHolder>() {

        init {
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = HomePageOrderAdapterDialogLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.homeOrderAdapterItemLayout.corner = 20f
            return ViewHolder(binding)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.bind(itemList[position])
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].first
        }

        inner class ViewHolder(itemView: HomePageOrderAdapterDialogLayoutBinding) :
            DragItemAdapter.ViewHolder(itemView.root, R.id.homeOrderDragIcon, false) {
            private var mText: TextView = itemView.homeOrderName

            fun bind(item: Pair<Long, HomePageOrderAdapterItem>) {
                mText.text = item.second.name
            }

        }
    }
}