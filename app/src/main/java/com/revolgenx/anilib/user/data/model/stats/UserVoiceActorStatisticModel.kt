package com.revolgenx.anilib.user.data.model.stats

import com.revolgenx.anilib.staff.data.model.StaffModel

class UserVoiceActorStatisticModel: BaseStatisticModel()  {
    var voiceActor: StaffModel? = null
    var characterIds:List<Int>? = null
}