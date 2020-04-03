package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.field.search.MediaSearchField
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.service.AdvanceSearchService
import com.revolgenx.anilib.source.AdvanceSearchSource
import com.revolgenx.anilib.type.MediaSort

class AdvanceSearchFragmentViewModel(private val advanceSearchService: AdvanceSearchService) :
    SourceViewModel<AdvanceSearchSource, BaseAdvanceSearchField>() {

    var field: BaseAdvanceSearchField = MediaSearchField().also {
        it.sort = MediaSort.TRENDING_DESC.ordinal
    }

    override fun createSource(field: BaseAdvanceSearchField): AdvanceSearchSource {
        source = AdvanceSearchSource(field, advanceSearchService, compositeDisposable)
        return source!!
    }

}
