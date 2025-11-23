package com.gifttrack.app.navigation

/**
 * Navigation routes for the GiftTrack app.
 *
 * This sealed class defines all navigation destinations.
 * Each route represents a top-level destination in the app.
 */
sealed class Screen(val route: String) {
    /**
     * Orders screen - shows list of all orders.
     */
    object Orders : Screen("orders")

    /**
     * Recipients screen - shows list of gift recipients.
     */
    object Recipients : Screen("recipients")

    /**
     * Tracking screen - shows package tracking information.
     */
    object Tracking : Screen("tracking")

    /**
     * Settings screen - app settings and preferences.
     */
    object Settings : Screen("settings")

    /**
     * Add Order screen - form for manually adding a new order.
     */
    object AddOrder : Screen("add_order")

    companion object {
        /**
         * All main navigation destinations.
         */
        val mainDestinations = listOf(Orders, Recipients, Tracking, Settings)
    }
}
