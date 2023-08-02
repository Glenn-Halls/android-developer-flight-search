package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

interface DepartureRepository {
    fun getAirportDepartures(id: Int): Flow<List<Departure>>
}
