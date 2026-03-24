package com.rtu.number.game.navigation.destinations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rtu.number.game.navigation.RootGraph
import com.rtu.number.game.ui.screens.SettingsScreen
import com.rtu.number.game.vm.GameViewModel
import kotlinx.serialization.Serializable


@Serializable
data object SettingsDestination

fun NavGraphBuilder.settings(
    contentPadding: PaddingValues,
    navController: NavController
) {
    composable<SettingsDestination> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(RootGraph)
        }
        val vm: GameViewModel = hiltViewModel(parentEntry)

        SettingsScreenRoute(
            contentPadding = contentPadding,
            vm = vm,
            onBack = {
                navController.navigate(HomeDestination) {
                    popUpTo(HomeDestination) { inclusive = false }
                    launchSingleTop = true
                }
            })
    }
}

@Composable
fun SettingsScreenRoute(
    contentPadding: PaddingValues,
    vm: GameViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        contentPadding = contentPadding,
        settings = uiState.settings,
        onBack = onBack,
        onChangeCellCount = vm::onChangeCellCount,
        onChangePlayer1Name = vm::onChangePlayer1Name,
        onChangePlayer2Name = vm::onChangePlayer2Name,
        onChangeFirstPlayer = vm::onChangeFirstPlayer,
        onChangeAlgorithm = vm::onChangeAlgorithm,
        onChangeAiDepth = vm::onChangeAiDepth
    )
}
