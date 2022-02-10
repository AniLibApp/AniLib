package com.revolgenx.anilib.social.ui.bottomsheet

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.SpoilerBottomSheetFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory

class SpoilerBottomSheetFragment : BaseLayoutFragment<SpoilerBottomSheetFragmentLayoutBinding>() {

    private lateinit var spanned: Spanned

    companion object {
        fun newInstance(spanned: Spanned) = SpoilerBottomSheetFragment().also {
            it.spanned = spanned
        }
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SpoilerBottomSheetFragmentLayoutBinding {
        return SpoilerBottomSheetFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        v.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.standard_bottom_sheet_dim
            )
        )
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSheetBackground(binding.spoierContainerLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.spoierContainerLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    closeFragment()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.spoilerCoordinatorLayout.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.spoilerCloserIv.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.spoilerTitleTv.text = getString(R.string.spoiler) + " ( ╯°□°)╯"

        if (!::spanned.isInitialized) return


        AlMarkwonFactory.getMarkwon().setParsedMarkdown(binding.spoilerTextTv, spanned)
    }


    private fun closeFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    private fun setupSheetBackground(view: View) {

        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(CornerFamily.ROUNDED, 16f)
            setTopLeftCorner(CornerFamily.ROUNDED, 16f)
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = dynamicBackgroundColor
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }
}