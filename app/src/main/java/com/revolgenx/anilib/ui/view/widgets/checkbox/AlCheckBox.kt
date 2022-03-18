package com.revolgenx.anilib.ui.view.widgets.checkbox

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.util.dp

class AlCheckBox : LinearLayout {

    enum class CheckBoxState {
        CHECKED, INTERMEDIATE, UNCHECKED
    }

    var state: CheckBoxState = CheckBoxState.UNCHECKED
    var hasIntermediate = false

    var onCheckChangeListener: ((state: CheckBoxState) -> Unit)? = null

    val checkDrawable: DynamicImageView
    val textView: DynamicTextView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        orientation = HORIZONTAL

        textView = DynamicTextView(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).also {
                    it.gravity = Gravity.CENTER_VERTICAL
                }
            it.colorType = Theme.ColorType.TINT_BACKGROUND
            it.gravity = Gravity.CENTER_VERTICAL
            it.textSize = 14f
        }
        checkDrawable = DynamicImageView(context).also {
            it.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).also {
                    it.gravity = Gravity.CENTER_VERTICAL
                }
            it.colorType = Theme.ColorType.TINT_BACKGROUND
            it.setPadding(dp(10f))
            it.setImageResource(R.drawable.ic_uncheck_circle)
        }

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.AlCheckBox,
            defStyle,
            0
        )

        try {
            textView.text = a.getString(R.styleable.AlCheckBox_text)
            hasIntermediate = a.getBoolean(R.styleable.AlCheckBox_intermediate_mode, false)
        } finally {
            a.recycle()
        }

        addView(checkDrawable)
        addView(textView)

        setOnClickListener {
            toggleState()
            updateCheckbox()
            onCheckChangeListener?.invoke(state)
        }
    }

    fun updateState(bool: Boolean?) {
        state = when(bool){
            true->{
                CheckBoxState.CHECKED
            }
            false->{
                CheckBoxState.UNCHECKED
            }
            null ->{
                CheckBoxState.INTERMEDIATE
            }
        }
        updateCheckbox()
    }


    private fun toggleState() {
        state = when (state) {
            CheckBoxState.CHECKED -> if (hasIntermediate) CheckBoxState.INTERMEDIATE else CheckBoxState.UNCHECKED
            CheckBoxState.INTERMEDIATE -> CheckBoxState.UNCHECKED
            CheckBoxState.UNCHECKED -> CheckBoxState.CHECKED
        }
    }

    private fun updateCheckbox() {
        when (state) {
            CheckBoxState.CHECKED -> {
                checkDrawable.colorType = Theme.ColorType.ACCENT
                textView.colorType = Theme.ColorType.ACCENT
                checkDrawable.setImageResource(R.drawable.ic_check_circle)
            }
            CheckBoxState.INTERMEDIATE -> {
                checkDrawable.colorType = Theme.ColorType.ACCENT
                textView.colorType = Theme.ColorType.ACCENT
                checkDrawable.setImageResource(R.drawable.ic_intermediate_circle)
            }
            CheckBoxState.UNCHECKED -> {
                textView.colorType = Theme.ColorType.TINT_BACKGROUND
                checkDrawable.colorType = Theme.ColorType.TINT_BACKGROUND
                checkDrawable.setImageResource(R.drawable.ic_uncheck_circle)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState()).also {
            it.state = this.state
            it.hasIntermediate = this.hasIntermediate
        }
    }

    override fun onRestoreInstanceState(parcelable: Parcelable?) {
        super.onRestoreInstanceState(parcelable)
        if (parcelable is SavedState) {
            this.state = parcelable.state
            this.hasIntermediate = parcelable.hasIntermediate
        }
    }

    internal class SavedState : BaseSavedState {
        constructor(parcelable: Parcelable?) : super(parcelable)
        constructor(parcel: Parcel) : super(parcel) {
            this.state = CheckBoxState.values()[parcel.readInt()]
            this.hasIntermediate = parcel.readInt() == 1
        }

        var state: CheckBoxState = CheckBoxState.UNCHECKED
        var hasIntermediate = false
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(state.ordinal)
            parcel.writeInt(if (hasIntermediate) 1 else 0)
        }


        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }


}