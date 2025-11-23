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

    /**
     * Order Details screen - shows detailed information about a specific order.
     */
    object OrderDetails : Screen("order_details/{orderId}") {
        /**
         * Creates a route for navigating to order details with the given order ID.
         */
        fun createRoute(orderId: String) = "order_details/$orderId"
    }

    /**
     * Edit Order screen - form for editing an existing order.
     */
    object EditOrder : Screen("edit_order/{orderId}") {
        /**
         * Creates a route for navigating to edit order with the given order ID.
         */
        fun createRoute(orderId: String) = "edit_order/$orderId"
    }

    companion object {
        /**
         * All main navigation destinations.
         */
        val mainDestinations = listOf(Orders, Recipients, Tracking, Settings)
    }
}
