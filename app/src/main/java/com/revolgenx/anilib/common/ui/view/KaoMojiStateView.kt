package com.revolgenx.anilib.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R

class KaoMojiStateView : RelativeLayout {


    enum class KoeMojiState {
        ERROR, LOADING
    }

    private var kaoMojiTv: TextView
    private var kaoMojiMsgTv: TextView

    var kaoMojiText: String = ""
        set(value) {
            field = value
            kaoMojiTv.text = value
        }

    var kaoMojiMsgText: String = ""
        set(value) {
            field = value
            kaoMojiMsgTv.text = value
        }

    var kaoMojiState: KoeMojiState
        set(value) {
            field = value
            when (value) {
                KoeMojiState.ERROR -> {
                    kaoMojiText = errorKoeMoji.random()
                    kaoMojiMsgText = context.getString(R.string.failed_to_load)
                }
                KoeMojiState.LOADING -> {
                    kaoMojiText = loadingKoeMoji.random()
                    kaoMojiMsgText = context.getString(R.string.loading)
                }
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        kaoMojiTv = DynamicTextView(context, attributeSet).also {
            it.id = R.id.kaoMojiTv
            it.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).also {
                it.addRule(CENTER_IN_PARENT, TRUE)
            }
            it.textSize = 36f
            it.textAlignment = TEXT_ALIGNMENT_CENTER
            it.typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
        }
        kaoMojiMsgTv = DynamicTextView(context).also {
            it.layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).also {
                it.addRule(BELOW, kaoMojiTv.id)
                it.setMargins(0, DynamicUnitUtils.convertDpToPixels(12f),0,0)
            }
            it.textAlignment = TEXT_ALIGNMENT_CENTER
            it.textSize = 14f
            it.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        }
        addView(kaoMojiTv)
        addView(kaoMojiMsgTv)

        kaoMojiState = KoeMojiState.LOADING

    }
}