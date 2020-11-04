package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableFragment<T : BaseFragment>(var clzz: Class<T>, var bundle: Bundle?) : Parcelable