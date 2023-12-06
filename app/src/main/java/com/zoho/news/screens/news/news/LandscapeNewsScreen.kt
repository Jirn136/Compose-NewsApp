package com.zoho.news.screens.news.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.zoho.news.commons.CustomCard
import com.zoho.news.domain.News
import com.zoho.news.screens.news.airQuality.AirQualityScreen
import com.zoho.weatherapp.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LandscapeNewsScreen(
    newsViewModel: NewsViewModel,
    clickedNews: (String) -> Unit,
    searchText: String,
    newsList: Flow<PagingData<News>>,
    visible: MutableState<Boolean>,
    focusManager: FocusManager
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.4f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 10.dp)
            ) {
                AirQualityScreen(modifier = Modifier)
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                    .onFocusChanged { focus ->
                        if (!focus.isFocused) {
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                        }

                    },
                value = searchText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                }),
                onValueChange = {
                    newsViewModel.searchNews(it)
                    visible.value = it.isNotEmpty()
                },
                placeholder = {
                    Text(text = stringResource(R.string.search))
                },
                trailingIcon = {
                    IconButton(onClick = {
                        newsViewModel.clearSearchText()
                        visible.value = false
                    }) {
                        if (visible.value) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = stringResource(
                                    R.string.clear
                                )
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                },
                shape = RoundedCornerShape(25.dp),
            )
        }

        NewsList(news = newsList.collectAsLazyPagingItems(), clickedNews = clickedNews)
    }

}