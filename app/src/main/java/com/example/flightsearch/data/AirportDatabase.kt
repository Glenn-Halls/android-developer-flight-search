package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Departure::class], version = 1, exportSchema = false)
abstract class AirportDatabase : RoomDatabase() {

    abstract fun airportDao(): AirportDao
    abstract fun departureDao(): DepartureDao

    companion object {
        @Volatile
        private var Instance: AirportDatabase? = null

        fun getDatabase(context: Context): AirportDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AirportDatabase::class.java, "database")
                    .createFromAsset("database/flight_search.db")
//                    .fallbackToDestructiveMigration() - removed to allow for persistence
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
