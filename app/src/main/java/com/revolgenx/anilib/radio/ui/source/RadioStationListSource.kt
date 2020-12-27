package com.revolgenx.anilib.radio.ui.source

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.radio.repository.room.RadioStation

class RadioStationListSource() : Source<RadioStation>() {

    private var firstPage: Page? = null
    private var stations: List<RadioStation>? = null

    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        if (page.isFirstPage()) {
            firstPage = page
            if (stations != null) {
                postResult(page, stations!!)
            }
        }
    }

    fun submitList(stations: List<RadioStation>) {
        this.stations = stations
        if (firstPage != null) {
            postResult(firstPage!!, stations)
        }
    }

    override fun onPageClosed(page: Page) {
        super.onPageClosed(page)
        if (page.isFirstPage()) {
            stations?.forEach { it.playbackStateListener = null }
        }
    }

    override fun areItemsTheSame(first: RadioStation, second: RadioStation): Boolean {
        return first.id == second.id && first.isFavourite == second.isFavourite && first.playbackState == second.playbackState
    }

    override fun areContentsTheSame(first: RadioStation, second: RadioStation): Boolean {
        return first == second
    }
}

