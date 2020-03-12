package com.revolgenx.anilib.model

class MediaReviewModel:BaseMediaModel() {
    var userId:Int? = null
    var summary:String? = null
    var userRating:Int? = null
    var rating:Int? = null
    var ratingAmount:Int? = null
    var user:MediaReviewUserModel? = null
}
