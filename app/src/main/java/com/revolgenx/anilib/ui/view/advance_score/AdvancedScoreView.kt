package com.revolgenx.anilib.ui.view.advance_score

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.entry.AdvancedScore
import kotlinx.android.synthetic.main.advaced_scoring_layout.view.*

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
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        )
        adapter = mAdapter
    }

    var advanceScoreObserver: (() -> Unit)? = null
    var advancedScores = mutableListOf<AdvancedScore>()

    fun setAdvanceScore(advancedScores: List<AdvancedScore>) {
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
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.advaced_scoring_layout, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return advancedScores.size
        }

        override fun onBindViewHolder(holder: AdvanceScoringViewHolder, position: Int) {
            val data = advancedScores[position]
            holder.itemView.apply {
                advanceScoringEt.setCounter(data.score)
                advancedScoringTv.text = data.scoreType
                advanceScoringEt.textChangeListener {
                    it.toString().toDoubleOrNull()?.let {
                        data.score = it
                        advanceScoreObserver?.invoke()
                    }
                }
            }
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            advancedScores.clear()
            super.onDetachedFromRecyclerView(recyclerView)
        }
    }


    class AdvanceScoringViewHolder(v: View) : ViewHolder(v) {

    }
}

