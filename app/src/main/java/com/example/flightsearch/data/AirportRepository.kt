package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAllAirports(): Flow<List<Airport>>
    fun getSearchAirports(search: String): Flow<List<Airport>>

    fun getDestinationAirports(selectedAirportId: Int, search: String): Flow<List<Airport>>
}
