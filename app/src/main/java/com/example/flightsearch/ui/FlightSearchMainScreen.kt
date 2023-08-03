@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flightsearch.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightInfoMainScreen(
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.factory),
) {
    val uiState = viewModel.uiState.collectAsState()
    val searchQuery = uiState.value.searchQuery
    val searchAirportList by viewModel.getSearchAirports(searchQuery ?: "").collectAsState()
    val focusManager = LocalFocusManager.current
    val airportSelected = uiState.value.airportSelected
    val noAirportSelected = uiState.value.noAirportSelected
    val departureList by viewModel.getDestinationAirports(airportSelected?.id ?: 8, searchQuery ?: "").collectAsState()

    val coroutineScope = rememberCoroutineScope()


    BackHandler {
        viewModel.resetToMain()
    }
    Scaffold(
        topBar = {
            FlightSearchTopBar(
                onUpButtonClick = { viewModel.resetToMain() },
                modifier = modifier
            )
        }
    ) {innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery ?: "",
                onValueChange = { viewModel.updateSearch(it) },
                label = {
                    Text(
                        text = "Search for Airport"
                    )
                },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch
                        this.defaultKeyboardAction(ImeAction.Done)
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Text(
                text = uiState.value.airportSelected.toString(),
            )
            Text(text = noAirportSelected.toString())
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = 0.dp
                ),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_small)
                ),
                modifier = modifier
                    .fillMaxSize()
            ) {
                if (noAirportSelected) {
                    items(searchAirportList) {airport ->
                        AirportCard(airport,false, {viewModel.selectAirport(airport)}, {}, modifier = Modifier.fillMaxWidth())
                    }
                } else {
                    items(departureList) {
                        AirportCard(
                            airport = it,
                            isAirportFavourite = false,
                            onAirportClick = { coroutineScope.launch {
                                viewModel.addFlightFromClick(it)
                            } },
                            onFavouriteClick = {},
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopBar(
    onUpButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Flight Search",
                style = MaterialTheme.typography.displayLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(onClick = onUpButtonClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button),
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportCard(
    airport: Airport,
    isAirportFavourite: Boolean,
    onAirportClick: (Airport) -> Unit,
    onFavouriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        onClick = { onAirportClick(airport) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.card_height))
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = airport.iataCode,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = airport.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(onClick = onFavouriteClick) {
                Icon(
                    imageVector = if (isAirportFavourite)
                        Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.favourite_button)
                )
            }
        }
    }
}
