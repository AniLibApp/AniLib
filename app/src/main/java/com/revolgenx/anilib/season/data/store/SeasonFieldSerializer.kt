package com.revolgenx.anilib.season.data.store

import com.revolgenx.anilib.media.data.store.MediaFieldSerializer

class SeasonFieldSerializer : MediaFieldSerializer() {
    override val defaultValue = SeasonFieldData.default()
}