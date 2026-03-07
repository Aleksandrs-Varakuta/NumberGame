package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rtu.number.game.ui.screens.GameGraphScreen
import com.rtu.number.game.vm.GameViewModel
import kotlinx.serialization.Serializable

@Serializable
data object GameGraphDestination

fun NavGraphBuilder.gameGraph(
    contentPadding: PaddingValues,
    navController: NavHostController
) {
    composable<GameGraphDestination> {
        val parentEntry = remember(it) {
            navController.getBackStackEntry(HomeDestination)
        }
        val viewModel: GameViewModel = viewModel(parentEntry)
        GameGraphScreenRoute(
            contentPadding = contentPadding,
            vm = viewModel
        )
    }
}

@Composable
private fun GameGraphScreenRoute(
    contentPadding: PaddingValues,
    vm: GameViewModel = hiltViewModel(),
) {

    GameGraphScreen(
        contentPadding = contentPadding,
        displayableGraph = emptyList(),
    )

}