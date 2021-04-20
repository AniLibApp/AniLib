package com.revolgenx.anilib.util

import android.content.Context
import com.revolgenx.anilib.common.preference.getAllSearchHistories
import com.revolgenx.anilib.common.preference.saveSearchHistories

class DataProvider(private val context: Context) {
    private val searchHistories = mutableListOf<String>()

    private var dataChangeListener: (() -> Unit)? = null

    fun getAllHistory(): List<String> {
        if (searchHistories.isEmpty()) {

            searchHistories.addAll(context.getAllSearchHistories())
        }
        return searchHistories
    }

    fun addToSearchHistory(query: String) {
        searchHistories.remove(query)
        searchHistories.add(0, query)
        updateSearchHistories()
        dataChangeListener?.invoke()
    }

    fun removeFromSearchHistory(query: String) {
        searchHistories.remove(query)
        updateSearchHistories()
    }

    fun onDataChanged(callback: () -> Unit) {
        dataChangeListener = callback
    }

    private fun updateSearchHistories() {
        context.saveSearchHistories(searchHistories.joinToString(separator = ","))
    }
}