package com.example.flightsearch.data

class OfflineAirportRepository (private val airportDao: AirportDao) : AirportRepository {
    override fun getAllAirports() = airportDao.getAllAirports()
}
