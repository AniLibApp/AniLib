package com.revolgenx.anilib.common.data.exporter

import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.OutputStream

class MALExportService : ListExportService {
    override val fileExtension: String = "xml"
    override val mimeType: String = "application/xml"

    override suspend fun export(items: List<MediaListModel>, outputStream: OutputStream) {
        withContext(Dispatchers.IO) {
            val validItems =
                items.filter { it.idMal != null && it.listStatus != MediaListStatus.REPEATING }
            val repeatingItems =
                items.filter { it.idMal != null && it.listStatus == MediaListStatus.REPEATING }
            val missingItems = items.filter { it.idMal == null }

            outputStream.bufferedWriter().use { writer ->
                writeXmlHeader(writer)
                writer.write("<myanimelist>\n")

                // 1. Write User Stats (Dynamic based on Anime vs Manga)
                if (items.isNotEmpty()) {
                    writeUserInfo(writer, validItems)
                }

                // 2. Write Summary of Missing Entries (The comment block at the top)
                if (missingItems.isNotEmpty()) {
                    writeMissingSummary(writer, missingItems)
                }

                // 3. Write Valid Items (Normal XML)
                for (item in validItems) {
                    writeItem(writer, item)
                }

                // 4. Write Missing Items
                if(missingItems.isNotEmpty()){
                    wrapComment(writer, msg = "Total Missing: ${missingItems.size}") {
                        for (item in missingItems) {
                            writeItem(writer, item)
                        }
                    }
                }

                if(repeatingItems.isNotEmpty()){
                    wrapComment(writer, msg = "Total Repeating: ${repeatingItems.size}") {
                        for (item in repeatingItems) {
                            writeItem(writer, item)
                        }
                    }
                }

                writer.write("</myanimelist>")
            }
        }
    }

    private fun writeMissingSummary(writer: BufferedWriter, missingItems: List<MediaListModel>) {
        wrapComment(writer, msg = "MAL entry not found for") {
            for (item in missingItems) {
                val media = item.media
                val typeSlug = if (media?.type == MediaType.ANIME) "anime" else "manga"
                writer.write("\t\t\"${media?.title?.romaji}\" https://anilist.co/$typeSlug/${media?.id}\n")
            }
        }
    }

    private fun writeUserInfo(
        writer: BufferedWriter,
        validItems: List<MediaListModel>
    ) {
        val list = validItems.firstOrNull()
        val isAnime = list?.media?.type == MediaType.ANIME
        val user = list?.user
        val total = validItems.size
        val watching = validItems.count { it.listStatus == MediaListStatus.CURRENT }
        val completed = validItems.count { it.listStatus == MediaListStatus.COMPLETED }
        val onHold = validItems.count { it.listStatus == MediaListStatus.PAUSED }
        val dropped = validItems.count { it.listStatus == MediaListStatus.DROPPED }
        val ptw = validItems.count { it.listStatus == MediaListStatus.PLANNING }

        writer.write("\t<myinfo>\n")
        writer.write("\t\t<user_id></user_id>\n")
        writer.write("\t\t<user_name>${user?.name.orEmpty()}</user_name>\n")

        // Export Type: 1 = Anime, 2 = Manga
        val exportType = if (isAnime) "1" else "2"
        writer.write("\t\t<user_export_type>$exportType</user_export_type>\n")

        // Dynamic Tag Names based on type
        val totalTag = if (isAnime) "user_total_anime" else "user_total_manga"
        val watchingTag = if (isAnime) "user_total_watching" else "user_total_reading"
        val ptwTag = if (isAnime) "user_total_plantowatch" else "user_total_plantoread"

        writer.write("\t\t<$totalTag>$total</$totalTag>\n")
        writer.write("\t\t<$watchingTag>$watching</$watchingTag>\n")
        writer.write("\t\t<user_total_completed>$completed</user_total_completed>\n")
        writer.write("\t\t<user_total_onhold>$onHold</user_total_onhold>\n")
        writer.write("\t\t<user_total_dropped>$dropped</user_total_dropped>\n")
        writer.write("\t\t<$ptwTag>$ptw</$ptwTag>\n")
        writer.write("\t</myinfo>\n\n")
    }

