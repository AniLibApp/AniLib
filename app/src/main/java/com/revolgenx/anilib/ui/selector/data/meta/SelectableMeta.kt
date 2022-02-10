package com.revolgenx.anilib.ui.selector.data.meta

import android.os.Parcelable
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.ui.selector.constant.SelectableTypes
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectableMeta(val title: Int? = null,
                          val hasIntermediateMode: Boolean = false,
                          val selectableItems: List<MutablePair<String, SelectedState>>) : Parcelable