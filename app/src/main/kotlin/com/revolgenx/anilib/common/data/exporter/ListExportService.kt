package com.revolgenx.anilib.common.data.exporter

import com.revolgenx.anilib.list.ui.model.MediaListModel
import java.io.OutputStream


interface ListExportService {
    suspend fun export(items: List<MediaListModel>, outputStream: OutputStream)
    val fileExtension: String
    val mimeType: String
}