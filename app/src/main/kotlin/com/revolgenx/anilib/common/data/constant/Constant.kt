package com.revolgenx.anilib.common.data.constant

import java.time.Instant
import java.time.temporal.ChronoUnit

const val YOUTUBE_URL = "https://www.youtube.com/watch?v="
const val DAILYMOTION_URL = "https://www.dailymotion.com/video/"


const val YOUTUBE = "youtube"
const val DAILYMOTION = "dailymotion"

object Config {
    const val API_URL = "https://graphql.anilist.co"
    const val SIGN_UP_URL = "https://anilist.co/signup"
    const val AUTH_ENDPOINT = "https://anilist.co/api/v2/oauth/authorize"
}

enum class AdsInterval(val value: Int) {
    EVERY_8_HR(0), EVERY_DAY(1), EVERY_OTHER_DAY(2), EVERY_WEEK(3), EVERY_MONTH(4), NEVER(-1);

    companion object {
        fun fromValue(value: Int): AdsInterval {
            return entries.first { it.value == value }
        }
    }
}

fun showAds(
    adsInterval: AdsInterval,
    currentEpochSecond: Long,
    adsDisplayedDateTime: Long
): Boolean {
    return when (adsInterval) {
        AdsInterval.EVERY_8_HR -> {
            ChronoUnit.HOURS.between(
                Instant.ofEpochSecond(adsDisplayedDateTime),
                Instant.ofEpochSecond(currentEpochSecond)
            ) >= 8
        }

        AdsInterval.EVERY_DAY -> {
            ChronoUnit.DAYS.between(
                Instant.ofEpochSecond(adsDisplayedDateTime),
                Instant.ofEpochSecond(currentEpochSecond)
            ) >= 1
        }

        AdsInterval.EVERY_OTHER_DAY -> {
            ChronoUnit.DAYS.between(
                Instant.ofEpochSecond(adsDisplayedDateTime),
                Instant.ofEpochSecond(currentEpochSecond)
            ) >= 2
        }

        AdsInterval.EVERY_WEEK -> {
            ChronoUnit.WEEKS.between(
                Instant.ofEpochSecond(adsDisplayedDateTime),
                Instant.ofEpochSecond(currentEpochSecond)
            ) >= 1
        }

        AdsInterval.EVERY_MONTH -> {
            ChronoUnit.MONTHS.between(
                Instant.ofEpochSecond(adsDisplayedDateTime),
                Instant.ofEpochSecond(currentEpochSecond)
            ) >= 1
        }

        AdsInterval.NEVER -> {
            false
        }
    }
}

object LauncherShortcutKeys {
    const val LAUNCHER_SHORTCUT_EXTRA_KEY = "LAUNCHER_SHORTCUT_EXTRA_KEY"
}

enum class LauncherShortcuts {
    HOME, ANIME, MANGA, NOTIFICATION
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
    FAVOURITES(35);

    companion object {
        fun from(sort: Int) = entries.firstOrNull { it.sort == sort }
    }
}

interface ContentOrder {
    fun toStringRes(): Int
}

enum class ExploreSectionOrder : ContentOrder {
    AIRING, TRENDING, POPULAR, NEWLY_ADDED, WATCHING, READING;

    override fun toStringRes(): Int {
        return when (this) {
            AIRING -> anilib.i18n.R.string.airing
            TRENDING -> anilib.i18n.R.string.trending
            POPULAR -> anilib.i18n.R.string.popular
            NEWLY_ADDED -> anilib.i18n.R.string.newly_added
            WATCHING -> anilib.i18n.R.string.watching
            READING -> anilib.i18n.R.string.reading
        }
    }
}

enum class MainPageOrder : ContentOrder {
    HOME, ANIME, MANGA, ACTIVITY;

    override fun toStringRes(): Int {
        return when (this) {
            HOME -> anilib.i18n.R.string.home
            ANIME -> anilib.i18n.R.string.anime
            MANGA -> anilib.i18n.R.string.manga
            ACTIVITY -> anilib.i18n.R.string.activity
        }
    }
}