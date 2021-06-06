package com.revolgenx.anilib.infrastructure.source


import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.data.field.media.MediaWatchField
import com.revolgenx.anilib.data.model.media_info.MediaWatchModel
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.infrastructure.service.media.MediaBrowseService
import io.reactivex.disposables.CompositeDisposable

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
                    else -> {}
                }
            }
        } else {
            postResult(page, emptyList<MediaWatchModel>())
        }
    }
}
