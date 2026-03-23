package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.ui.screens.GameScreen
import com.rtu.number.game.vm.GameViewModel
import kotlinx.serialization.Serializable

@Serializable
data object GameDestination

fun NavGraphBuilder.game(
    contentPadding: PaddingValues,
    navController: NavController
) {
    composable<GameDestination> {
        val parentEntry = remember(it) {
            navController.getBackStackEntry(HomeDestination)
        }
        val vm: GameViewModel = viewModel(parentEntry)
        GameScreenRoute(
            contentPadding = contentPadding,
            vm = vm,
            onBack = { navController.popBackStack() })
    }
}

@Composable
fun GameScreenRoute(
    contentPadding: PaddingValues,
    vm: GameViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        vm.onRestart()
    }

    GameScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        onRestart = vm::onRestart,
        onNumberClick = vm::onNumberClick,
        onMoveAnimationFinished = vm::onMoveAnimationFinished,
        onBack = onBack
    )
}
