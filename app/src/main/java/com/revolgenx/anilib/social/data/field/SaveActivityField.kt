package com.revolgenx.anilib.social.data.field

import com.revolgenx.anilib.common.data.field.BaseField

abstract class SaveActivityField<QM> : BaseField<QM>() {
    var id: Int? = null
    var text: String = ""
}