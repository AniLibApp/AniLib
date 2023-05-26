package com.revolgenx.anilib.media.ui.model

import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffRoleType
import com.revolgenx.anilib.type.MediaRelation

data class MediaEdgeModel(
    val characterName: String? = null,
    val characterRole: Int? = null,
    //The characters in the media voiced by the parent actor
    val characters: List<CharacterModel>? = null,
    //Used for grouping roles where multiple dubs exist for the same language. Either dubbing company name or language valiant.
    val dubGroup: String? = null,
    //The order the media should be displayed from the users favourites
    val favouriteOrder: Int? = null,
    val id: Int? = null,
    //    If the studio is the main animation studio of the media (For Studio->MediaConnection field only)
    val isMainStudio: Boolean = false,
    val node: MediaModel? = null,
    //    The type of relation to the parent model
    val relationType: MediaRelation? = null,
    //    Notes regarding the VA's role for the character
    val roleNotes: String? = null,
    //    The role of the staff member in the production of the media
    val staffRole: String? = null,
    // The voice actors of the character with role date
    val voiceActorRoles: List<StaffRoleType>? = null,
    //    The voice actors of the character
    val voiceActors: List<StaffModel>? = null
)