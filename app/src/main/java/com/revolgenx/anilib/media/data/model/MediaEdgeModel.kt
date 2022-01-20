package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.constant.AlCharacterRole
import com.revolgenx.anilib.constant.AlMediaRelation
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffRoleType

class MediaEdgeModel {
    var characterName: String? = null
    var characterRole: Int? = null

    //The characters in the media voiced by the parent actor
    var characters: List<CharacterModel>? = null

    //Used for grouping roles where multiple dubs exist for the same language. Either dubbing company name or language variant.
    var dubGroup: String? = null

    //The order the media should be displayed from the users favourites
    var favouriteOrder: Int? = null

    var id: Int? = null

    //    If the studio is the main animation studio of the media (For Studio->MediaConnection field only)
    var isMainStudio: Boolean = false

    var node: MediaModel? = null

    //    The type of relation to the parent model
    var relationType: AlMediaRelation? = null

    //    Notes regarding the VA's role for the character
    var roleNotes: String? = null

    //    The role of the staff member in the production of the media
    var staffRole: String? = null

    // The voice actors of the character with role date
    var voiceActorRoles: List<StaffRoleType>? = null

    //    The voice actors of the character
    var voiceActors: List<StaffModel>? = null
}
