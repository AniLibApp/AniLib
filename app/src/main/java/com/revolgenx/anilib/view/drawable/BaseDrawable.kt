package com.revolgenx.anilib.view.drawable

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.NonNull


abstract class BaseDrawable : Drawable() {
    protected var mAlpha = 0xFF
    protected var mColorFilter: ColorFilter? = null
    protected var mTintList: ColorStateList? = null
    protected var mTintMode: PorterDuff.Mode? = PorterDuff.Mode.SRC_IN
    protected var mTintFilter: PorterDuffColorFilter? = null

    private val mConstantState = DummyConstantState()

    override fun getAlpha(): Int {
        return mAlpha
    }

    /**
     * {@inheritDoc}
     */
    override fun setAlpha(alpha: Int) {
        if (mAlpha != alpha) {
            mAlpha = alpha
            invalidateSelf()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun getColorFilter(): ColorFilter? {
        return mColorFilter
    }

    /**
     * {@inheritDoc}
     */
    override fun setColorFilter(colorFilter: ColorFilter?) {
        mColorFilter = colorFilter
        invalidateSelf()
    }

    /**
     * {@inheritDoc}
     */
    override fun setTint(@ColorInt tintColor: Int) {
        setTintList(ColorStateList.valueOf(tintColor))
    }

    /**
     * {@inheritDoc}
     */
    override fun setTintList(tint: ColorStateList?) {
        mTintList = tint
        if (updateTintFilter()) {
            invalidateSelf()
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun setTintMode(@NonNull tintMode: PorterDuff.Mode?) {
        mTintMode = tintMode
        if (updateTintFilter()) {
            invalidateSelf()
        }
    }

    override fun isStateful(): Boolean {
        return mTintList != null && mTintList!!.isStateful
    }

    override fun onStateChange(state: IntArray?): Boolean {
        return updateTintFilter()
    }

    private fun updateTintFilter(): Boolean {
        if (mTintList == null || mTintMode == null) {
            val hadTintFilter = mTintFilter != null
            mTintFilter = null
            return hadTintFilter
        }
        val tintColor = mTintList!!.getColorForState(state, Color.TRANSPARENT)
        // They made PorterDuffColorFilter.setColor() and setMode() @hide.
        mTintFilter = PorterDuffColorFilter(tintColor, mTintMode!!)
        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun getOpacity(): Int { // Be safe.
        return PixelFormat.TRANSLUCENT
    }

    /**
     * {@inheritDoc}
     */
    override fun draw(canvas: Canvas) {
        val bounds = bounds
        if (bounds.width() == 0 || bounds.height() == 0) {
            return
        }
        val saveCount = canvas.save()
        canvas.translate(bounds.left.toFloat(), bounds.top.toFloat())
        onDraw(canvas, bounds.width(), bounds.height())
        canvas.restoreToCount(saveCount)
    }

    protected fun getColorFilterForDrawing(): ColorFilter? {
        return if (mColorFilter != null) mColorFilter else mTintFilter
    }

    protected abstract fun onDraw(canvas: Canvas?, width: Int, height: Int)

    // Workaround LayerDrawable.ChildDrawable which calls getConstantState().newDrawable()
    // without checking for null.
    // We are never inflated from XML so the protocol of ConstantState does not apply to us. In
    // order to make LayerDrawable happy, we return ourselves from DummyConstantState.newDrawable().

    // Workaround LayerDrawable.ChildDrawable which calls getConstantState().newDrawable()
// without checking for null.
// We are never inflated from XML so the protocol of ConstantState does not apply to us. In
// order to make LayerDrawable happy, we return ourselves from DummyConstantState.newDrawable().
    override fun getConstantState(): ConstantState? {
        return mConstantState
    }

    inner class DummyConstantState : ConstantState() {
        override fun getChangingConfigurations(): Int {
            return 0
        }

        @NonNull
        override fun newDrawable(): Drawable {
            return this@BaseDrawable
        }
    }

}