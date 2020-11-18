package com.revolgenx.anilib.infrastructure.event

import com.revolgenx.anilib.data.meta.StaffMeta

data class BrowseStaffEvent(var meta: StaffMeta) : CommonEvent()
