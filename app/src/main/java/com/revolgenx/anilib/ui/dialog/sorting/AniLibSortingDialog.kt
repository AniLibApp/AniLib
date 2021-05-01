package com.revolgenx.anilib.ui.dialog.sorting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.AnilibSortingAdapterLayoutBinding
import com.revolgenx.anilib.databinding.AnilibSortingDialogLayoutBinding

class AniLibSortingDialog(val items: List<AniLibSortingModel>) :
    BaseDialogFragment<AnilibSortingDialogLayoutBinding>() {


    override var titleRes: Int? = R.string.sort
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    private val ascDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_asc).also {
            DynamicDrawableUtils.colorizeDrawable(it, contrastAccentWithBg)
        }
    }

    private val descDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_desc).also {
            DynamicDrawableUtils.colorizeDrawable(it, contrastAccentWithBg)
        }
    }

    private val checkDrawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ads_ic_check).also {
            DynamicDrawableUtils.colorizeDrawable(it, contrastAccentWithBg)
        }
    }

    override fun bindView(): AnilibSortingDialogLayoutBinding {
        return AnilibSortingDialogLayoutBinding.inflate(provideLayoutInflater)
    }

    override fun builder(dialogBuilder: DynamicDialog.Builder, savedInstanceState: Bundle?) {
        binding.sortingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sortingRecyclerView.adapter = AniLibSortingAdapter()
    }

    fun show(
        ctx: Context,
        func: AniLibSortingDialog.() -> Unit
    ): AniLibSortingDialog {
        this.func()
        this.show(ctx)
        return this
    }

    inner class AniLibSortingAdapter() : RecyclerView.Adapter<AniLibSortingViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AniLibSortingViewHolder {
            return AniLibSortingViewHolder(
                AnilibSortingAdapterLayoutBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: AniLibSortingViewHolder, position: Int) {
            val item = items[position]
            holder.binding.alSortingTv.text = item.title
            holder.binding.updateSortIv(item.order)

            holder.binding.root.setOnClickListener {
                val order = item.order
                items.firstOrNull { it.order != SortOrder.NONE }?.order = SortOrder.NONE
                item.order = when(order){
                    SortOrder.ASC -> SortOrder.DESC
                    SortOrder.DESC -> if(item.allowNone) SortOrder.NONE else SortOrder.ASC
                    SortOrder.NONE -> if(item.canSort) SortOrder.ASC else SortOrder.CHECK
                    SortOrder.CHECK -> SortOrder.NONE
                }
                notifyDataSetChanged()
            }
        }

        private fun AnilibSortingAdapterLayoutBinding.updateSortIv(order: SortOrder) {
            val icon = when (order) {
                SortOrder.ASC -> {
                    ascDrawable
                }
                SortOrder.DESC -> {
                    descDrawable
                }
                SortOrder.NONE -> {
                    null
                }
                SortOrder.CHECK -> {
                    checkDrawable
                }
            }
            alSortingOrderIv.setImageDrawable(icon)
        }

        override fun getItemCount(): Int = items.count()
    }

    inner class AniLibSortingViewHolder(val binding: AnilibSortingAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}