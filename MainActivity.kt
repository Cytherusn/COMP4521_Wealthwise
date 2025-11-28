package com.example.wealthwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentPageName = currentBackStack?.destination?.route?.split("/")?.first() ?: "Dashboard"

            CompositionLocalProvider(LocalNavController provides navController) {
                LayoutComposable(
                    currentPageName = currentPageName,
                    navController = navController
                ) {
                    NavHost(navController = navController, startDestination = "Dashboard") {
                        composable("Dashboard") { DashboardScreen() }
                        composable("Transactions") { TransactionsScreen() }
                        composable("Budgets") { BudgetsScreen() }
                        composable("Groups") { GroupsScreen() }
                        composable("Settings") { SettingsScreen() }
                        composable("AddTransaction") { AddTransactionScreen() }
                        composable(
                            "GroupDetail/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
                        ) { entry ->
                            GroupDetailScreen(groupId = entry.arguments?.getString("groupId") ?: "")
                        }
                    }
                }
            }

        }
    }
}
