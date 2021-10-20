package com.revolgenx.anilib.ui.viewmodel.setting.filter

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.setting.filter.EditTagField
import com.revolgenx.anilib.data.meta.TagFilterMetaType
import com.revolgenx.anilib.util.TagPrefUtil
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class EditTagFilterViewModel : BaseViewModel() {
    val tagFields: MutableList<EditTagField> = mutableListOf()
    var editMode = TagEditMode.DEFAULT
    var tagFilterType:TagFilterMetaType = TagFilterMetaType.TAG

    lateinit var filteredTagFields:List<EditTagField>


    fun search(query:String){
        filteredTagFields = tagFields.filter { it.tag.contains(query, true) }.sortedBy { it.tag }.sortedBy { !it.isExcluded }
    }


    fun updateField(context: Context) {
        tagFields.clear()
        val tags = when (tagFilterType) {
            TagFilterMetaType.GENRE -> {
                val excludedTags = TagPrefUtil.getExcludedPrefGenre(context)
                val userPrefGenres = TagPrefUtil.getUserPrefGenres(context)
                userPrefGenres.map { EditTagField(false, excludedTags.contains(it),it) }

            }
            TagFilterMetaType.TAG -> {
                val excludedTags = TagPrefUtil.getExcludedPrefTags(context)
                val userPrefTags = TagPrefUtil.getUserPrefTags(context)
                userPrefTags.map { EditTagField(false, excludedTags.contains(it),it) }
            }
            TagFilterMetaType.STREAMING_ON -> {
                val streamingOnTags = TagPrefUtil.getUserPrefStreamingOn(context)
                streamingOnTags.map { EditTagField(false, false, it) }
            }
        }
        tagFields.addAll(tags)
        search("")
    }

    fun reloadTagFields(context: Context) {
        tagFields.clear()
        val tags = when (tagFilterType) {
            TagFilterMetaType.GENRE -> {
                context.resources.getStringArray(R.array.media_genre).toList()
            }
            TagFilterMetaType.TAG -> {
                context.resources.getStringArray(R.array.media_tags).toList()
            }
            TagFilterMetaType.STREAMING_ON -> {
                context.resources.getStringArray(R.array.streaming_on).toList()
            }
        }
        tagFields.addAll(tags.map { EditTagField(false, false, it) })
    }

    fun removeTagFields() {
        tagFields.removeAll { it.isSelected }
    }

    fun saveTagFields(context: Context) {
        when(tagFilterType){
            TagFilterMetaType.GENRE -> {
                TagPrefUtil.saveExcludedGenre(context, tagFields.filter { it.isExcluded }.map { it.tag })
                TagPrefUtil.saveGenrePref(context, tagFields.map { it.tag })
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.saveExcludedTags(context, tagFields.filter { it.isExcluded }.map { it.tag })
                TagPrefUtil.saveTagPref(context, tagFields.map { it.tag })
            }
            TagFilterMetaType.STREAMING_ON -> {
                TagPrefUtil.saveStreamingOnPref(context, tagFields.map { it.tag })
            }
        }
        TagPrefUtil.invalidateAll(context)
    }

    fun saveExcludedTags(context: Context){
        when(tagFilterType){
            TagFilterMetaType.GENRE -> {
                TagPrefUtil.saveExcludedGenre(context, tagFields.filter { it.isSelected || it.isExcluded}.map {
                    it.isExcluded = true
                    it.tag
                })
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.saveExcludedTags(context, tagFields.filter { it.isSelected  || it.isExcluded }.map {
                    it.isExcluded = true
                    it.tag
                })
            }
        }
    }

    fun saveNotExcludedTags(context: Context){
        when(tagFilterType){
            TagFilterMetaType.GENRE -> {
                tagFields.filter { it.isSelected }.forEach { it.isExcluded = false }
                TagPrefUtil.saveExcludedGenre(context, tagFields.filter { it.isExcluded }.map {
                    it.tag
                })
            }
            TagFilterMetaType.TAG -> {
                tagFields.filter { it.isSelected }.forEach { it.isExcluded = false }
                TagPrefUtil.saveExcludedTags(context, tagFields.filter { it.isExcluded }.map {it.tag })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tagFields.clear()
        editMode = TagEditMode.DEFAULT
    }

}

enum class TagEditMode {
    ADD, RELOAD, DELETE, DEFAULT, EXCLUDED
}