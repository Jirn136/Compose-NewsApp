package com.zoho.news.commons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    isLandScape: Boolean = false,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .then(
                if (isLandScape) {
                    modifier.size(width = 250.dp, height = 350.dp)
                } else modifier
            )
            .size(width = 350.dp, height = 650.dp), elevation = CardDefaults.cardElevation(5.dp)
    ) {
        content()
    }
}