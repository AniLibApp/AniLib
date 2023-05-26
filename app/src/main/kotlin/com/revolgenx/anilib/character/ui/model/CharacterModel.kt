package com.revolgenx.anilib.character.ui.model

import com.revolgenx.anilib.CharacterQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.CharacterImage
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel


data class CharacterModel(
    val id: Int = -1,
    val bloodType: String? = null,
    val dateOfBirth: FuzzyDateModel? = null,
    val age: String? = null,
    val gender: String? = null,

    val name: CharacterNameModel? = null,
    val isFavourite: Boolean = false,
    // If the character is blocked from being added to favourites
    val isFavouriteBlocked: Boolean = false,
    val media: MediaConnectionModel? = null,

    val siteUrl: String? = null,
    val favourites: Int? = null,
    val description: String? = null,
    val image: CharacterImageModel? = null,
) : BaseModel(id)


fun CharacterImage.toModel() = CharacterImageModel(medium, large)

fun CharacterQuery.Character.toModel() = CharacterModel(
    id = id,
    isFavourite = isFavourite,
    description = description,
    siteUrl = siteUrl,
    favourites = favourites,
    age = age,
    bloodType = bloodType,
    dateOfBirth = dateOfBirth?.fuzzyDate?.toModel(),
    gender = gender,
    name = name?.let {
        CharacterNameModel(
            full = it.full,
            native = it.native,
            alternative = it.alternative?.filterNotNull()
        )
    },
    image = image?.characterImage?.toModel()
)