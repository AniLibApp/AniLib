package com.revolgenx.anilib.constant

import com.revolgenx.anilib.type.MediaSource

enum class AlMediaSource(val value:String) {
    /**
     * An original production not based of another work
     */
    ORIGINAL("ORIGINAL"),

    /**
     * Asian comic book
     */
    MANGA("MANGA"),

    /**
     * Written work published in volumes
     */
    LIGHT_NOVEL("LIGHT_NOVEL"),

    /**
     * Video game driven primary by text and narrative
     */
    VISUAL_NOVEL("VISUAL_NOVEL"),

    /**
     * Video game
     */
    VIDEO_GAME("VIDEO_GAME"),

    /**
     * Other
     */
    OTHER("OTHER"),

    /**
     * Version 2+ only. Written works not published in volumes
     */
    NOVEL("NOVEL"),

    /**
     * Version 2+ only. Self-published works
     */
    DOUJINSHI("DOUJINSHI"),

    /**
     * Version 2+ only. Japanese Anime
     */
    ANIME("ANIME"),

    /**
     * Version 3 only. Written works published online
     */
    WEB_NOVEL("WEB_NOVEL"),

    /**
     * Version 3 only. Live action media such as movies or TV show
     */
    LIVE_ACTION("LIVE_ACTION"),

    /**
     * Version 3 only. Games excluding video games
     */
    GAME("GAME"),

    /**
     * Version 3 only. Comics excluding manga
     */
    COMIC("COMIC"),

    /**
     * Version 3 only. Multimedia project
     */
    MULTIMEDIA_PROJECT("MULTIMEDIA_PROJECT"),

    /**
     * Version 3 only. Picture book
     */
    PICTURE_BOOK("PICTURE_BOOK"),

    /**
     * Auto generated constant for unknown enum values
     */
    UNKNOWN("UNKNOWN");
}


fun MediaSource?.toSource() = this?.let {AlMediaSource.valueOf(rawValue)}