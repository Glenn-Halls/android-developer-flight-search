package com.example.flightsearch.ui

import com.example.flightsearch.data.Airport

data class FlightSearchUiState(
    val airportSelected: Airport? = null,
    val searchQuery: String? = null,
)
