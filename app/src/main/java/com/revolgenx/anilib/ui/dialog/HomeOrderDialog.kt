package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.data.model.home.HomeOrderedAdapterItem
import com.revolgenx.anilib.common.preference.getHomeOrderFromType
import com.revolgenx.anilib.common.preference.setHomeOrderFromType
import com.revolgenx.anilib.databinding.HomeOrderAdapterLayoutBinding
import com.revolgenx.anilib.databinding.HomeOrderDialogLayoutBinding
import com.woxthebox.draglistview.DragItemAdapter

class HomeOrderDialog : BaseDialogFragment(){

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override var titleRes: Int? = R.string.home_order_drag_message
    override var viewRes: Int? = R.layout.home_order_dialog_layout

    private val homeListOrder by lazy {
        requireContext().resources.getStringArray(R.array.home_list_order)
    }

    private lateinit var adapter:HomeOrderRecyclerAdapter

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        val binding = HomeOrderDialogLayoutBinding.bind(dialogView)
        with(binding){
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))

            val homeOrderTypes = HomeOrderType.values();
            val orderList = homeListOrder.mapIndexed { index, s ->
                Pair(index.toLong(), HomeOrderedAdapterItem(s, getHomeOrderFromType(requireContext(), homeOrderTypes[index]), homeOrderTypes[index]))
            }.sortedBy { it.second.order }.toMutableList()

            adapter = HomeOrderRecyclerAdapter(orderList);
            mDragListView.setAdapter(adapter, true)
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        adapter.itemList.forEachIndexed { index, pair ->
            setHomeOrderFromType(requireContext(), pair.second.orderType, index + 1)
        }
        dismiss()
        activity?.recreate()
    }


    internal class HomeOrderRecyclerAdapter(
        list: MutableList<Pair<Long, HomeOrderedAdapterItem>>
    ) :DragItemAdapter<Pair<Long, HomeOrderedAdapterItem>, HomeOrderRecyclerAdapter.ViewHolder>(){

        init{
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = HomeOrderAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
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

        inner class ViewHolder(itemView: HomeOrderAdapterLayoutBinding) : DragItemAdapter.ViewHolder(itemView.root, R.id.homeOrderDragIcon, false) {
            private var mText: TextView = itemView.homeOrderName

            fun bind(item: Pair<Long, HomeOrderedAdapterItem>) {
                mText.text = item.second.name
            }

        }
    }
}