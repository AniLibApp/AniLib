package com.revolgenx.anilib.constant

enum class MediaListDisplayMode {
    COMPACT, NORMAL, CARD, CLASSIC, MINIMAL, MINIMAL_LIST
}

enum class AiringListDisplayMode{
    COMPACT, NORMAL, MINIMAL_LIST
}

enum class MediaCharacterDisplayMode{
    COMPACT, NORMAL
}
enum class StaffMediaCharacterDisplayMode{
    COMPACT, NORMAL
}

enum class MediaListStatusEditor(val status: Int) {
    CURRENT(0),
    PLANNING(1),
    COMPLETED(2),
    REPEATING(5),
    PAUSED(4),
    DROPPED(3),
    UNKNOWN(6);

    companion object {
        fun fromMediaListStatus(status: Int): MediaListStatusEditor {
            return values().first { it.status == status }
        }

        fun toMediaListStatus(alStatus:Int) = values()[alStatus].status
    }
}

enum class AlMediaSort(val sort: Int) {
    ID(0),
    TITLE_ROMAJI(2),
    TITLE_ENGLISH(4),
    TITLE_NATIVE(6),
    TYPE(8),
    FORMAT(10),
    START_DATE(12),
    END_DATE(14),
    SCORE(16),
    POPULARITY(18),
    TRENDING(20),
    EPISODES(22),
    DURATION(24),
    STATUS(26),
    CHAPTERS(28),
    VOLUMES(30),
    UPDATED_AT(32),
    FAVOURITES(35),
}


enum class ALMediaListSort(val sort: Int) {
    MEDIA_ID(0),
    SCORE(2),
    STATUS(4),
    PROGRESS(6),
    PROGRESS_VOLUMES(8),
    REPEAT(10),
    PRIORITY(12),
    STARTED_ON(14),
    FINISHED_ON(16),
    ADDED_TIME(18),
    UPDATED_TIME(20),
    MEDIA_TITLE_ROMAJI(22),
    MEDIA_TITLE_ENGLISH(24),
    MEDIA_TITLE_NATIVE(26),
    MEDIA_POPULARITY(28),
}

enum class ALMediaListCollectionSort(val sort: Int) {
    TITLE(0),
    SCORE(2),
    PROGRESS(4),
    UPDATED_AT(6),
    ADDED(8),
    STARTED(10),
    COMPLETED(12),
    RELEASE(14),
    AVERAGE_SCORE(16),
    POPULARITY(18),
}

enum class ALReviewSort(val sort: Int) {
    ID(0),
    SCORE(2),
    RATING(4),
    CREATED_AT(6),
    UPDATED_AT(8),
}

enum class ALAiringSort(val sort: Int) {
    ID(0),
    MEDIA_ID(2),
    TIME(4),
    EPISODE(6),
}

enum class AlActivityType(val type: Int) {
    ALL(-1),
    TEXT(0),
    LIST(4),
    MESSAGE(3)
}
