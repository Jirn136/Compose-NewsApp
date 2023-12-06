package com.zoho.news.screens.news.news

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.zoho.news.utils.isNetworkAvailable
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

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    if (!context.isNetworkAvailable()) stringResource(id = R.string.no_internet)
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitNewsScreen(
            newsViewModel,
            clickedNews,
            searchText,
            newsList,
            visible,
            focusManager
        )
    } else {
        LandscapeNewsScreen(
            newsViewModel,
            clickedNews, searchText.trim(), newsList, visible, focusManager
        )
    }

}