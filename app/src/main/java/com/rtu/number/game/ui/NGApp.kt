package com.rtu.number.game.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.rtu.number.game.MainViewModel
import com.rtu.number.game.navigation.NGNavHost

@Composable
fun NumberGameApp(
    navController: NavHostController,
    mainState: MainViewModel.MainState,
) {
    Scaffold {
        Box {
            NGNavHost(
                contentPadding = it,
                mainState = mainState,
                navController = navController,
            )
        }
    }
}
