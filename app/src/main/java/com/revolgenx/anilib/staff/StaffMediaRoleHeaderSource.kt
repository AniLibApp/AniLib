package com.revolgenx.anilib.staff

import android.content.Context
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.source.StaffMediaRoleSource
import com.revolgenx.anilib.media.data.model.MediaModel

class StaffMediaRoleHeaderSource(private val context: Context) : HeaderSource<MediaModel, String>() {
    private var lastHeader: String = ""
    override fun dependsOn(source: Source<*>): Boolean {
        return source is StaffMediaRoleSource
    }

    override fun areItemsTheSame(first: Data<MediaModel, String>, second: Data<MediaModel, String>): Boolean {
        return first.anchor.id == second.anchor.id
    }

    override fun computeHeaders(page: Page, list: List<MediaModel>): List<Data<MediaModel, String>> {
        val results = arrayListOf<Data<MediaModel, String>>()
        val animeStaffRoles = context.getString(R.string.anime_staff_roles)
        val mangaStaffRoles = context.getString(R.string.manga_staff_roles)
        for (model in list) {
            val header = if (model.isAnime) animeStaffRoles else mangaStaffRoles
            if (header != lastHeader) {
                results.add(Data(model, header))
                lastHeader = header
            }
        }
        return results
    }
}