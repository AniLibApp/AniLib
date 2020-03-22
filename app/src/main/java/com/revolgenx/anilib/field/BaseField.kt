package com.revolgenx.anilib.field

interface BaseField<T> {

    fun toQueryOrMutation(): T

    companion object {
        const val MEDIA_ID_KEY = "media_id_key"
        const val MEDIA_COVER_URL_KEY = "media_cover_url_key"
        const val MEDIA_BANNER_URL_KEY = "media_banner_url_key"

        const val PER_PAGE = 10
    }
}
