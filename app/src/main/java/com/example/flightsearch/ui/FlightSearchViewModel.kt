package com.example.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.AirportDao
import com.example.flightsearch.data.Departure
import com.example.flightsearch.data.DepartureDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class FlightSearchViewModel (
    private val airportDao: AirportDao,
    private val departureDao: DepartureDao,
    ) : ViewModel() {

    // Observable state holder containing the selected airport and search term
    private val _uiState = MutableStateFlow(FlightSearchUiState())
    // Accessor to state values
    val uiState: StateFlow<FlightSearchUiState> = _uiState

    // Update user search query
    fun updateSearch(input: String) {
        _uiState.update {
            it.copy(
                searchQuery = input
            )
        }
    }

    // Select Airport
    fun selectAirport(airport: Airport) {
        _uiState.update {
            it.copy(
                airportSelected = airport
            )
        }
    }


    // Get list of all airports from database
    fun getAllAirports(): StateFlow<List<Airport>> = airportDao.getAllAirports()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun getSearchAirports(search: String): StateFlow<List<Airport>> =
        airportDao.getSearchAirports(search)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    fun getDepartures(airport: Airport): StateFlow<List<Departure>> =
        departureDao.getAirportDepartures(airport.id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    // Get a list of all airports matching the search query
    /*TODO*/

    // Get list of airports from database containing search term
    /*TODO*/

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                FlightSearchViewModel(
                    application.database.airportDao(),
                    application.database.departureDao()
                )
            }
        }
    }
}
