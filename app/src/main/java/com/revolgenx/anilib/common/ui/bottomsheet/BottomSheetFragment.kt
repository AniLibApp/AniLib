package com.revolgenx.anilib.common.ui.bottomsheet

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicCornerRadius

abstract class BottomSheetFragment<VB : ViewBinding> : BottomSheetDialogFragment() {
    override fun getTheme(): Int {
        return R.style.ThemeOverlay_App_BottomSheetDialog
    }

    protected open val setupSheetBackground = false

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!


    abstract fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!setupSheetBackground) {
            setupSheetBackground(binding.root)
        }

        (dialog as? BottomSheetDialog)?.behavior?.let {
            it.state = BottomSheetBehavior.STATE_EXPANDED
            it.skipCollapsed = true
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    fun show(ctx: Any) {
        when (ctx) {
            is FragmentActivity -> show(ctx.supportFragmentManager, TAG)
            is AppCompatActivity -> show(ctx.supportFragmentManager, TAG)
            is Fragment -> show(ctx.childFragmentManager, TAG)
            is PreferenceFragmentCompat -> show(ctx.childFragmentManager, TAG)
            else -> throw IllegalStateException("Context has no window attached.")
        }
    }

    protected fun setupSheetBackground(view: View) {
        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(CornerFamily.ROUNDED, dynamicCornerRadius.toFloat())
            setTopLeftCorner(CornerFamily.ROUNDED, dynamicCornerRadius.toFloat())
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = dynamicBackgroundColor
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }
}