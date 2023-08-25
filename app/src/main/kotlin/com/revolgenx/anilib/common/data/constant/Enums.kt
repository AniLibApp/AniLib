package com.revolgenx.anilib.common.data.constant

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
    FAVOURITES(35);

    companion object{
        fun from(sort: Int) = AlMediaSort.values().firstOrNull { it.sort == sort }
    }
}

enum class LauncherShortcuts {
    HOME, ANIME, MANGA, NOTIFICATION
}

object LauncherShortcutKeys {
    const val LAUNCHER_SHORTCUT_EXTRA_KEY = "LAUNCHER_SHORTCUT_EXTRA_KEY"
}


