package com.revolgenx.anilib.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.field.BaseField

abstract class BaseViewModel<F:BaseField<*>>:ViewModel() {
    abstract val field: F
}