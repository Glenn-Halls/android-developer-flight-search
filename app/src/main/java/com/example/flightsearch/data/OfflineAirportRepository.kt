package com.example.flightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository (private val airportDao: AirportDao) : AirportRepository {
    override fun getAllAirports() = airportDao.getAllAirports()
    override fun getSearchAirports(search: String): Flow<List<Airport>> =
        airportDao.getSearchAirports(search)
}
