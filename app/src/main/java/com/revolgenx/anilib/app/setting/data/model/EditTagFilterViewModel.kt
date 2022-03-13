package com.revolgenx.anilib.app.setting.data.model

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.setting.data.field.EditTagField
import com.revolgenx.anilib.app.setting.data.meta.TagEditMode
import com.revolgenx.anilib.app.setting.data.meta.TagFilterMetaType
import com.revolgenx.anilib.common.viewmodel.BaseViewModel
import com.revolgenx.anilib.util.TagPrefUtil

class EditTagFilterViewModel : BaseViewModel() {
    val tagFields: MutableList<EditTagField> = mutableListOf()
    var editMode = TagEditMode.DEFAULT
    var tagFilterType: TagFilterMetaType = TagFilterMetaType.TAG

    lateinit var filteredTagFields:List<EditTagField>


    fun search(query:String){
        filteredTagFields = tagFields.filter { it.tag.contains(query, true) }.sortedBy { it.tag }.sortedBy { !it.isExcluded }
    }


    fun updateField(context: Context) {
        tagFields.clear()
        val tags = when (tagFilterType) {
            TagFilterMetaType.GENRE -> {
                val excludedTags = TagPrefUtil.getExcludedPrefGenre()
                val userPrefGenres = TagPrefUtil.getUserPrefGenres()
                userPrefGenres.map { EditTagField(false, excludedTags.contains(it),it) }

            }
            TagFilterMetaType.TAG -> {
                val excludedTags = TagPrefUtil.getExcludedPrefTags()
                val userPrefTags = TagPrefUtil.getUserPrefTags()
                userPrefTags.map { EditTagField(false, excludedTags.contains(it),it) }
            }
            TagFilterMetaType.STREAMING_ON -> {
                val streamingOnTags = TagPrefUtil.getUserPrefStreamingOn()
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
                TagPrefUtil.saveExcludedGenre(tagFields.filter { it.isExcluded }.map { it.tag })
                TagPrefUtil.saveGenrePref(tagFields.map { it.tag })
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.saveExcludedTags(tagFields.filter { it.isExcluded }.map { it.tag })
                TagPrefUtil.saveTagPref(tagFields.map { it.tag })
            }
            TagFilterMetaType.STREAMING_ON -> {
                TagPrefUtil.saveStreamingOnPref(tagFields.map { it.tag })
            }
        }
        TagPrefUtil.invalidateAll()
    }

    fun saveExcludedTags(context: Context){
        when(tagFilterType){
            TagFilterMetaType.GENRE -> {
                TagPrefUtil.saveExcludedGenre(tagFields.filter { it.isSelected || it.isExcluded}.map {
                    it.isExcluded = true
                    it.tag
                })
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.saveExcludedTags(tagFields.filter { it.isSelected  || it.isExcluded }.map {
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
                TagPrefUtil.saveExcludedGenre(tagFields.filter { it.isExcluded }.map {
                    it.tag
                })
            }
            TagFilterMetaType.TAG -> {
                tagFields.filter { it.isSelected }.forEach { it.isExcluded = false }
                TagPrefUtil.saveExcludedTags(tagFields.filter { it.isExcluded }.map {it.tag })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tagFields.clear()
        editMode = TagEditMode.DEFAULT
    }

}

