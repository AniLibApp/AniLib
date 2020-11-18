package com.revolgenx.anilib.ui.viewmodel.staff

import com.revolgenx.anilib.data.field.staff.StaffMediaCharacterField
import com.revolgenx.anilib.infrastructure.service.staff.StaffService
import com.revolgenx.anilib.infrastructure.source.StaffMediaCharacterSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class StaffMediaCharacterViewModel(private val staffService: StaffService) :
    SourceViewModel<StaffMediaCharacterSource, StaffMediaCharacterField>() {

    override var field: StaffMediaCharacterField = StaffMediaCharacterField()

    override fun createSource(): StaffMediaCharacterSource {
        source = StaffMediaCharacterSource(field, staffService, compositeDisposable)
        return source!!
    }
}
