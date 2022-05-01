package com.revolgenx.anilib.ui.view

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.view.doOnNextLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicSurfaceColor
import com.revolgenx.anilib.databinding.AlDescriptionBinding
import com.revolgenx.anilib.util.copyToClipBoard

class AlDescriptionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = AlDescriptionBinding.inflate(LayoutInflater.from(context), this, true)

    private var animatorSet: AnimatorSet? = null

    private var recalculateHeights = false
    private var descExpandedHeight = -1
    private var descShrunkHeight = -1

    var expanded = false
        set(value) {
            if (field != value) {
                field = value
                updateExpandState()
            }
        }

    private val description: CharSequence get() = descriptionTv.text
    val descriptionTv get() = binding.descriptionText

    fun updateLayout() {
        recalculateHeights = true
        doOnNextLayout {
            updateExpandState()
        }
        if (!isInLayout) {
            requestLayout()
        }
    }

    private fun updateExpandState() = binding.apply {
        val initialSetup = descriptionText.maxHeight < 0

        val maxHeightTarget = if (expanded) descExpandedHeight else descShrunkHeight
        val maxHeightStart = if (initialSetup) maxHeightTarget else descriptionText.maxHeight
        val descMaxHeightAnimator = ValueAnimator().apply {
            setIntValues(maxHeightStart, maxHeightTarget)
            addUpdateListener {
                descriptionText.maxHeight = it.animatedValue as Int
            }
        }

        var pastHalf = false
        val toggleTarget = if (expanded) 1F else 0F
        val toggleStart = if (initialSetup) {
            toggleTarget
        } else {
            toggleMore.translationY / toggleMore.height
        }
//        val toggleAnimator = ValueAnimator().apply {
//            setFloatValues(toggleStart, toggleTarget)
//            addUpdateListener {
//                val value = it.animatedValue as Float
//
//                toggleMore.translationY = toggleMore.height * value
//                descriptionScrim.translationY = toggleMore.translationY
//
//                // Update non-animatable objects mid-animation makes it feel less abrupt
//                if (it.animatedFraction >= 0.5F && !pastHalf) {
//                    pastHalf = true
//                    descriptionText.text = description
//                }
//            }
//        }

        animatorSet?.cancel()
        animatorSet = AnimatorSet().apply {
            interpolator = FastOutSlowInInterpolator()
            duration = TOGGLE_ANIM_DURATION
            playTogether(/*toggleAnimator,*/ descMaxHeightAnimator)
            start()
        }

        val toggleDrawable = ContextCompat.getDrawable(context, R.drawable.ic_more_horiz)
        toggleMore.setImageDrawable(toggleDrawable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Wait until parent view has determined the exact width
        // because this affect the description line count
        val measureWidthFreely = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
        if (!recalculateHeights || measureWidthFreely) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        recalculateHeights = false

        // Measure with expanded lines
        binding.descriptionText.maxLines = Int.MAX_VALUE
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        descExpandedHeight = binding.descriptionText.measuredHeight

        // Measure with shrunk lines
        binding.descriptionText.maxLines = SHRUNK_DESC_MAX_LINES
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        descShrunkHeight = binding.descriptionText.measuredHeight
    }

    init {
        binding.descriptionScrim.background = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP,
            intArrayOf(dynamicSurfaceColor, Color.TRANSPARENT)
        )

        binding.descriptionText.apply {
            // So that 1 line of text won't be hidden by scrim
            minLines = DESC_MIN_LINES

            setOnLongClickListener {
                context.copyToClipBoard(description.toString())
                true
            }
        }

        arrayOf(
            binding.descriptionText,
            binding.descriptionScrim,
            binding.toggleMore,
        ).forEach {
            it.setOnClickListener { expanded = !expanded }
        }
    }
}

private const val TOGGLE_ANIM_DURATION = 300L

private const val DESC_MIN_LINES = 2
private const val SHRUNK_DESC_MAX_LINES = 3