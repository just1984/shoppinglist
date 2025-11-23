package com.gifttrack.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            OrdersScreen()
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
