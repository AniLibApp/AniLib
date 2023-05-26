package com.revolgenx.anilib.staff.data.service

import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.service.BaseService
import com.revolgenx.anilib.staff.data.field.StaffField
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.model.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class StaffServiceImpl(apolloRepository: ApolloRepository) : StaffService,
    BaseService(apolloRepository) {
    override fun getStaff(field: StaffField): Flow<StaffModel?> {
        return field.toQuery().map { it.dataAssertNoErrors.staff?.toModel() }
            .flowOn(Dispatchers.IO)
    }
}