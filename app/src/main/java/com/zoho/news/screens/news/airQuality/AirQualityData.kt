package com.zoho.news.screens.news.airQuality

import QualityLevelIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zoho.news.domain.AirQuality
import com.zoho.news.remote.state.LoadingState
import com.zoho.news.utils.toProgressData

@Composable
fun AirQualityData(
    modifier: Modifier = Modifier,
    airViewModel: AirQualityViewModel,
    hitAirQualityData: () -> Unit
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
            when (airQualityData) {
                is LoadingState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoadingState.Success -> {
                    val progressData =
                        (airQualityData as LoadingState.Success<AirQuality>).data.toProgressData()
                    LazyRow {
                        items(progressData.size) {
                            QualityLevelIndicator(progressData[it])
                        }
                    }
                }

                is LoadingState.Error -> {
                    Text(text = "Unable to fetch data, Please try again later.")
                }
            }
        }
        Spacer(modifier = modifier.fillMaxWidth())
        IconButton(
            onClick = { hitAirQualityData() },
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