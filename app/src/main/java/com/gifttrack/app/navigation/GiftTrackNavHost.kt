package com.gifttrack.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gifttrack.feature.orders.ui.AddOrderScreen
import com.gifttrack.feature.orders.ui.EditOrderScreen
import com.gifttrack.feature.orders.ui.OrderDetailsScreen
import com.gifttrack.feature.orders.ui.OrdersScreen
import com.gifttrack.feature.recipients.ui.RecipientsScreen
import com.gifttrack.feature.settings.ui.SettingsScreen
import com.gifttrack.feature.tracking.ui.TrackingScreen

/**
 * GiftTrack Navigation Host.
 *
 * Defines the navigation graph for the entire app.
 * Each composable represents a top-level destination.
 */
@Composable
fun GiftTrackNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Orders.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Orders Screen
        composable(route = Screen.Orders.route) {
            OrdersScreen(
                onNavigateToAddOrder = {
                    navController.navigate(Screen.AddOrder.route)
                },
                onNavigateToOrderDetails = { orderId ->
                    navController.navigate(Screen.OrderDetails.createRoute(orderId))
                }
            )
        }

        // Add Order Screen
        composable(route = Screen.AddOrder.route) {
            AddOrderScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Order Details Screen
        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(
                navArgument("orderId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = {
                    navController.navigate(Screen.EditOrder.createRoute(orderId))
                }
            )
        }

        // Edit Order Screen
        composable(
            route = Screen.EditOrder.route,
            arguments = listOf(
                navArgument("orderId") {
                    type = NavType.StringType
                }
            )
        ) {
            EditOrderScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Recipients Screen
        composable(route = Screen.Recipients.route) {
            RecipientsScreen()
        }

        // Tracking Screen
        composable(route = Screen.Tracking.route) {
            TrackingScreen()
        }

        // Settings Screen
        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
