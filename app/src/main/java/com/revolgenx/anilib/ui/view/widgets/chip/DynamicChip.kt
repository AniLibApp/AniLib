package com.revolgenx.anilib.ui.view.widgets.chip

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.*

class DynamicChip : Chip {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        val chipBgColor = ColorStateList.valueOf(dynamicSurfaceColor)
        val tintAccent = contrastAccentWithSurface
        val chipStColor = ColorStateList.valueOf(tintAccent)

        chipBackgroundColor = chipBgColor
        isCloseIconVisible = true
        closeIconTint = chipStColor
        chipIconTint = chipStColor
        setTextColor(tintAccent)
        typeface = ResourcesCompat.getFont(context, R.font.rubik_regular)
    }

}