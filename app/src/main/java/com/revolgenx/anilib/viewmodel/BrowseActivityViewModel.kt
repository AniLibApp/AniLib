package com.revolgenx.anilib.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.repository.util.Resource

class BrowseActivityViewModel : ViewModel() {
    var searchQuery: String = ""
    var searchLiveData = MutableLiveData<Resource<SearchData>>()
    var genreTagFields: MutableList<TagField>? = null
    var tagTagFields: MutableList<TagField>? = null
    var streamTagFields: MutableList<TagField>? = null

    private val handler = Handler()

    fun searchNow() {
        searchLiveData.value =
            Resource.success(SearchData(searchNow = true, searchLate = false))
    }

    fun searchLate() {
        clearHandler()
        searchLiveData.value = Resource.loading(null)
        handler.postDelayed({
            searchLiveData.value =
                Resource.success(SearchData(searchNow = false, searchLate = true))
        }, SEARCH_DELAY_TIME)
    }

    override fun onCleared() {
        clearHandler()
        super.onCleared()
    }

    fun clearHandler() {
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val SEARCH_DELAY_TIME = 2000L
        data class SearchData(var searchNow: Boolean, var searchLate: Boolean)
    }
}
