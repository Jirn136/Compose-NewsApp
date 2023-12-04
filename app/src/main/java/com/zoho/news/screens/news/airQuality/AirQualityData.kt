package com.zoho.news.screens.news.airQuality

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zoho.news.remote.state.LoadingState

@Composable
fun AirQualityData(
    modifier: Modifier = Modifier,
    airViewModel: AirQualityViewModel,
    requestPermission: () -> Unit
) {
    val airQualityData by airViewModel.airQuality.observeAsState(initial = LoadingState.Loading)

    Row(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Air Pollution at your location",
                modifier = modifier.padding(bottom = 3.dp)
            )
            Text(text = "Air Item")
        }
        Spacer(modifier = modifier.fillMaxWidth())
        IconButton(
            onClick = { requestPermission() },
            modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh",
                modifier = modifier.size(15.dp)
            )
        }
    }
}