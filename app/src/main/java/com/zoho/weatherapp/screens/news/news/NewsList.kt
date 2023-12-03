package com.zoho.weatherapp.screens.news.news

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.zoho.weatherapp.R
import com.zoho.weatherapp.commons.CustomCard
import com.zoho.weatherapp.domain.News

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsList(
    news: LazyPagingItems<News>, clickedNews: (String) -> Unit, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = news.loadState) {
        if (news.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                (news.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (news.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val lazyListState = rememberLazyListState()
            val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
            val lastItemIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val needProgressBar = lastItemIndex < news.itemCount - 1
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly,
                state = lazyListState,
                flingBehavior = flingBehavior
            ) {
                items(news.itemCount) {
                    news[it]?.let { it1 ->
                        NewsItem(
                            news = it1,
                            state = lazyListState,
                            index = it,
                            context = context,
                            clickedNews = clickedNews
                        )
                    }
                }
                if (needProgressBar) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewsItem(
    news: News,
    modifier: Modifier = Modifier,
    state: LazyListState,
    index: Int,
    context: Context,
    clickedNews: (String) -> Unit
) {
    val scale by remember {
        derivedStateOf {
            val currentItem = state.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                ?: return@derivedStateOf 1.0f
            val halfRowWidth = state.layoutInfo.viewportSize.width / 2
            (1f - minOf(
                1f,
                kotlin.math.abs(currentItem.offset + (currentItem.size / 2) - halfRowWidth)
                    .toFloat() / halfRowWidth
            ) * 0.20f)
        }
    }

    Column(
        modifier = modifier
            .scale(scale)
            .zIndex(scale * 20)
    ) {
        Text(
            text = news.newsSite.toString(),
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(fontSize = 16.sp, textDecoration = TextDecoration.Underline)
        )
        CustomCard(
            modifier = modifier
                .clickable {
                    clickedNews(news.url.toString())
                },
        ) {
            Box {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context).data(news.imageUrl).crossfade(1500)
                        .build(),
                    contentDescription = news.title,
                    contentScale = ContentScale.Crop,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.CenterVertically)
                                .padding(16.dp)
                                .align(Alignment.Center)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    },
                    error = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_error),
                            contentDescription = "Error",
                            modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                        )
                    },
                    modifier = Modifier.fillMaxHeight(0.8f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.DarkGray.copy(alpha = 0.5f), Color.Gray.copy(alpha = 0.3f)
                                ), tileMode = TileMode.Decal, endY = 200F, startY = 0F
                            )
                        )
                ) {
                    Text(
                        text = news.title.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontSize = 26.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(5.dp)
                    )
                }

            }
            Text(
                text = news.summary.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = "Read More...",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 16.sp, textDecoration = TextDecoration.Underline),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.End)
            )
        }
    }

}