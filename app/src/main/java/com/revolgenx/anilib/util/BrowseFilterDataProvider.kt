package com.revolgenx.anilib.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.preference.getBrowseFilterPreference
import com.revolgenx.anilib.preference.setBrowseFilterPreference

object BrowseFilterDataProvider {
    private var data: BrowseFilterModel? = null
    private val gson by lazy { Gson() }

    fun getBrowseFilterData(context: Context): BrowseFilterModel? {
        if (data != null) return data
        val filter =
            context.getBrowseFilterPreference()?.takeIf { it.isNotEmpty() } ?: return null

        data = try {
            when (gson.fromJson(filter, JsonObject::class.java).get("type").asInt) {
                BrowseTypes.ANIME.ordinal, BrowseTypes.MANGA.ordinal -> {
                    gson.fromJson(filter, MediaBrowseFilterModel::class.java)
                }
                BrowseTypes.CHARACTER.ordinal -> {
                    gson.fromJson(filter, CharacterBrowseFilterModel::class.java)
                }
                BrowseTypes.STAFF.ordinal -> {
                    gson.fromJson(filter, StaffBrowseFilterModel::class.java)
                }
                BrowseTypes.STUDIO.ordinal -> {
                    gson.fromJson(filter, StudioBrowseFilterModel::class.java)
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

    fun setBrowseFilterData(context: Context, filter: BrowseFilterModel) {
        data = filter
        context.setBrowseFilterPreference(gson.toJson(data))
    }
}