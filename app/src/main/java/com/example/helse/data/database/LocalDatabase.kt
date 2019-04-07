package com.example.helse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.Location

@Database(entities = [Location::class, AirqualityForecast::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
    abstract fun airqualityDao(): AirqualityDao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java, "local_database"
            ).build()
    }

}
