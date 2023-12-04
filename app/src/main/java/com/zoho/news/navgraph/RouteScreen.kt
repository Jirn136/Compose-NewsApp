package com.zoho.news.navgraph

sealed class RouteScreen(val route: String) {
    object NavScreen : RouteScreen(route = "navScreen")
    object WebScreen : RouteScreen(route = "webScreen")
}