    private fun writeItem(writer: BufferedWriter, item: MediaListModel) {
        val isAnime = item.media?.type == MediaType.ANIME
        val nodeName = if (isAnime) "anime" else "manga"

        writer.write("\t<$nodeName>\n")
        // --- ID ---
        val idTag = if (isAnime) "series_animedb_id" else "manga_mangadb_id"
        writer.write("\t\t<$idTag>${item.idMal ?: ""}</$idTag>\n")

        // --- TITLE ---
        val titleTag = if (isAnime) "series_title" else "manga_title"
        writer.write("\t\t<$titleTag><![CDATA[${item.media?.title?.romaji.orEmpty()}]]></$titleTag>\n")

        if (isAnime) {
            // Anime "Type"
            writer.write("\t\t<series_type></series_type>\n")
        } else {
            // --- VOLUMES (Manga Only) ---
            writer.write("\t\t<manga_volumes>${item.media?.volumes.orZero()}</manga_volumes>\n")
        }

        // --- EPISODES / CHAPTERS ---
        if (isAnime) {
            writer.write("\t\t<series_episodes>${item.media?.episodes.orZero()}</series_episodes>\n")
        } else {
            writer.write("\t\t<manga_chapters>${item.media?.chapters.orZero()}</manga_chapters>\n")
        }

        // --- MY ID ---
        writer.write("\t\t<my_id></my_id>\n") // Usually blank in exports

        // --- READ VOLUMES (Manga Only) ---
        if (!isAnime) {
            writer.write("\t\t<my_read_volumes>${item.progressVolumes}</my_read_volumes>\n")
        }

        // --- WATCHED EPISODES / READ CHAPTERS ---
        val progressTag = if (isAnime) "my_watched_episodes" else "my_read_chapters"
        writer.write("\t\t<$progressTag>${item.progress}</$progressTag>\n")

        // --- DATES ---
        writer.write("\t\t<my_start_date>${item.startedAt.toMALDate()}</my_start_date>\n")
        writer.write("\t\t<my_finish_date>${item.completedAt.toMALDate()}</my_finish_date>\n")

        // --- EXTRA METADATA ---
        if (isAnime) {
            writer.write("\t\t<my_rated></my_rated>\n")
        } else {
            writer.write("\t\t<my_scanalation_group><![CDATA[]]></my_scanalation_group>\n")
        }

        writer.write("\t\t<my_score>${item.score10}</my_score>\n")

        if (isAnime) {
            writer.write("\t\t<my_dvd></my_dvd>\n")
        }

        writer.write("\t\t<my_storage></my_storage>\n")

        // --- STATUS ---
        writer.write("\t\t<my_status>${mapStatusToMal(item.listStatus!!, isAnime)}</my_status>\n")

        // --- COMMENTS ---
        writer.write("\t\t<my_comments><![CDATA[${item.notes.orEmpty()}]]></my_comments>\n")

        // --- REWATCH/REREAD INFO ---
        val timesTag = if (isAnime) "my_times_watched" else "my_times_read"
        writer.write("\t\t<$timesTag>0</$timesTag>\n")

        if (isAnime) {
            writer.write("\t\t<my_rewatch_value></my_rewatch_value>\n")
        }

        writer.write("\t\t<my_tags><![CDATA[]]></my_tags>\n")

        if (isAnime) {
            writer.write("\t\t<my_rewatching>0</my_rewatching>\n")
            writer.write("\t\t<my_rewatching_ep>0</my_rewatching_ep>\n")
        } else {
            writer.write("\t\t<my_reread_value></my_reread_value>\n")
        }

        writer.write("\t\t<update_on_import>0</update_on_import>\n")

        // --- CLOSE TAG ---
        writer.write("\t</$nodeName>\n")
    }

    private fun writeXmlHeader(writer: BufferedWriter) {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
    }

    private fun wrapComment(writer: BufferedWriter, msg: String = "", content: () -> Unit) {
        writer.write("\t<!-- ${msg}\n")
        content()
        writer.write("\t-->)\n\n")
    }

    private fun mapStatusToMal(status: MediaListStatus, isAnime: Boolean): String {
        return when (status) {
            MediaListStatus.CURRENT, MediaListStatus.REPEATING -> if (isAnime) "Watching" else "Reading"
            MediaListStatus.COMPLETED -> "Completed"
            MediaListStatus.PAUSED -> "On-Hold"
            MediaListStatus.DROPPED -> "Dropped"
            MediaListStatus.PLANNING -> if (isAnime) "Plan to Watch" else "Plan to Read"
            else -> ""
        }
    }

    private fun FuzzyDateModel?.toMALDate() =
        if (this != null) "${year}-${month}-${day}" else "0000-00-00"
}