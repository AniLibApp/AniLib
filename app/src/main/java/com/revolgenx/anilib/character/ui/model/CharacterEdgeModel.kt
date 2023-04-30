package com.revolgenx.anilib.character.ui.model

import com.revolgenx.anilib.MediaCharacterQuery
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.StaffNameModel
import com.revolgenx.anilib.staff.ui.model.StaffRoleType
import com.revolgenx.anilib.staff.ui.model.toModel
import com.revolgenx.anilib.type.CharacterRole

data class CharacterEdgeModel(
    //    The order the character should be displayed from the users favourites
    val favouriteOrder: Int? = null,
    //    The id of the connection
    val id: Int = -1,
    val media: List<MediaModel>? = null,
    //The media the character is in
    val name: String? = null,
    //Media specific character name
    val node: CharacterModel? = null,
    //The characters role in the media
    val role: CharacterRole? = null,
    //    The voice actors of the character with role date
    val voiceActorRoles: List<StaffRoleType>? = null,
    val voiceActors: List<StaffModel>? = null
) : BaseModel(id)

fun MediaCharacterQuery.Edge.toModel() = CharacterEdgeModel(
    id = id ?: -1,
    role = role,
    voiceActors = voiceActors?.mapNotNull { v ->
        v?.let {
            StaffModel(
                id = it.id,
                name = it.name?.let { name ->
                    StaffNameModel(name.full)
                },
                languageV2 = it.languageV2,
                image = it.image?.staffImage?.toModel()
            )
        }
    },
    node = node?.let { node ->
        CharacterModel(
            id = node.id,
            name = node.name?.let { CharacterNameModel(full = it.full) },
            image = node.image?.characterImage?.toModel(),
            siteUrl = node.siteUrl
        )

    }

)