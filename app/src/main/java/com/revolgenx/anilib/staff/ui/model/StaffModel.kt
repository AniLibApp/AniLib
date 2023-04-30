package com.revolgenx.anilib.staff.ui.model

import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.character.ui.model.CharacterConnectionModel
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.StaffImage
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel


data class StaffModel(
    val id: Int = -1,
    val name: StaffNameModel? = null,
    val image: StaffImageModel? = null,
    val description: String? = null,
    val favourites: Int? = null,
    val languageV2: String? = null,
    val isFavourite: Boolean = false,
    val siteUrl: String? = null,
    //    The person's age in years
    val age: Int? = null,
    //    The persons blood type
    val bloodType: String? = null,
    //    Media the actor voiced characters in. (Same data as characters with media as node instead of characters)
    val characterMedia: MediaConnectionModel? = null,
    //    Characters voiced by the actor
    val characters: CharacterConnectionModel? = null,
    val dateOfBirth: FuzzyDateModel? = null,
    val dateOfDeath: FuzzyDateModel? = null,
    //    The staff's gender. Usually Male, Female, or Non-binary but can be any string.
    val gender: String? = null,
    //    The persons birthplace or hometown
    val homeTown: String? = null,
    //    If the staff member is blocked from being added to favourites
    val isFavouriteBlocked: Boolean = false,
    //    The person's primary occupations
    val primaryOccupations: List<String>? = null,
    //    Media where the staff member has a production role
    val staffMedia: MediaConnectionModel? = null,
    val yearsActive: List<Int>? = null,
) : BaseModel(id)


fun StaffQuery.Staff.toModel(): StaffModel {
    return StaffModel(
        id = id,
        name = name?.let {
            StaffNameModel(it.full, it.native, it.alternative?.filterNotNull())
        },
        image = image?.staffImage?.toModel(),
        languageV2 = languageV2,
        favourites = favourites,
        isFavourite = isFavourite,
        siteUrl = siteUrl,
        description = description,
        age = age,
        bloodType = bloodType,
        homeTown = homeTown,
        primaryOccupations = primaryOccupations?.filterNotNull(),
        gender = gender,
        dateOfBirth = dateOfBirth?.fuzzyDate?.toModel(),
        dateOfDeath = dateOfDeath?.fuzzyDate?.toModel(),
        yearsActive = yearsActive?.filterNotNull()
    )
}