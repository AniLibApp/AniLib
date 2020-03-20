package com.revolgenx.anilib.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.constant.PAGE_SIZE
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class SeasonSource(
    private val pageSize: Int = PAGE_SIZE,
    private val baseGraphRepository: BaseGraphRepository,
    private val seasonField: SeasonField,
    private val compositeDisposable: CompositeDisposable? = null
) : MainSource<CommonMediaModel>() {

    override fun areItemsTheSame(first: CommonMediaModel, second: CommonMediaModel): Boolean =
        first.mediaId == second.mediaId

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        var key = 1
        if (page.isFirstPage()) {
            setKey(page, 1)
        } else {
            key = getKey<Int>(page.previous()!!)!!.plus(1)
            setKey(page, key)
        }


        seasonField.also {
            it.page = key
            it.perPage = pageSize
        }

        val disposable = baseGraphRepository.request(seasonField.toQueryOrMutation())
            .map {
                it.data()?.Page()?.media()!!.map { media ->
                    media.fragments().narrowMediaContent().getCommonMedia()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postResult(page, it)
            }, {
                postResult(page, Exception(it))
            })

        compositeDisposable?.add(disposable)
    }

}