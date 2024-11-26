package com.example.bookpointkotlin

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookpoint.screens.HomeScreen
import com.example.bookpointkotlin.widget.AppTheme

@Composable
fun MyApp() {
    val navController = rememberNavController()

    MaterialTheme(
        colors = AppTheme.colors,
        typography = AppTheme.typography,
        shapes = AppTheme.shapes
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") { HomeScreen() }
            // Add other screens here
        }
    }
}

fun main() {
    androidx.compose.ui.platform.ComposeView(LocalContext.current).setContent {
        MyApp()
    }
}

