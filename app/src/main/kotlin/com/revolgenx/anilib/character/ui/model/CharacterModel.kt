package com.revolgenx.anilib.character.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.CharacterQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.CharacterImage
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale


data class CharacterModel(
    val id: Int = -1,
    val bloodType: String? = null,
    val dateOfBirth: FuzzyDateModel? = null,
    val age: String? = null,
    val gender: String? = null,

    val name: CharacterNameModel? = null,
    val isFavourite: MutableState<Boolean> = mutableStateOf(false),
    // If the character is blocked from being added to favourites
    val isFavouriteBlocked: Boolean = false,
    val media: MediaConnectionModel? = null,

    val siteUrl: String? = null,
    val favourites: Int? = null,
    val description: String? = null,
    val image: CharacterImageModel? = null,

    var spannedDescription: Spanned? = null,
) : BaseModel(id){
    fun generalDescription(): String {
        var generalInfo = ""
        dateOfBirth?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>Birthday:</b> $month $day, $year \n"
        }
        age?.let {
            generalInfo += "<b>Age:</b> $it \n"
        }
        gender?.let {
            generalInfo += "<b>Gender:</b> $it \n"
        }
        bloodType?.let {
            generalInfo += "<b>Blood Type:</b> $it \n"
        }

        if (generalInfo.isNotBlank()) {
            generalInfo += " \n"
        }
        return generalInfo
    }
}


fun CharacterImage.toModel() = CharacterImageModel(medium, large)

fun CharacterQuery.Character.toModel(): CharacterModel {
    val model = CharacterModel(
        id = id,
        isFavourite = mutableStateOf(isFavourite),
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
    model.spannedDescription =
        markdown.toMarkdown(model.generalDescription() + anilify(model.description))

    return model
}