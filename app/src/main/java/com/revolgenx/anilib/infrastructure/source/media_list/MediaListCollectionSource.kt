package com.revolgenx.anilib.infrastructure.source.media_list

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.list.MediaListService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import com.revolgenx.anilib.ui.sorting.MediaListSorting
import com.revolgenx.anilib.ui.sorting.makeMediaListSortingComparator
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaListCollectionSource(
    field: MediaListCollectionField,
    private val listMap: MutableMap<Int, AlMediaListModel>,
    private val mediaListService: MediaListService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<AlMediaListModel, MediaListCollectionField>(field) {

    private lateinit var firstPage: Page

    override fun areItemsTheSame(first: AlMediaListModel, second: AlMediaListModel): Boolean {
        return first.id == second.id
    }

    fun filterPage() {
        if (::firstPage.isInitialized){
            postFilteredResult(firstPage)
        }
    }

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            firstPage = page

            if (listMap.isEmpty()) {
                mediaListService.getMediaListCollection(field, compositeDisposable) {
                    if (it.status == Status.SUCCESS) {
                        (it.data as List<AlMediaListModel>).forEach {
                            listMap[it.mediaId!!] = it
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            val filteredList = getFilteredList()
                            launch(Dispatchers.Main) {
                                postResult(page, filteredList)
                            }
                        }
                    }else{
                        postResult(page, it)
                    }
                }
            }
        } else {
            postResult(page, emptyList<AlMediaListModel>())
        }
    }

    private fun postFilteredResult(page:Page){
        CoroutineScope(Dispatchers.IO).launch {
            val filteredList = getFilteredList()
            launch(Dispatchers.Main) {
                postResult(page, filteredList)
            }
        }
    }

    private fun getFilteredList(): MutableList<AlMediaListModel> {
        val filter = field.filter

        return if (filter.formatsIn.isNullOrEmpty()) listMap.values else {
            listMap.values.filter {  filter.formatsIn!!.contains(it.format) }
        }.let {
            if (field.filter.status == null) it else it.filter { it.status == filter.status }
        }.let {
            if (filter.genre == null) it else it.filter { it.genres?.contains(filter.genre!!) == true }
        }.let {
            if (filter.search.isNullOrEmpty()) it else {
                it.filter { model->
                    model.title!!.romaji?.contains(filter.search!!, true) == true ||
                            model.title!!.english?.contains(filter.search!!, true) == true ||
                            model.title!!.native?.contains(filter.search!!, true)  == true||
                            model.synonyms?.any {  it.contains(filter.search!!, true)} == true
                }
            }
        }.let {
            if (filter.listSort == null) it else it.sortedWith(
                makeMediaListSortingComparator(
                    MediaListSorting.MediaListSortingType.values()[filter.listSort!!]
                )
            )
        }.toMutableList()
    }

}
