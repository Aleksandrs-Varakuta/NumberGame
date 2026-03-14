package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.ui.screens.HomeScreen
import com.rtu.number.game.vm.GameViewModel
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

@Composable
fun HomeScreenRoute(
    contentPadding: PaddingValues,
    vm: GameViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        onRestart = vm::onRestart,
        onNumberClick = vm::onNumberClick,
    )

}