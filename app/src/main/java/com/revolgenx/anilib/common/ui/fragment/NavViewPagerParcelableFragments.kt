package com.revolgenx.anilib.common.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NavViewPagerParcelableFragments(
    val clzzes: List<String>,
    val bundles: List<Bundle>
): Parcelable