package com.revolgenx.anilib.model.airing

import com.revolgenx.anilib.model.AiringTimeModel
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.entry.MediaEntryListModel

class AiringMediaModel :CommonMediaModel(){
    var airingTimeModel: AiringTimeModel? = null
    var mediaEntryListModel: MediaEntryListModel? = null
}
