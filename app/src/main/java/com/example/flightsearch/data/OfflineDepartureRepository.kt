package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineDepartureRepository (private val departureDao: DepartureDao) : DepartureRepository {
    override fun getAirportDepartures(id: Int): Flow<List<Departure>>
    = departureDao.getAirportDepartures(id)

    override suspend fun addFlightDetails(departure: Departure) =
        departureDao.addFlightDetails(departure)
}
