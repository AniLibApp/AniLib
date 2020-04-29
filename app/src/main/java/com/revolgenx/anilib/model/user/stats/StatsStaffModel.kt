package com.revolgenx.anilib.model.user.stats


class StatsStaffModel : BaseStatsModel() {
    var staffId: Int? = null
        set(value) {
            field = value
            baseId = value
        }
    var name: String? = null
    var image:String? = null
}