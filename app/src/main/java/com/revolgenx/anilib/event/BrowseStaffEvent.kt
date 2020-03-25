package com.revolgenx.anilib.event

import com.revolgenx.anilib.event.meta.StaffMeta

data class BrowseStaffEvent(var meta: StaffMeta) : BaseEvent()
