package com.revolgenx.anilib.data.model.staff

import com.revolgenx.anilib.data.model.BaseModel

open class StaffModel : BaseModel() {
    var name: StaffNameModel? = null
    var image: StaffImageModel? = null
    var description: String? = null
    var favourites: Int? = null
    var language: Int? = null
    var isFavourite = false
    var siteUrl: String? = null
}
