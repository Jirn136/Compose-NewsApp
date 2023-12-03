package com.zoho.weatherapp.screens.news.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import com.zoho.weatherapp.commons.CustomCard
import com.zoho.weatherapp.domain.News
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PortraitNewsScreen(
    newsViewModel: NewsViewModel,
    clickedNews: (String) -> Unit,
    searchText: String,
    newsList: Flow<PagingData<News>>,
    visible: MutableState<Boolean>,
    focusManager: FocusManager,
    composition: LottieComposition?
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .size(150.dp)
                )

                Text(
                    text = "Check air pollution on your area", style = TextStyle(
                        fontSize = 16.sp, color = Color.Gray
                    )
                )
            }

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
                Text(text = "Search...")
            },
            trailingIcon = {
                IconButton(onClick = {
                    newsViewModel.clearSearchText()
                    visible.value = false
                }) {
                    if (visible.value) {
                        Icon(
                            imageVector = Icons.Rounded.Clear, contentDescription = "Clear"
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search, contentDescription = "Search"
                )
            },
            shape = RoundedCornerShape(25.dp),
        )

        NewsList(news = newsList.collectAsLazyPagingItems(), clickedNews = clickedNews)
    }
}