package com.revolgenx.anilib.common.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.DynamicBottomSheetFragmentBinding

abstract class DynamicBottomSheetFragment<VB : ViewBinding> : BottomSheetFragment<VB>() {
    private var _bottomSheetBinding: DynamicBottomSheetFragmentBinding? = null
    protected val bottomSheetBinding get() = _bottomSheetBinding!!

    override val setupSheetBackground: Boolean = true

    var onPositiveClicked: (() -> Unit)? = null
    var onNegativeClicked: (() -> Unit)? = null

    @StringRes
    var positiveTextRes: Int = R.string.done

    @StringRes
    var negativeTextRes: Int = R.string.cancel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _bottomSheetBinding = DynamicBottomSheetFragmentBinding.inflate(inflater, container, false)
        bottomSheetBinding.bottomSheetContainerLayout.addView(binding.root)
        return bottomSheetBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSheetBackground(bottomSheetBinding.root)

        bottomSheetBinding.apply {
            bottomSheetPositiveTv.setText(positiveTextRes)
            bottomSheetNegativeTv.setText(negativeTextRes)
            bottomSheetPositiveTv.setOnClickListener {
                onPositiveClicked?.invoke()
            }
            bottomSheetNegativeTv.setOnClickListener {
                onNegativeClicked?.invoke()
            }
        }
    }

}