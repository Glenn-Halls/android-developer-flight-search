package com.example.flightsearch.data

import android.content.Context

interface AppContainer {
    val airportRepository: AirportRepository
    val departureRepository: DepartureRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val airportRepository: AirportRepository by lazy {
        OfflineAirportRepository(AirportDatabase.getDatabase(context).airportDao())
    }
    override val departureRepository: DepartureRepository by lazy {
        OfflineDepartureRepository(AirportDatabase.getDatabase(context).departureDao())
    }
}
