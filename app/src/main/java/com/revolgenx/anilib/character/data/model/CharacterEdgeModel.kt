package com.revolgenx.anilib.character.data.model

import com.revolgenx.anilib.constant.AlCharacterRole
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.staff.data.model.StaffRoleType

class CharacterEdgeModel {
    //    The order the character should be displayed from the users favourites
    var favouriteOrder: Int? = null

    //    The id of the connection
    var id: Int? = null

    var media: List<MediaModel>? = null
    //The media the character is in

    var name: String? = null
    //Media specific character name

    var node: CharacterModel? = null

    //The characters role in the media
    var role: Int? = null

    //    The voice actors of the character with role date
    var voiceActorRoles: List<StaffRoleType>? = null

    var voiceActors: List<StaffModel>? = null
}