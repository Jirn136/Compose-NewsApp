package com.zoho.news

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.zoho.news.navgraph.NavGraph
import com.zoho.news.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Box(
                        modifier = Modifier.background(
                            brush = Brush.linearGradient(
                                colors = if (isSystemInDarkTheme()) listOf(
                                    Color.DarkGray,
                                    Color.Black
                                ) else
                                    listOf(
                                        Color.LightGray,
                                        Color.White
                                    ),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            )
                        )
                    ) { NavGraph(navController = navController) }
                }
            }
        }
    }
}