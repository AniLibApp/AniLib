package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableFragment(var clzz: Class<*>, var bundle: Bundle?) : Parcelable