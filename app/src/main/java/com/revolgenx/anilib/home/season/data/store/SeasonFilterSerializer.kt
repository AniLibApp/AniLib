package com.revolgenx.anilib.home.season.data.store

import com.revolgenx.anilib.media.data.filter.MediaFilterSerializer

class SeasonFilterSerializer : MediaFilterSerializer() {
    override val defaultValue = SeasonFieldData.default()
}