package com.rtu.number.game.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rtu.number.game.R
import com.rtu.number.game.navigation.NGNavHost

@Composable
fun NumberGameApp(
    navController: NavHostController,
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.main_background),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            NGNavHost(
                contentPadding = it,
                navController = navController,
            )
        }
    }
}
