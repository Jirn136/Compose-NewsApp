package com.zoho.news.screens.news.airQuality

import QualityLevelIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoho.news.domain.AirQuality
import com.zoho.news.remote.state.LoadingState
import com.zoho.news.utils.Constants
import com.zoho.news.utils.getReadableText
import com.zoho.news.utils.toProgressData
import com.zoho.weatherapp.R

@Composable
fun AirQualityData(
    modifier: Modifier = Modifier,
    airViewModel: AirQualityViewModel
) {
    val airQualityData by airViewModel.airQuality.observeAsState(initial = LoadingState.Loading)
    val airQualityRange = remember {
        mutableStateOf(Constants.EMPTY_STRING)
    }
    val airQualityColor = remember {
        mutableStateOf(Color.Black)
    }
    val context = LocalContext.current

    Row(modifier = modifier) {
        Column(modifier) {
            val text = AnnotatedString.Builder()
            text.apply {
                pushStyle(SpanStyle(fontWeight = FontWeight.Medium, color = Color.Black))
                append(stringResource(R.string.air_quality_at_your_location))
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = airQualityColor.value))
                append(airQualityRange.value)
                pop()
            }
            Text(
                text = text.toAnnotatedString(),
                style = TextStyle(fontSize = 16.sp),
                modifier = modifier
                    .weight(3f)
                    .padding(top = 3.dp, start = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            when (airQualityData) {
                is LoadingState.Loading -> {
                    Text(
                        text = stringResource(R.string.no_data_found),
                        modifier
                            .weight(3f)
                            .align(Alignment.CenterHorizontally),
                        style = TextStyle(fontSize = 13.sp)
                    )
                }

                is LoadingState.Success -> {
                    val progressData =
                        (airQualityData as LoadingState.Success<AirQuality>).data.toProgressData(
                            context = context
                        )
                    val progressUpdatedValue = progressData.getReadableText(context = context)
                    if (airQualityRange.value.isNotEmpty()) airQualityRange.value =
                        Constants.EMPTY_STRING
                    airQualityRange.value = progressUpdatedValue.first
                    airQualityColor.value = progressUpdatedValue.second
                    LazyRow {
                        items(progressData.size) {
                            QualityLevelIndicator(
                                progressData[it],
                                modifier.weight(3f)
                            )
                        }
                    }
                }

                is LoadingState.Error -> {
                    Text(
                        text = stringResource(R.string.unable_to_fetch_data_please_try_again_later),
                        style = TextStyle(fontSize = 13.sp),
                        modifier = modifier
                            .weight(3f)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}