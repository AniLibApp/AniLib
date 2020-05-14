package com.revolgenx.anilib.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.core.view.setPadding
import androidx.core.widget.ImageViewCompat
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.revolgenx.anilib.util.dp

typealias CheckListener = ((checked: Boolean) -> Unit)?

class DynamicToggleButton(context: Context, attributeSet: AttributeSet?, style: Int) :
    DynamicImageView(context, attributeSet, style) {

    private val accentColor by lazy { DynamicTheme.getInstance().get().accentColor }
    private val primaryTint by lazy { DynamicTheme.getInstance().get().tintPrimaryColor }

    private var checkListener: CheckListener = null
    private val rippleDrawable by lazy {
        RippleDrawable(ColorStateList.valueOf(accentColor), null, null)
    }

    var checked = false
        set(value) {
            field = value
            ImageViewCompat.setImageTintList(
                this,
                ColorStateList.valueOf(if (field) accentColor else primaryTint)
            )
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        this.setOnClickListener {
            checked = !checked
            checkListener?.invoke(checked)
        }
        background = rippleDrawable
        setPadding(dp(10f))
        checked = false
    }

    fun setToggleListener(listener: CheckListener) {
        checkListener = listener
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        when (state) {
            is SavedState -> {
                checked = state.isChecked
            }
            else -> {

            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState()).apply {
            isChecked = checked
        }
    }


    internal class SavedState : BaseSavedState {
        var isChecked = false

        constructor(parcelable: Parcelable?) : super(parcelable)
        constructor(parcel: Parcel) : super(parcel) {
            isChecked = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeByte(if (isChecked) 1 else 0)
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