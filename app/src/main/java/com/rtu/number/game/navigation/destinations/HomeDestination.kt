package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.screens.HomeScreen
import com.rtu.number.game.MainViewModel
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.home(
    contentPadding: PaddingValues,
) {
    composable<HomeDestination> {
        HomeScreenRoute(
            contentPadding = contentPadding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenRoute(
    contentPadding: PaddingValues,
    vm: MainViewModel = hiltViewModel(),
) {
    val uiState by vm.mainState.collectAsStateWithLifecycle()

    HomeScreen(
        contentPadding = contentPadding,
        uiState = uiState,
    )

}