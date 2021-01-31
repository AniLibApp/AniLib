package com.revolgenx.anilib.radio.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RadioStationDao {

    @Query("SELECT * FROM RadioStations ORDER BY id")
    suspend fun get(): List<RadioStation>

    @Insert
    suspend fun add(stations: List<RadioStation>): List<Long>

    @Update
    suspend fun update(stations: RadioStation)

    @Query("DELETE FROM RadioStations")
    suspend fun delete()

}