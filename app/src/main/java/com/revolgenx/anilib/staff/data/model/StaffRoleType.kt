package com.revolgenx.anilib.staff.data.model

class StaffRoleType {

    //    Used for grouping roles where multiple dubs exist for the same language. Either dubbing company name or language variant.
    var dubGroup: String? = null

    //    Notes regarding the VA's role for the character
    var roleNotes: String? = null

    //    The voice actors of the character
    var voiceActor: StaffModel? = null
}
