package com.checklistapp2.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.checklistapp2.app.ui.screen.*
import com.checklistapp2.app.ui.viewmodel.ChecklistViewModel
import com.checklistapp2.app.ui.viewmodel.UserViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object ChecklistDetail : Screen("checklist/{checklistId}") {
        fun createRoute(checklistId: Long) = "checklist/$checklistId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    checklistViewModel: ChecklistViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                checklistViewModel = checklistViewModel,
                userViewModel = userViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable(
            route = Screen.ChecklistDetail.route,
            arguments = listOf(navArgument("checklistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val checklistId = backStackEntry.arguments?.getLong("checklistId") ?: return@composable
            ChecklistDetailScreen(
                navController = navController,
                checklistViewModel = checklistViewModel,
                checklistId = checklistId
            )
        }
    }
}
