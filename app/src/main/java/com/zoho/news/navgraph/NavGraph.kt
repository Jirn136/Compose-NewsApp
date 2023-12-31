package com.zoho.news.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zoho.news.screens.news.news.NewsScreen
import com.zoho.news.screens.news.news.NewsViewModel
import com.zoho.news.screens.news.web.WebScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(navController: NavHostController) {
    val newsViewModel = hiltViewModel<NewsViewModel>()
    NavHost(
        navController = navController,
        startDestination = RouteScreen.NewsScreen.route
    ) {
        composable(route = RouteScreen.NewsScreen.route) {
            NewsScreen(newsViewModel) { clickedItem ->
                val encodedUrl = URLEncoder.encode(clickedItem, StandardCharsets.UTF_8.toString())
                navController.navigate(route = RouteScreen.WebScreen.route.plus("/$encodedUrl"))

            }
        }
        composable(route = RouteScreen.WebScreen.route.plus("/{encodedItem}")) { backStackEntry ->
            val arguments = backStackEntry.arguments
            val encodedItem = arguments?.getString("encodedItem")
            val decodeUrl = URLDecoder.decode(encodedItem, StandardCharsets.UTF_8.toString())
            WebScreen(decodeUrl)

        }
    }
}