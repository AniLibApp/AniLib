package com.revolgenx.anilib.ui.view.drawable

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.Property
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.revolgenx.anilib.util.dp


class PlayPauseDrawable : BaseDrawablePaint() {

    enum class State(val startPoints: IntArray, val endPoints: IntArray) {
        Play(
            intArrayOf(
                8, 5, 8, 12, 19, 12, 19, 12,
                8, 12, 8, 19, 19, 12, 19, 12
            ), intArrayOf(
                12, 5, 5, 16, 12, 16, 12, 5,
                12, 5, 12, 16, 19, 16, 12, 5
            )
        ),
        Pause(
            intArrayOf(
                6, 5, 6, 19, 10, 19, 10, 5,
                14, 5, 14, 19, 18, 19, 18, 5
            ), intArrayOf(
                5, 6, 5, 10, 19, 10, 19, 6,
                5, 14, 5, 18, 19, 18, 19, 14
            )
        );

    }

    private var mIntrinsicSize = 0



    private var mPreviousState: State? = null
    private var mCurrentState = State.Play
    private var mFraction = 1f
    private var mNextState: State? = null

    private var mAnimator: Animator? = null

    private val mPath: Path = Path()
    private val mMatrix: Matrix = Matrix()


    init {
        mIntrinsicSize = dp(INTRINSIC_SIZE_DP.toFloat())
        mAnimator = ObjectAnimator.ofFloat(this, FRACTION_PROPERTY, 0f, 1f).setDuration(200)
        mAnimator!!.interpolator = FastOutSlowInInterpolator()
        mAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                tryMoveToNextState()
            }
        })
    }

    override fun getIntrinsicWidth(): Int {
        return mIntrinsicSize
    }

    override fun getIntrinsicHeight(): Int {
        return mIntrinsicSize
    }

    fun setAnimationDuration(duration: Long) {
        mAnimator!!.duration = duration
    }

    // The name getState() clashes with Drawable.getState().
    fun getPlayPauseState(): State? {
        return if (mNextState != null) mNextState else mCurrentState
    }

    fun jumpToState(state: State) {
        stop()
        mPreviousState = null
        mCurrentState = state
        mFraction = 1f
        mNextState = null
        invalidateSelf()
    }

    fun setState(state: State) {
        if (mCurrentState == state) {
            mNextState = null
            return
        }
        if (!isVisible) {
            jumpToState(state)
            return
        }
        mNextState = state
        tryMoveToNextState()
    }

    private fun tryMoveToNextState() {
        if (mNextState == null || isRunning()) {
            return
        }
        mPreviousState = mCurrentState
        mCurrentState = mNextState!!
        mFraction = 0f
        mNextState = null
        start()
    }

    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        if (isVisible != visible || restart) {
            stop()
            if (mNextState != null) {
                jumpToState(mNextState!!)
            }
            invalidateSelf()
        }
        return super.setVisible(visible, restart)
    }

    private fun start() {
        if (mAnimator!!.isStarted) {
            return
        }
        mAnimator!!.start()
        invalidateSelf()
    }

    private fun stop() {
        if (!mAnimator!!.isStarted) {
            return
        }
        mAnimator!!.end()
    }

    private fun isRunning(): Boolean {
        return mAnimator!!.isRunning
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (isRunning()) {
            invalidateSelf()
        }
    }

    override fun onPreparePaint(paint: Paint?) {
        paint?.style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas?, width: Int, height: Int, paint: Paint?) {
        mMatrix.setScale(width.toFloat() / 24, height.toFloat() / 24)
        when (mFraction) {
            0f -> {
                drawState(canvas!!, paint!!, mPreviousState)
            }
            1f -> {
                drawState(canvas!!, paint!!, mCurrentState)
            }
            else -> {
                drawBetweenStates(canvas!!, paint!!, mPreviousState, mCurrentState, mFraction)
            }
        }
    }


    private fun drawState(canvas: Canvas, paint: Paint, state: State?) {
        val points = state!!.startPoints
        mPath.rewind()
        var i = 0
        val count = points.size
        val subCount = count / 2
        while (i < count) {
            val x = points[i].toFloat()
            val y = points[i + 1].toFloat()
            if (i % subCount == 0) {
                if (i > 0) {
                    mPath.close()
                }
                mPath.moveTo(x, y)
            } else {
                mPath.lineTo(x, y)
            }
            i += 2
        }
        mPath.close()
        mPath.transform(mMatrix)
        // Drawing the transformed path makes rendering much less blurry than using canvas transform
// directly. See https://stackoverflow.com/a/16091390 .
        canvas.drawPath(mPath, paint)
    }

    private fun drawBetweenStates(
        canvas: Canvas, paint: Paint, fromState: State?, toState: State,
        fraction: Float
    ) {
        mMatrix.preRotate(lerp(0, 90, fraction), 12f, 12f)
        val startPoints = fromState!!.startPoints
        val endPoints = toState.endPoints
        mPath.rewind()
        var i = 0
        val count = startPoints.size
        val subCount = count / 2
        while (i < count) {
            val startX = startPoints[i]
            val startY = startPoints[i + 1]
            val endX = endPoints[i]
            val endY = endPoints[i + 1]
            val x: Float = lerp(startX, endX, fraction)
            val y: Float = lerp(startY, endY, fraction)
            if (i % subCount == 0) {
                if (i > 0) {
                    mPath.close()
                }
                mPath.moveTo(x, y)
            } else {
                mPath.lineTo(x, y)
            }
            i += 2
        }
        mPath.close()
        mPath.transform(mMatrix)
        canvas.drawPath(mPath, paint)
    }


    companion object {
        private const val INTRINSIC_SIZE_DP = 24

        private val FRACTION_PROPERTY: Property<PlayPauseDrawable, Float> =
            object : Property<PlayPauseDrawable, Float>(Float::class.java, "fraction") {

                override fun set(d: PlayPauseDrawable?, value: Float?) {
                    d?.mFraction = value!!
                }

                override fun get(d: PlayPauseDrawable?): Float {
                    return d!!.mFraction
                }
            }

        fun lerp(
            start: Int,
            end: Int,
            fraction: Float
        ): Float {
            return start + (end - start) * fraction
        }
    }
}