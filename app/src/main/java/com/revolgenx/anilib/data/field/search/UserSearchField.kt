package com.revolgenx.anilib.data.field.search

import com.revolgenx.anilib.UserSearchQuery

class UserSearchField : SearchField() {
    override val type: Int = com.revolgenx.anilib.constant.SearchTypes.USER.ordinal

    override fun toQueryOrMutation(): Any {
        return UserSearchQuery.builder()
            .page(page)
            .perPage(perPage)
            .search(query)
            .build()
    }

}