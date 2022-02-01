package com.revolgenx.anilib.search.data.field

import com.revolgenx.anilib.UserSearchQuery

class UserSearchField : SearchField() {
    override val type: Int = com.revolgenx.anilib.constant.SearchTypes.USER.ordinal

    override fun toQueryOrMutation(): Any {
        return UserSearchQuery(page = nn(page), perPage = nn(perPage), search = nn(query))
    }

}