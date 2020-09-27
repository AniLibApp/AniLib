package com.revolgenx.anilib.viewmodel.setting

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.field.TagState
import com.revolgenx.anilib.meta.TagFilterMetaType
import com.revolgenx.anilib.util.TagPrefUtil
import com.revolgenx.anilib.viewmodel.BaseViewModel

class TagFilterSettingDialogViewModel : BaseViewModel() {
    val tagFields: MutableList<TagField> = mutableListOf()
    var editMode = TagEditMode.DEFAULT
    var tagFilterType:TagFilterMetaType = TagFilterMetaType.TAG


    fun updateField(context: Context) {
        tagFields.clear()
        val tags = when (tagFilterType) {
            TagFilterMetaType.GENRE -> {
                TagPrefUtil.getUserPrefGenres(context)
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.getUserPrefTags(context)
            }
            TagFilterMetaType.STREAMING_ON -> {
                TagPrefUtil.getUserPrefStreamingOn(context)
            }
        }

        tagFields.addAll(tags.map { TagField(it, TagState.EMPTY) })
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
        tagFields.addAll(tags.map { TagField(it, TagState.EMPTY) })
    }

    fun removeTagFields() {
        tagFields.removeAll { it.tagState == TagState.TAGGED }
    }

    fun saveTagFields(context: Context) {
        when(tagFilterType){
            TagFilterMetaType.GENRE -> {
                TagPrefUtil.saveGenrePref(context, tagFields.map { it.tag })
            }
            TagFilterMetaType.TAG -> {
                TagPrefUtil.saveTagPref(context, tagFields.map { it.tag })
            }
            TagFilterMetaType.STREAMING_ON -> {
                TagPrefUtil.saveStreamingOnPref(context, tagFields.map { it.tag })
            }
        }
        TagPrefUtil.invalidateAll(context)
    }

    override fun onCleared() {
        super.onCleared()
        tagFields.clear()
        editMode = TagEditMode.DEFAULT
    }

}

enum class TagEditMode {
    ADD, RELOAD, DELETE, DEFAULT
}