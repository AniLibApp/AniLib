package com.revolgenx.anilib.common.data.model

import com.revolgenx.anilib.fragment.PageInfo

data class PageModel<M>(val pageInfo: PageInfo?, val data: List<M>?)