package com.revolgenx.anilib.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.home.HomeOrderType
import com.revolgenx.anilib.model.home.HomeOrderedAdapterItem
import com.revolgenx.anilib.preference.getHomeOrderFromType
import com.revolgenx.anilib.preference.setHomeOrderFromType
import com.woxthebox.draglistview.DragItemAdapter
import kotlinx.android.synthetic.main.home_order_adapter_layout.view.*
import kotlinx.android.synthetic.main.home_order_dialog_layout.*
import timber.log.Timber


class HomeOrderDialog :BaseDialogFragment(){

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
        with(alertDialog){
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
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.home_order_adapter_layout, parent, false)
            view.homeOrderAdapterItemLayout.corner = 20f
            return ViewHolder(view)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.bind(itemList[position])
        }

        override fun getUniqueItemId(position: Int): Long {
            return mItemList[position].first
        }

        inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, R.id.homeOrderDragIcon, false) {
            private var mText: TextView = itemView.homeOrderName

            fun bind(item: Pair<Long, HomeOrderedAdapterItem>) {
                mText.text = item.second.name
            }

        }
    }
}