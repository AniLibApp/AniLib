package com.revolgenx.anilib.staff.viewmodel

import com.revolgenx.anilib.staff.service.StaffService
import com.revolgenx.anilib.infrastructure.source.StaffMediaCharacterSource
import com.revolgenx.anilib.staff.data.field.StaffMediaCharacterField
import com.revolgenx.anilib.common.viewmodel.SourceViewModel

class StaffMediaCharacterViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaCharacterSource, StaffMediaCharacterField>() {

    override var field: StaffMediaCharacterField = StaffMediaCharacterField()

    override fun createSource(): StaffMediaCharacterSource {
        source = StaffMediaCharacterSource(field, staffService, compositeDisposable)
        return source!!
    }
}