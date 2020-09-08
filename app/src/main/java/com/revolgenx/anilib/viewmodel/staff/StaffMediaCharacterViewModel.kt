package com.revolgenx.anilib.viewmodel.staff

import com.revolgenx.anilib.field.staff.StaffMediaCharacterField
import com.revolgenx.anilib.service.staff.StaffService
import com.revolgenx.anilib.source.StaffMediaCharacterSource
import com.revolgenx.anilib.viewmodel.SourceViewModel

class StaffMediaCharacterViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaCharacterSource, StaffMediaCharacterField>() {

    override var field: StaffMediaCharacterField = StaffMediaCharacterField()

    override fun createSource(): StaffMediaCharacterSource {
        source = StaffMediaCharacterSource(field, staffService, compositeDisposable)
        return source!!
    }
}
