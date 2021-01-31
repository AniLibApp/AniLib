package com.revolgenx.anilib.ui.view


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class TitleTextView(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    RelativeLayout(context, attributeSet, defStyle) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        background = RippleDrawable(ColorStateList.valueOf(DynamicTheme.getInstance().get().accentColor), null, null)

        val textColor = DynamicTheme.getInstance().get().tintPrimaryColor
        var titleName = ""
        attributeSet?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.TitleTextView, 0, 0)
                .apply {
                    titleName = getString(R.styleable.TitleTextView_titleName) ?: ""
                }
        }

        DynamicTextView(context)
            .let {
                it.id = R.id.customTextViewTitleId
                it.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                if (isInEditMode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    it.typeface = resources.getFont(R.font.cabin_regular)
                else it.typeface = ResourcesCompat.getFont(context, R.font.cabin_regular)

                it.gravity = Gravity.CENTER
                it.color = textColor
                it.textSize = 11f
                it.text = titleName
                addView(it)
            }

        DynamicTextView(context)
            .let {
                it.id = R.id.customTextViewDescriptionId
                it.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                ).also { params ->
                    params.addRule(BELOW, R.id.customTextViewTitleId)
                    params.addRule(CENTER_IN_PARENT)
                    params.setMargins(0,dp(2f),0,0)
                }
                it.color = textColor
                if (isInEditMode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    it.typeface = resources.getFont(R.font.cabincondensed_regular)
                else it.typeface = ResourcesCompat.getFont(context, R.font.cabincondensed_regular)

                if (isInEditMode)
                    it.text = "text test description"
                it.gravity = Gravity.CENTER
                it.maxLines = 2
                it.ellipsize = TextUtils.TruncateAt.END
                it.textSize = 13f
                addView(it)
            }
    }

    constructor(context: Context) : this(context, null, 0)

    fun titleTextView() = findViewById<TextView>(R.id.customTextViewTitleId)
    fun descriptionTextView() = findViewById<TextView>(R.id.customTextViewDescriptionId)

    var description = ""
        set(value) {
            field = value
            descriptionTextView().text = value
        }

}
