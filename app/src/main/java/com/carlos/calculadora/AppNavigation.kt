
package com.carlos.calculadora

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Compartimos el mismo ViewModel entre las pantallas
    val calculatorViewModel: CalculatorViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "calculator"
    ) {

        composable(route = "calculator") {
            ShowCalculator(
                onHistoryClick = {
                    navController.navigate("history")
                },
                viewModel = calculatorViewModel
            )
        }

        composable(route = "history") {
            ShowHistory(
                onBack = {
                    navController.popBackStack()
                },
                viewModel = calculatorViewModel
            )
        }
    }
}
