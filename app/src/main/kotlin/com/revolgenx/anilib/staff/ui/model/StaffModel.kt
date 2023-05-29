package com.revolgenx.anilib.staff.ui.model

import android.text.Spanned
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.character.ui.model.CharacterConnectionModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.StaffImage
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale


data class StaffModel(
    val id: Int = -1,
    val name: StaffNameModel? = null,
    val image: StaffImageModel? = null,
    val description: String? = null,
    val favourites: Int? = null,
    val languageV2: String? = null,
    val isFavourite: MutableState<Boolean> = mutableStateOf(false),
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

    var spannedDescription: Spanned? = null,
) : BaseModel(id) {
    fun generalDescription(): String {
        var generalInfo = ""
        dateOfBirth?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>Birth:</b> $month $day, $year \n"
        }
        dateOfDeath?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>Death:</b> $month $day, $year \n"
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
        yearsActive?.let {
            val firstDate = it.getOrNull(0).naText()
            val lastDate = it.getOrNull(1) ?: "Present"
            generalInfo += "<b>Years active:</b> $firstDate-$lastDate \n"
        }

        homeTown?.let {
            generalInfo += "<b>Hometown:</b> $it \n"
        }

        if (generalInfo.isNotBlank()) {
            generalInfo += " \n"
        }
        return generalInfo
    }
}


fun StaffQuery.Staff.toModel(): StaffModel {
    val model = StaffModel(
        id = id,
        name = name?.let {
            StaffNameModel(it.full, it.native, it.alternative?.filterNotNull())
        },
        image = image?.staffImage?.toModel(),
        languageV2 = languageV2,
        favourites = favourites,
        isFavourite = mutableStateOf(isFavourite),
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

    model.spannedDescription =
        markdown.toMarkdown(model.generalDescription() + anilify(model.description))
    return model
}