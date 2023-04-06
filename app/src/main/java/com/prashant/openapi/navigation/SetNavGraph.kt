package com.prashant.openapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prashant.openapi.screens.search.Search


/**
 *A composable function that sets up the navigation graph for the app.
 *
 *@param navHostController the NavHostController to use for navigation
 */
@Composable
fun SetNavGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.SearchScreen.route,
    ) {
        composable(Screens.SearchScreen.route) {
            Search(navHostController)
        }
    }
}