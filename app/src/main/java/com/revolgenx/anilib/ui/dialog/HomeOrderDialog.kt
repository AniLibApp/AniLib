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
import com.revolgenx.anilib.common.preference.isHomeOrderEnabled
import com.revolgenx.anilib.common.preference.setHomeOrderFromType
import com.revolgenx.anilib.databinding.HomeOrderAdapterLayoutBinding
import com.revolgenx.anilib.databinding.HomeOrderDialogLayoutBinding
import com.woxthebox.draglistview.DragItemAdapter

class HomeOrderDialog : BaseDialogFragment<HomeOrderDialogLayoutBinding>() {

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    override var titleRes: Int? = R.string.home_order_drag_message

    private val homeListOrder by lazy {
        requireContext().resources.getStringArray(R.array.home_list_order)
    }

    override fun bindView(): HomeOrderDialogLayoutBinding {
        return HomeOrderDialogLayoutBinding.inflate(provideLayoutInflater())
    }

    private lateinit var adapterPage: HomeOrderRecyclerAdapter

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        with(binding) {
            mDragListView.setCanDragHorizontally(false)
            mDragListView.setLayoutManager(LinearLayoutManager(requireContext()))

            val homeOrderTypes = HomeOrderType.values();
            val orderList = homeListOrder.mapIndexed { index, s ->
                val type = homeOrderTypes[index]
                Pair(
                    index.toLong(),
                    HomeOrderedAdapterItem(
                        s,
                        getHomeOrderFromType(requireContext(), type),
                        type,
                        isHomeOrderEnabled(requireContext(), type)
                    )
                )
            }.sortedBy { it.second.order }.toMutableList()

            adapterPage = HomeOrderRecyclerAdapter(orderList);
            mDragListView.setAdapter(adapterPage, true)
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        adapterPage.itemList.forEachIndexed { index, pair ->
            setHomeOrderFromType(
                requireContext(),
                pair.second.orderType,
                index,
                pair.second.isEnabled
            )
        }
        dismiss()
    }


    internal class HomeOrderRecyclerAdapter(
        list: MutableList<Pair<Long, HomeOrderedAdapterItem>>
    ) : DragItemAdapter<Pair<Long, HomeOrderedAdapterItem>, HomeOrderRecyclerAdapter.ViewHolder>() {

        init {
            itemList = list
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = HomeOrderAdapterLayoutBinding.inflate(
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

        inner class ViewHolder(private val binding: HomeOrderAdapterLayoutBinding) :
            DragItemAdapter.ViewHolder(binding.root, R.id.homeOrderDragIcon, false) {

            fun bind(item: Pair<Long, HomeOrderedAdapterItem>) {
                binding.homeOrderName.setOnCheckedChangeListener(null)
                binding.homeOrderName.text = item.second.name
                binding.homeOrderName.isChecked = item.second.isEnabled
                binding.homeOrderName.setOnCheckedChangeListener { _, isChecked ->
                    item.second.isEnabled = isChecked
                }
            }

        }
    }
}