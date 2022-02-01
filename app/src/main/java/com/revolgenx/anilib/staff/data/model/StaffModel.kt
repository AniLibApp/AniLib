package com.revolgenx.anilib.staff.data.model

import com.revolgenx.anilib.character.data.model.CharacterConnectionModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.fragment.StaffImage
import com.revolgenx.anilib.media.data.model.MediaConnectionModel


class StaffModel:BaseModel() {
    var name: StaffNameModel? = null
    var image: StaffImageModel? = null
    var description: String? = null
    var favourites: Int? = null

    var languageV2: String? = null
    var isFavourite = false
    var siteUrl: String? = null

    //    The person's age in years
    var age: Int? = null

    //    The persons blood type
    var bloodType: String? = null

    //    Media the actor voiced characters in. (Same data as characters with media as node instead of characters)
    var characterMedia: MediaConnectionModel? = null

    //    Characters voiced by the actor
    var characters: CharacterConnectionModel? = null

    var dateOfBirth: FuzzyDateModel? = null
    var dateOfDeath: FuzzyDateModel? = null

    //    The staff's gender. Usually Male, Female, or Non-binary but can be any string.
    var gender: String? = null

    //    The persons birthplace or hometown
    var homeTown: String? = null

    //    If the staff member is blocked from being added to favourites
    var isFavouriteBlocked: Boolean = false

    //    The person's primary occupations
    var primaryOccupations: List<String>? = null

    //    Media where the staff member has a production role
    var staffMedia: MediaConnectionModel? = null

    var yearsActive: List<Int>? = null
}


fun StaffImage.toModel() = StaffImageModel(medium, large)