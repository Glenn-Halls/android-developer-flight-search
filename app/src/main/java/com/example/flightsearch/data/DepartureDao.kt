package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartureDao {
    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getAirportDepartures(id: Int): Flow<List<Departure>>
}
