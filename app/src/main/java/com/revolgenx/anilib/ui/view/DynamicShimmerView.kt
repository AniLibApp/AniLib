package com.revolgenx.anilib.ui.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.adapter.DynamicShimmerAdapter

class DynamicShimmerView : ShimmerFrameLayout {

    private var adapterLayoutRes: Int = 0
    private var totalShimmers: Int = 8
    private var portraitShimmers: Int = 1
    private var landscapeShimmers: Int = 2
    private var shimmerOrientation: Int = 0 //horizontal
    private var whichLayoutManager: Int = 0 //LinearLayoutManager

    private lateinit var recyclerView: RecyclerView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.DynamicShimmerView,
            def,
            0
        )
        try {
            adapterLayoutRes = a.getResourceId(R.styleable.DynamicShimmerView_shimmerLayout, 0)
            totalShimmers = a.getInt(R.styleable.DynamicShimmerView_totalShimmer, 8)
            portraitShimmers = a.getInt(R.styleable.DynamicShimmerView_portraitShimmer, 1)
            landscapeShimmers = a.getInt(R.styleable.DynamicShimmerView_landscapeShimmer, 2)
            shimmerOrientation =
                a.getInt(R.styleable.DynamicShimmerView_shimmerOrientation, 0)
            whichLayoutManager =
                a.getInt(R.styleable.DynamicShimmerView_shimmerLayoutManager, 0)
        } finally {
            a.recycle()
        }
        updateView(context)
    }


    private fun updateView(context: Context) {
        recyclerView = RecyclerView(context).also {
            it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            it.isNestedScrollingEnabled = false
            it.clipToPadding = false
        }
        val mAdapter = DynamicShimmerAdapter(adapterLayoutRes, totalShimmers)
        recyclerView.layoutManager =
            if (whichLayoutManager == 0) {
                LinearLayoutManager(
                    context,
                    shimmerOrientation,
                    false
                )
            } else {
                GridLayoutManager(
                    context,
                    if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) landscapeShimmers else portraitShimmers
                )
            }
        recyclerView.adapter = mAdapter
        addView(recyclerView)
    }
}
