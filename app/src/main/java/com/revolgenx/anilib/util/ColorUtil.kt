package com.revolgenx.anilib.util

import com.pranavpandey.android.dynamic.utils.DynamicColorUtils

val colorsMap by lazy {
    mapOf(
        "amazon" to "#ff9b00",
        "animax" to "#2377ff",
        "animelab" to "#3c2F7E",
        "animenetwork" to "#735ce8",
        "comicwalker" to "#f41f1f",
        "comico" to "#f41f1f",
        "crunchyroll" to "#f4851e",
        "dajiaochong manhua" to "#4286f4",
        "daum webtoon" to "#f45350",
        "facebook" to "#29487d",
        "fakku" to "#f41f1f",
        "funimation" to "#644fcc",
        "hidive" to "#30c7ff",
        "hulu" to "#00E676",
        "justoon" to "#636ee2",
        "kakaopage" to "#e0e03c",
        "kaiKan manhua" to "#e0e03c",
        "lezhin" to "#f45350",
        "madman" to "#f45350",
        "manga.club" to "#d68917",
        "mangabox" to "#69b7db",
        "manman manhua" to "#69b7db",
        "naver" to "#1ec800",
        "netflix" to "#e50914",
        "nico nico Seiga" to "#f4851e",
        "piccoma" to "#fc8b4e",
        "pixiv comic" to "#dc8fea",
        "qq" to "#70a3f4",
        "toomics" to "#f41f1f",
        "twitter" to "#00acee",
        "viewster" to "#f4851e",
        "vimeo" to "#00acee",
        "viz" to "#00acee",
        "wakanim" to "#f41f1f",
        "web comics" to "#f41f1f",
        "webtoons" to "#fc8b4e",
        "weibo manhua" to "#fc8b4e",
        "youtube" to "#e50914"
    )
}

fun isEnoughWhite(color:Int) = DynamicColorUtils.getColorDarkness(color) <= 0.2
