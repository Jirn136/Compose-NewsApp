package com.zoho.news.navgraph

sealed class RouteScreen(val route: String) {
    data object NewsScreen : RouteScreen(route = "navScreen")
    data object WebScreen : RouteScreen(route = "webScreen")
}