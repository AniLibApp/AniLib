package com.revolgenx.anilib.source

import android.os.Handler
import android.os.Looper
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.SeasonListQuery
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.field.SeasonField
import com.revolgenx.anilib.repository.network.BaseGraphRepository
import com.revolgenx.anilib.repository.network.converter.getCommonMedia
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

class SeasonSource(
    private val pageSize: Int = 10,
    private val baseGraphRepository: BaseGraphRepository,
    private val seasonField: SeasonField
) : MainSource<CommonMediaModel>() {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun areItemsTheSame(first: CommonMediaModel, second: CommonMediaModel): Boolean =
        first.id == second.id

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        var key = 1
        if (page.isFirstPage()) {
            setKey(page, 1)
        } else {
            key = getKey<Int>(page.previous()!!)!!.plus(1)
            setKey(page, key)
        }


        val query = seasonField.toQuery(key, pageSize)
        val disposable = baseGraphRepository.request(query)
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

        compositeDisposable.add(disposable)
    }

}