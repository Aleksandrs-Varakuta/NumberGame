package com.rtu.number.game.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rtu.number.game.navigation.destinations.GameDestination
import com.rtu.number.game.navigation.destinations.HomeDestination
import com.rtu.number.game.navigation.destinations.SettingsDestination
import com.rtu.number.game.navigation.destinations.game
import com.rtu.number.game.navigation.destinations.home
import com.rtu.number.game.navigation.destinations.settings

@Composable
fun NGNavHost(
    contentPadding: PaddingValues,
    navController: NavHostController,
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = HomeDestination
    ) {
        home(
            contentPadding = contentPadding,
            onStartGame = { navController.navigate(GameDestination) },
            onOpenSettings = { navController.navigate(SettingsDestination) })
        game(
            contentPadding = contentPadding,
            navController = navController
        )
        settings(
            contentPadding = contentPadding,
            navController = navController
        )
    }
}
