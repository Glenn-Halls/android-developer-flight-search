package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport ORDER BY iata_code")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("""
        SELECT * FROM airport
        WHERE name LIKE '%'||:search||'%'
        OR iata_code LIKE '%'||:search||'%'
        ORDER BY iata_code
    """)
    fun getSearchAirports(search: String): Flow<List<Airport>>
}
