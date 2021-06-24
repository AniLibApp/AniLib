package com.revolgenx.anilib.social.ui.viewmodel.activity_composer

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.SaveActivityField
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

abstract class ActivityComposerViewModel<CM, M : BaseModel, F : SaveActivityField<*>> :
    BaseViewModel() {
    var text: String = ""
    var activeModel: M? = null
    abstract val field: F
    fun resetField() {
        field.id = null
        field.text = ""
    }

    fun saveActivity(callback: (Resource<CM>) -> Unit) {
        resetField()
        activeModel?.let {
            field.id = it.id
        }
        field.text = text
        save(callback)
    }

    protected abstract fun save(callback: (Resource<CM>) -> Unit)
}
