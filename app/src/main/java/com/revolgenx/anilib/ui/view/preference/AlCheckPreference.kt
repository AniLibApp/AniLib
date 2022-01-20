package com.revolgenx.anilib.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox
import com.revolgenx.anilib.R

class AlCheckPreference : DynamicCheckBox {

    private var prefKey: String? = null
    private var value = false

    val preferenceChangeListener: ((b: Boolean) -> Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun loadFromAttributes(attrs: AttributeSet?) {
        super.loadFromAttributes(attrs)
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.AlCheckPreference
        )
        try {
            prefKey = a.getString(R.styleable.AlCheckPreference_al_pref_key)
            value = a.getBoolean(R.styleable.AlCheckPreference_al_value, false)
        } finally {
            a.recycle()
        }
        isChecked = DynamicPreferences.getInstance().load(prefKey, value)

        setOnCheckedChangeListener { _, b ->
            DynamicPreferences.getInstance().save(prefKey, b)
            preferenceChangeListener?.invoke(b)
        }
    }

}