package com.revolgenx.anilib.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.databinding.DynamicBottomSheetLayoutBinding

abstract class DynamicBottomSheetFragment<V : ViewBinding> : BottomSheetDialogFragment() {
    private var _binding: V? = null
    protected val binding get() = _binding!!

    private var _rootBinding: DynamicBottomSheetLayoutBinding? = null
    protected val rootBinding get() = _rootBinding!!


    open lateinit var windowContext: Context

    protected val dynamicTheme get() = DynamicTheme.getInstance().get()
    protected open val title: Int? = null
    protected open val positiveText: Int? = null
    protected open val negativeText: Int? = null

    var onPositiveClicked: (() -> Unit)? = null
    var onNegativeClicked: (() -> Unit)? = null

    companion object {
        private val dialogTag = "DynamicBottomSheet"
    }

    protected abstract fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater, container, savedInstanceState)
        _rootBinding = DynamicBottomSheetLayoutBinding.inflate(inflater, container, false)
        rootBinding.bottomSheetContainerLayout.addView(binding.root)
        rootBinding.root.setBackgroundColor(dynamicTheme.backgroundColor)
        return rootBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootBinding.setupBottomSheet()
        setupBottomSheetBehavior(view)
    }

    private fun setupBottomSheetBehavior(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog? ?: return
                val dialogBehavior = dialog.behavior
                dialogBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                dialogBehavior.peekHeight = -1
                dialogBehavior.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, dY: Float) {
                        // TODO: Make button layout stick to the bottom through translationY property.
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        })
    }

    private fun DynamicBottomSheetLayoutBinding.setupBottomSheet() {
        title?.let {
            bottomSheetTitleTv.visibility = View.VISIBLE
            bottomSheetTitleTv.setText(it)
        }

        positiveText?.let {
            bottomSheetPositiveTv.visibility = View.VISIBLE
            bottomSheetPositiveTv.setText(it)
            bottomSheetPositiveTv.setOnClickListener {
                onPositiveClicked?.invoke()
            }
        }

        negativeText?.let {
            bottomSheetNegativeTv.visibility = View.VISIBLE
            bottomSheetNegativeTv.setText(it)
            bottomSheetNegativeTv.setOnClickListener {
                onNegativeClicked?.invoke()
            }
        }
    }

    protected fun displayButtonPositive(bool: Boolean) {
        rootBinding.bottomSheetPositiveTv.visibility = if (bool) View.VISIBLE else View.GONE
    }


    protected fun layoutInflater() = LayoutInflater.from(requireContext())!!

    /** Show the bottom sheet. */
    fun show() {
        windowContext.let { ctx ->
            when (ctx) {
                is FragmentActivity -> show(ctx.supportFragmentManager, dialogTag)
                is AppCompatActivity -> show(ctx.supportFragmentManager, dialogTag)
                is Fragment -> show(ctx.childFragmentManager, dialogTag)
                is PreferenceFragmentCompat -> show(ctx.childFragmentManager, dialogTag)
                else -> throw IllegalStateException("Context has no window attached.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _rootBinding = null
    }
}