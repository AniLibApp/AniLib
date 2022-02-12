package com.revolgenx.anilib.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.list.constant.ListConstant
import com.revolgenx.anilib.list.data.model.MediaListGroupModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

abstract class MediaListCollectionStoreVM : ViewModel() {
    var lists: MutableList<MediaListGroupModel>? = null //reference it
    val dataSetChangeLiveData = MutableLiveData<String?>() //group name

    fun update(item: MediaListModel) {
        val storedLists = lists ?: return

        val customListKeys =
            item.customLists?.filter { it.second }?.map { it.first to false }?.toMutableList()
                ?: mutableListOf()
        val statusToDefaultGroup = ListConstant.statusToDefaultGroup(
            MediaListStatus.values()[item.status!!],
            MediaType.values()[item.media!!.type!!]
        )
        customListKeys.add(statusToDefaultGroup to true)

        customListKeys.forEach { key ->
            if (key.second && item.hiddenFromStatusLists) {
                return@forEach
            }

            val customList = storedLists.firstOrNull { key.first == it.name }
            if (customList == null) {
                storedLists.add(MediaListGroupModel().also { model ->
                    model.name = key.first
                    model.isCustomList = true
                    //TODO
                    //model.isCompletedList
                    model.entries = mutableListOf(item)
                })
            } else {
                val entry = customList.entries?.indexOfFirst { item.id == it.id }
                if (entry == -1 || entry == null) {
                    if (customList.entries.isNullOrEmpty()) {
                        customList.entries = mutableListOf(item)
                    } else {
                        customList.entries!!.add(item)
                    }
                } else {
                    customList.entries?.set(entry, item)
                }
            }
        }
        notifyDataChange(customListKeys.map { it.first })
    }

    fun delete(item: MediaListModel) {
        val storedLists = lists ?: return
        val customListKeys =
            item.customLists?.mapNotNull { it.first.takeIf { _ -> it.second } }?.toMutableList()
                ?: mutableListOf()
        val statusToDefaultGroup = ListConstant.statusToDefaultGroup(
            MediaListStatus.values()[item.status!!],
            MediaType.values()[item.media!!.type!!]
        )
        customListKeys.add(statusToDefaultGroup)
        storedLists.filter { customListKeys.contains(it.name) }.forEach {
            it.entries?.removeAll { it.id == item.id }
        }
        notifyDataChange(customListKeys)
    }

    private fun notifyDataChange(list: List<String>) {
        list.forEach {
            dataSetChangeLiveData.value = it
        }
        dataSetChangeLiveData.value = null
    }
}