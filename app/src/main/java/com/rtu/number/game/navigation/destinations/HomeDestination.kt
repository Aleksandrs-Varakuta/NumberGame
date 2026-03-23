package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.navigation.RootGraph
import com.rtu.number.game.ui.screens.HomeScreen
import com.rtu.number.game.vm.GameViewModel
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.home(
    contentPadding: PaddingValues,
    onStartGame: () -> Unit,
    onOpenSettings: () -> Unit,
    navController: NavController
) {
    composable<HomeDestination> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(RootGraph)
        }
        val vm: GameViewModel = hiltViewModel(parentEntry)

        HomeScreenRoute(
            contentPadding = contentPadding,
            onStartGame = onStartGame,
            onOpenSettings = onOpenSettings,
            vm = vm
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
