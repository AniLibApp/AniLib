package com.revolgenx.anilib.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.TagField

class AdvanceSearchViewModel : ViewModel() {
    var genreTagFields: List<TagField>? = null
    var tagTagFields: List<TagField>? = null
    var streamTagFields: List<TagField>? = null
}
