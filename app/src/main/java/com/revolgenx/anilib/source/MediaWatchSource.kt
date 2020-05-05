package com.revolgenx.anilib.source


import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.field.media.MediaWatchField
import com.revolgenx.anilib.model.MediaWatchModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception

class MediaWatchSource(
    private val field: MediaWatchField,
    private val mediaBrowseService: MediaBrowseService,
    private val compositeDisposable: CompositeDisposable
) : MainSource<MediaWatchModel>() {
    override fun areItemsTheSame(first: MediaWatchModel, second: MediaWatchModel): Boolean {
        return first.url == second.url
    }
    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        if (page.isFirstPage()) {
            mediaBrowseService.getMediaWatch(field, compositeDisposable) { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        postResult(page, res.data ?: emptyList())
                    }
                    Status.ERROR -> {
                        postResult(page, Exception(res.message))
                    }
                }
            }
        } else {
            postResult(page, emptyList<MediaWatchModel>())
        }
    }
}
