package com.zoho.weatherapp.screens.news.news

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zoho.weatherapp.R

@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel,
    clickedNews: (String) -> Unit
) {

    val searchText by newsViewModel.searchText.collectAsState()
    val newsList by newsViewModel.news.collectAsState()
    val visible = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.pollution))

    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitNewsScreen(
            newsViewModel,
            clickedNews,
            searchText,
            newsList,
            visible,
            focusManager,
            composition
        )
    } else {
        LandscapeNewsScreen(
            newsViewModel,
            clickedNews, searchText, newsList, visible, focusManager, composition
        )
    }

}