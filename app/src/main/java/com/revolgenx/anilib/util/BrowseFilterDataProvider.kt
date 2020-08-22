package com.revolgenx.anilib.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.preference.getBrowseFilterPreference
import com.revolgenx.anilib.preference.setBrowseFilterPreference

object BrowseFilterDataProvider {
    private var data: SearchFilterModel? = null
    private val gson by lazy { Gson() }

    fun getBrowseFilterData(context: Context): SearchFilterModel? {
        if (data != null) return data
        val filter =
            context.getBrowseFilterPreference()?.takeIf { it.isNotEmpty() } ?: return null

        data = try {
            when (gson.fromJson(filter, JsonObject::class.java).get("type").asInt) {
                SearchTypes.ANIME.ordinal, SearchTypes.MANGA.ordinal -> {
                    gson.fromJson(filter, MediaSearchFilterModel::class.java)
                }
                SearchTypes.CHARACTER.ordinal -> {
                    gson.fromJson(filter, CharacterSearchFilterModel::class.java)
                }
                SearchTypes.STAFF.ordinal -> {
                    gson.fromJson(filter, StaffSearchFilterModel::class.java)
                }
                SearchTypes.STUDIO.ordinal -> {
                    gson.fromJson(filter, StudioSearchFilterModel::class.java)
                }
                SearchTypes.USER.ordinal->{
                    gson.fromJson(filter, UserSearchFilterModel::class.java)
                }
                else -> null
            }
        } catch (e: ClassCastException) {
            null
        } catch (e: IllegalStateException) {
            null
        }
        return data
    }

    fun setBrowseFilterData(context: Context, filter: SearchFilterModel) {
        data = filter
        context.setBrowseFilterPreference(gson.toJson(data))
    }
}