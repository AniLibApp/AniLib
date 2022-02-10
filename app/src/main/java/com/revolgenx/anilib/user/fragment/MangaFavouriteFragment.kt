package com.revolgenx.anilib.user.fragment

import com.revolgenx.anilib.search.data.field.SearchTypes

class MangaFavouriteFragment : UserFavouriteFragment() {
    override val favouriteType: SearchTypes = SearchTypes.MANGA
}