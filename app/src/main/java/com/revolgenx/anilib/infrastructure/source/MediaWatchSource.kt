package com.revolgenx.anilib.infrastructure.source


import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.extensions.MainSource
import com.revolgenx.anilib.media.data.field.MediaWatchField
import com.revolgenx.anilib.media.data.model.MediaStreamingEpisodeModel
import com.revolgenx.anilib.common.repository.util.Status
import com.revolgenx.anilib.media.service.MediaInfoService
import io.reactivex.disposables.CompositeDisposable

class MediaWatchSource(
    private val field: MediaWatchField,
    private val mediaBrowseService: MediaInfoService,
    private val compositeDisposable: CompositeDisposable
) : MainSource<MediaStreamingEpisodeModel>() {
    override fun areItemsTheSame(first: MediaStreamingEpisodeModel, second: MediaStreamingEpisodeModel): Boolean {
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
            postResult(page, emptyList<MediaStreamingEpisodeModel>())
        }
    }
}
