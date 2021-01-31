package com.revolgenx.anilib.radio.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RadioStation::class], version = 1)
abstract class RadioStationDatabase : RoomDatabase() {
    abstract fun radioStationDao(): RadioStationDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RadioStationDatabase? = null

        fun getDatabase(context: Context): RadioStationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RadioStationDatabase::class.java,
                    "radiostation_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}