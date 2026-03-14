package com.rtu.number.game.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rtu.number.game.navigation.destinations.HomeDestination
import com.rtu.number.game.navigation.destinations.home

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
        )
    }
}
