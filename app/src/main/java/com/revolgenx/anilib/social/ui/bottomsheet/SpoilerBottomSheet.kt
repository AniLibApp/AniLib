package com.revolgenx.anilib.social.ui.bottomsheet

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.shape.MaterialShapeDrawable
import com.maxkeppeler.sheets.core.Sheet
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.databinding.SpoilerBottomSheetLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory

class SpoilerBottomSheet : Sheet() {

    private var _binding: SpoilerBottomSheetLayoutBinding? = null
    private val binding: SpoilerBottomSheetLayoutBinding get() = _binding!!
    private lateinit var spanned: Spanned

    companion object {
        fun newInstance(spanned: Spanned) = SpoilerBottomSheet().also {
            it.spanned = spanned
        }
    }

    override fun onCreateLayoutView(): View {
        displayButtonsView(false)
        title(getString(R.string.spoiler) + " ( ╯°□°)╯")
        titleColor(contrastAccentWithBg)
        _binding = SpoilerBottomSheetLayoutBinding.inflate(LayoutInflater.from(windowContext), null, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.background as MaterialShapeDrawable).fillColor =
            ColorStateList.valueOf(dynamicBackgroundColor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!::spanned.isInitialized) return

        AlMarkwonFactory.getMarkwon().setParsedMarkdown(binding.spoilerTextTv, spanned)

    }

    /** Build [InfoSheet] and show it later. */
    fun build(
        ctx: Context,
        width: Int? = null,
        func: SpoilerBottomSheet.() -> Unit
    ): SpoilerBottomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        return this
    }

    /** Build and show [InfoSheet] directly. */
    fun show(
        ctx: Context,
        width: Int? = null,
        func: SpoilerBottomSheet.() -> Unit
    ): SpoilerBottomSheet {
        this.windowContext = ctx
        this.width = width
        this.func()
        this.show()
        return this
    }

//    override fun show(manager: FragmentManager, tag: String?) {
//        manager.beginTransaction().add(R.id.main_fragment_container, this, tag).commit()
//    }

}