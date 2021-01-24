package com.revolgenx.anilib.ui.view


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_ALPHA_MIN = 0f

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_ALPHA_MAX = 1f

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
const val ANIM_DURATION_300 = 300L

/** Fade in a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun View.fadeIn(
    alpha: Float = ANIM_ALPHA_MAX,
    duration: Long = ANIM_DURATION_300,
    listener: (() -> Unit)? = null
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}

/** Fade in a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun animValues(
    valueStart: Float = ANIM_ALPHA_MIN,
    valueEnd: Float = ANIM_ALPHA_MAX,
    duration: Long = ANIM_DURATION_300,
    listener: (Float) -> Unit,
    listenerFinished: () -> Unit
): ValueAnimator {

    return ValueAnimator.ofFloat(valueStart, valueEnd).apply {
        setDuration(duration)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation -> listener.invoke(animation.animatedValue as Float) }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                listenerFinished.invoke()
            }
        })
        start()
    }
}

/** Fade out a view. */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun View.fadeOut(
    alpha: Float = ANIM_ALPHA_MIN,
    duration: Long = ANIM_DURATION_300,
    listener: (() -> Unit)? = null
) {

    animate().alpha(alpha)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { listener?.invoke() }
        .start()
}