package com.revolgenx.anilib.staff.ui.model

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.StaffQuery
import com.revolgenx.anilib.character.ui.model.CharacterConnectionModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.common.ui.model.toModel
import com.revolgenx.anilib.fragment.SmallStaff
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
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
) : BaseModel {
    fun generalDescription(context: Context): String {
        var generalInfo = ""
        dateOfBirth?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.birth)}:</b> $month $day, $year \n"
        }
        dateOfDeath?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.death)}:</b> $month $day, $year \n"
        }
        age?.let {
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.age)}:</b> $it \n"
        }
        gender?.let {
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.gender)}:</b> $it \n"
        }
        bloodType?.let {
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.blood_type)}:</b> $it \n"
        }
        yearsActive?.takeIf { it.isNotEmpty() }?.let {
            val firstDate = it.getOrNull(0).naText()
            val lastDate = it.getOrNull(1) ?: context.getString(anilib.i18n.R.string.present)
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.years_active)}:</b> $firstDate-$lastDate \n"
        }

        homeTown?.let {
            generalInfo += "<b>${context.getString(anilib.i18n.R.string.hometown)}:</b> $it \n"
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

    return model
}

fun SmallStaff.toModel() = StaffModel(
    id = id,
    name = name?.let { name ->
        StaffNameModel(name.full)
    },
    image = image?.staffImage?.toModel()
)