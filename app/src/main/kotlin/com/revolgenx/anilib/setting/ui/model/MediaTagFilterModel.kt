package com.revolgenx.anilib.setting.ui.model

import androidx.compose.runtime.MutableState
import com.revolgenx.anilib.common.ui.model.BaseModel

data class MediaTagFilterModel(val name: String, var isSelected: MutableState<Boolean>, val data: BaseModel) {
    override fun equals(other: Any?): Boolean {
        if (other is MediaTagFilterModel) {
            return this.name == other.name
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        val result = name.hashCode()
        return result
    }
}