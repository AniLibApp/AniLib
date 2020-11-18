package com.revolgenx.anilib.ui.viewmodel.browse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.infrastructure.repository.util.Resource

class BrowseActivityViewModel : ViewModel() {
    var searchQuery: String = ""
    var searchLiveData = MutableLiveData<Resource<SearchData>>()
    var genreTagFields: MutableList<TagField> = mutableListOf()
    var tagTagFields: MutableList<TagField> = mutableListOf()
    var tagIncludeTagFields: MutableList<TagField> = mutableListOf()
    var tagExcludeTagFields: MutableList<TagField> = mutableListOf()
    var genreIncludeTagFields: MutableList<TagField> = mutableListOf()
    var genreExcludeTagFields: MutableList<TagField> = mutableListOf()
    var streamTagFields: MutableList<TagField> = mutableListOf()

    fun searchNow() {
        searchLiveData.value =
            Resource.success(
                SearchData(
                    searchNow = true
                )
            )
    }

    override fun onCleared() {
        genreTagFields.clear()
        tagTagFields.clear()
        streamTagFields.clear()
        super.onCleared()
    }

    companion object {
        data class SearchData(var searchNow: Boolean)
    }
}
