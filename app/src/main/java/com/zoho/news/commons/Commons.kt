package com.zoho.news.commons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import com.zoho.weatherapp.R

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


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

fun customFontFamily(): FontFamily =
    FontFamily(
        Font(
            googleFont = GoogleFont("Roboto Slab"),
            fontProvider = provider
        )
    )
