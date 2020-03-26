package com.revolgenx.anilib.viewmodel

import com.revolgenx.anilib.field.staff.StaffMediaCharacterField
import com.revolgenx.anilib.service.StaffService
import com.revolgenx.anilib.source.StaffMediaCharacterSource

class StaffMediaCharacterViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaCharacterSource, StaffMediaCharacterField>() {
    override fun createSource(field: StaffMediaCharacterField): StaffMediaCharacterSource {
        source = StaffMediaCharacterSource(field, staffService, compositeDisposable)
        return source!!
    }
}
