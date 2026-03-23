package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.ui.screens.HomeScreen
import com.rtu.number.game.vm.GameViewModel
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.home(
    contentPadding: PaddingValues,
    onStartGame: () -> Unit,
    onOpenSettings: () -> Unit
) {
    composable<HomeDestination> {
        HomeScreenRoute(
            contentPadding = contentPadding,
            onStartGame = onStartGame,
            onOpenSettings = onOpenSettings
        )
    }
}

@Composable
fun HomeScreenRoute(
    contentPadding: PaddingValues,
    vm: GameViewModel = hiltViewModel(),
    onOpenSettings: () -> Unit,
    onStartGame: () -> Unit,
) {

    HomeScreen(
        contentPadding = contentPadding,
        onStartGame = { gameMode ->
            vm.onChangeGameMode(gameMode)
            onStartGame()
        },
        onOpenSettings = onOpenSettings,
    )
}
