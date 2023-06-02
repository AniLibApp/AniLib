package com.revolgenx.anilib.staff.ui.model

import com.revolgenx.anilib.MediaStaffQuery
import com.revolgenx.anilib.common.ui.model.BaseModel


data class StaffEdgeModel(
    //    The order the staff should be displayed from the users favourites
    val favouriteOrder: Int? = null,
    //    The id of the connection
    val id: Int = -1,
    val node: StaffModel? = null,
    val role: String? = null,
) : BaseModel


fun MediaStaffQuery.Edge.toModel() = StaffEdgeModel(
    id = id ?: -1,
    role = role,
    node = node?.let { staff ->
        StaffModel(
            id = staff.id,
            name = staff.name?.let {
                StaffNameModel(it.full)
            },
            image = staff.image?.staffImage?.toModel()
        )
    }
)