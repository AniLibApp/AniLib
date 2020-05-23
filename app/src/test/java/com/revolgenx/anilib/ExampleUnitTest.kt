package com.revolgenx.anilib

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.type.MediaSort
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val str = listOf(
        "Title Romaji",
        "Title Romaji Desc",
        "Title English",
        "Title English Desc",
        "Title Native",
        "Title Native Desc",
        "Type",
        "Type Desc",
        "Format",
        "Format Desc",
        "Start Date",
        "Start Date Desc",
        "End Date",
        "End Date Desc",
        "Score",
        "Score Desc",
        "Popularity",
        "Popularity Desc",
        "Trending",
        "Trending Desc",
        "Episodes",
        "Episodes Desc",
        "Duration",
        "Duration Desc",
        "Status",
        "Status Desc",
        "Chapters",
        "Chapters Desc",
        "Volumes",
        "Volumes Desc",
        "Updated At",
        "Updated At Desc",
        "Favourites",
        "Favourites Desc"
    )

    val genres =
        "Action, Adventure, Comedy, Drama, Ecchi, Fantasy, Horror, Mahou Shoujo, Mecha, Music, Mystery, Psychological, Romance, Sci-Fi, Slice of Life, Sports, Supernatural, Thriller"

    val tags =
        "4-koma, Achronological Order, Afterlife, Age Gap, Airsoft, Aliens, Alternate Universe, American Football, Amnesia, Anti-Hero, Archery, Assassins, Athletics, Augmented Reality, Aviation," +
                "Badminton, Band, Bar, Baseball, Basketball, Battle Royale, Biographical, Bisexual, Body Swapping, Boxing, Bullying, Calligraphy, Card Battle, Cars, CGI, Chibi, Chuunibyou, Classic Literature, College, Coming of Age, Cosplay, Crossdressing, Crossover, Cultivation, Cute Girls Doing Cute Things, Cyberpunk, Cycling," +
                "Dancing, Delinquents, Demons, Development, Dragons, Drawing, Dystopian, Economics, Educational, Ensemble Cast, Environmental, Episodic, Espionage, Fairy Tale, Family Life, Fashion, Female Protagonist, Fishing, Fitness, Flash, Food, Football, Foreign, Fugitive, Full CGI, Full Colour," +
                "Gambling, Gangs, Gender Bending, Gender Neutral, Ghost, Gods, Gore, Guns, Gyaru, Harem, Henshin, Hikikomori, Historical, Ice Skating, Idol, Isekai, Iyashikei, Josei, Kaiju, Karuta, Kemonomimi, Kids, Love Triangle, Mafia, Magic, Mahjong, Maids, Male Protagonist, Martial Arts, Memory Manipulation, Meta, Military, Monster Girl, Mopeds, Motorcycles, Musical, Mythology," +
                "Nekomimi, Ninja, No Dialogue, Noir, Nudity, Otaku Culture, Outdoor, Parody, Philosophy, Photography, Pirates, Poker, Police, Politics, Post-Apocalyptic, Primarily Adult Cast, Primarily Female Cast, Primarily Male Cast, Puppetry, Real Robot, Rehabilitation, Reincarnation, Revenge, Reverse Harem, Robots, Rugby, Rural," +
                "Samurai, Satire, School, School Club, Seinen, Ships, Shogi, Shoujo, Shoujo Ai, Shounen, Shounen Ai, Slapstick, Slavery, Space, Space Opera, Steampunk, Stop Motion, Super Power, Super Robot, Superhero, Surreal Comedy, Survival, Swimming, Swordplay," +
                "Table Tennis, Tanks, Teacher, Tennis, Terrorism, Time Manipulation, Time Skip, Tragedy, Trains, Triads, Tsundere, Urban Fantasy, Vampire, Video Games, Virtual World, Volleyball, War, Witch, Work, Wrestling, Writing, Wuxia, Yakuza, Yandere, Youkai, Zombie"

    @Test
    fun testSort() {

        str.map { it.replace(" ", "_").toUpperCase() }.forEach {
            println(MediaSort.valueOf(it))
        }
    }

    @Test
    fun printTags() {
        tags.split(",").map { it.trim() }.forEach { x ->
            println("<item>$x</item>")
        }
    }


    internal class testClass {
        var hlw = "hlw"
        var nth = "nth"
        var nth2 = null
    }

    internal class testClass1 {
        var hlw: String? = null
        var nth: String? = null
        var nth2: String? = null
        override fun toString(): String {
            return hlw + nth + nth2
        }
    }

    @Test
    fun gsonTest() {
        println(System.currentTimeMillis())
        MediaBrowseFilterModel().also {
            it.countryOfOrigin = 1
            it.tags = listOf("evertying", "nth")
        }.let {
            println(System.currentTimeMillis())
            Gson().toJson(it as BrowseFilterModel)
        }.let {
            println(it)
            println(
                Gson().fromJson(
                    "{\"yearEnabled\":false,\"countryOfOrigin\":1,\"tags\":[\"evertying\",\"nth\"],\"query\":\"sdasdfa\"}\n",
                    JsonObject::class.java
                ).get("countryOfOrigin")
            )
        }
        println(System.currentTimeMillis())

    }

    val filter =
        "{\"type\":0,\"yearEnabled\":false,\"countryOfOrigin\":1,\"tags\":[\"evertying\",\"nth\"]}\n"

    @Test
    fun type() {
        val gson = Gson()
        println(System.currentTimeMillis())
        val data = try {
            when (gson.fromJson(filter, JsonObject::class.java).get("type").asInt) {
                BrowseTypes.ANIME.ordinal, BrowseTypes.MANGA.ordinal -> {
                    gson.fromJson(filter, MediaBrowseFilterModel::class.java)
                }
                BrowseTypes.CHARACTER.ordinal -> {
                    gson.fromJson(filter, CharacterBrowseFilterModel::class.java)
                }
                BrowseTypes.STAFF.ordinal -> {
                    gson.fromJson(filter, StaffBrowseFilterModel::class.java)
                }
                BrowseTypes.STUDIO.ordinal -> {
                    gson.fromJson(filter, StudioBrowseFilterModel::class.java)
                }
                else -> null
            }
        } catch (e: ClassCastException) {
            null
        } catch (e: IllegalStateException) {
            null
        }
        println(data.toString())
        println(System.currentTimeMillis())

        gson.toJson(data)
        println(System.currentTimeMillis())

    }


    @Test
    fun testTagPerf() {
        runBlocking {

            val testlist = tags.split(",").map { it.trim() }
            println(System.currentTimeMillis().toString())
            val mutablemap = mutableMapOf<String, TagField>()
            val testmap = testlist.map { mutablemap[it] = TagField(it, false) }

            println(System.currentTimeMillis().toString())
//            testlist.forEach {
//                testmap[it] = TagField(it, false)
//            }
            println(System.currentTimeMillis().toString())
        }

    }

    @Test
    fun testMatchers() {
        val youtube =
            "youtube(https://youtbe.com) youtube(https://youtbe.com)  youtube(https://youtbe.com)youtube(https://youtbe.com)"
        val pattern = Pattern.compile("youtube\\(([^)]+)\\)")
        val matches = pattern.matcher(youtube)
        while (matches.find()) {
            println(matches.group(1))
            println(matches.start())
        }
        val patternimg: Pattern = Pattern.compile("img(\\d+)?\\(([^)]+)\\)")
        val matcher = patternimg.matcher("img999(125)")
        while (matcher.find()) {
            println(
                matcher.group(1) +
                        matcher.group(2)
            )
        }
    }

}
