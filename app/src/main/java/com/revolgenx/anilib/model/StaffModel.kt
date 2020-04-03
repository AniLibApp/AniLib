package com.revolgenx.anilib.model

open class StaffModel : BaseModel() {
    var staffId: Int? = null
    var staffName: StaffNameModel? = null
    var staffImage: StaffImageModel? = null
    var description: String? = null
    var favourites: Int? = null
    var language: Int? = null
    var isFavourite = false
    var siteUrl: String? = null
}
