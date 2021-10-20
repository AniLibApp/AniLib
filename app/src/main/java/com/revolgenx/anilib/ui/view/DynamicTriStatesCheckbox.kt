package com.revolgenx.anilib.ui.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import com.pranavpandey.android.dynamic.support.widget.DynamicCheckBox
import com.revolgenx.anilib.R


class DynamicTriStatesCheckbox : DynamicCheckBox {

    private var mListener: ((state: TriStateCheckState) -> Unit)? = null
    var checkedState: TriStateCheckState = TriStateCheckState.EMPTY
        set(value) {
            field = value
            updateButtonView()
        }

    var triStateMode: TriStateMode = TriStateMode.TRI_MODE

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.DynamicTriStatesCheckbox,
            defStyleAttr,
            0
        )

        try {
            val mode =
                a.getInt(R.styleable.DynamicTriStatesCheckbox_mode, TriStateMode.TRI_MODE.ordinal)
            triStateMode = TriStateMode.values()[mode]
        } finally {
            a.recycle()
        }

        updateView()
    }


    private fun updateView() {
        setOnClickListener { _ ->
            checkedState = if (triStateMode == TriStateMode.BI_MODE) {
                if (checkedState == TriStateCheckState.TICK) TriStateCheckState.EMPTY else TriStateCheckState.TICK
            } else {
                when (checkedState) {
                    TriStateCheckState.TICK -> TriStateCheckState.CROSS
                    TriStateCheckState.CROSS -> TriStateCheckState.EMPTY
                    TriStateCheckState.EMPTY -> TriStateCheckState.TICK
                }
            }
            updateButtonView()
            mListener?.invoke(checkedState)
        }
        updateButtonView()
    }

    private fun updateButtonView() {
        val checkboxDrawable = when (checkedState) {
            TriStateCheckState.TICK -> R.drawable.ic_checkbox
            TriStateCheckState.CROSS -> R.drawable.ic_checkbox_intermediate
            TriStateCheckState.EMPTY -> R.drawable.ic_empty_checkbox
        }

        setButtonDrawable(checkboxDrawable)
    }

    fun setStateChangeListener(listener: (state: TriStateCheckState) -> Unit){
        mListener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(super.onSaveInstanceState()).apply {
            this.toSaveCheckState = checkedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if(state is SavedState){
            checkedState = state.toSaveCheckState
        }
    }

    fun toggleTriState(){
        checkedState = when(checkedState){
            TriStateCheckState.TICK -> TriStateCheckState.CROSS
            TriStateCheckState.CROSS -> TriStateCheckState.EMPTY
            TriStateCheckState.EMPTY -> TriStateCheckState.TICK
        }
    }

    internal class SavedState : BaseSavedState {
        var toSaveCheckState = TriStateCheckState.EMPTY

        constructor(parcelable: Parcelable?) : super(parcelable)
        constructor(parcel: Parcel) : super(parcel) {
            toSaveCheckState = TriStateCheckState.values()[parcel.readInt()]
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeInt(toSaveCheckState.ordinal)
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


enum class TriStateCheckState {
    TICK, CROSS, EMPTY
}

enum class TriStateMode {
    TRI_MODE, BI_MODE
}