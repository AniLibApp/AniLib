package com.revolgenx.anilib.ui.view.advance_score

import android.content.Context
import android.content.res.Configuration
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.entry.data.model.AdvancedScoreModel
import com.revolgenx.anilib.databinding.AdvancedScoringLayoutBinding

class AdvancedScoreView : DynamicRecyclerView {
    private var mAdapter: AdvancedScoringAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        mAdapter = AdvancedScoringAdapter()
        layoutManager = GridLayoutManager(
            context,
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        )
        adapter = mAdapter
    }

    var advanceScoreObserver: (() -> Unit)? = null
    var advancedScores = mutableListOf<MutablePair<String, Double>>()

    fun setAdvanceScore(advancedScores: List<MutablePair<String, Double>>) {
        this.advancedScores.clear()
        this.advancedScores.addAll(advancedScores)
        mAdapter.notifyDataSetChanged()
    }

    inner class AdvancedScoringAdapter :
        RecyclerView.Adapter<AdvanceScoringViewHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AdvanceScoringViewHolder {
            return AdvanceScoringViewHolder(
                AdvancedScoringLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return advancedScores.size
        }

        override fun onBindViewHolder(holder: AdvanceScoringViewHolder, position: Int) {
            val data = advancedScores[position]
            holder.binding.apply {
                advanceScoringEt.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                advanceScoringEt.updateCount(data.second)
                advancedScoringTv.text = data.first
                advanceScoringEt.onCountChangeListener = {
                    data.second = it
                    advanceScoreObserver?.invoke()
                }
            }
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            advancedScores.clear()
            super.onDetachedFromRecyclerView(recyclerView)
        }
    }


    class AdvanceScoringViewHolder(val binding: AdvancedScoringLayoutBinding) :
        ViewHolder(binding.root)
}

