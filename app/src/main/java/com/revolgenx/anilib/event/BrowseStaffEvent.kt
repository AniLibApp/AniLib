package com.revolgenx.anilib.event

import com.revolgenx.anilib.meta.StaffMeta

data class BrowseStaffEvent(var meta: StaffMeta) : CommonEvent()
