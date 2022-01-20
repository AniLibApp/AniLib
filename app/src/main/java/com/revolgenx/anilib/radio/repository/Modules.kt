package com.revolgenx.anilib.radio.repository

import android.content.Context
import androidx.room.Room
import com.revolgenx.anilib.radio.repository.api.RadioGithubApi
import com.revolgenx.anilib.radio.repository.room.RadioStationDao
import com.revolgenx.anilib.radio.repository.room.RadioStationDatabase
import org.koin.dsl.module


val radioApiModules = module {
    single { RadioGithubApi() }
}

val radioRoomModules = module {
    fun provideDatabase(context: Context): RadioStationDatabase {
        return synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                RadioStationDatabase::class.java,
                "radiostation_database"
            ).build()
        }
    }

    fun provideRadioStationDao(db: RadioStationDatabase): RadioStationDao {
        return db.radioStationDao()
    }

    single { provideDatabase(get()) }
    factory { provideRadioStationDao(get()) }

}