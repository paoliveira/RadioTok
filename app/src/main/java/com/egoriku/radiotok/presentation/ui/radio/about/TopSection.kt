package com.egoriku.radiotok.presentation.ui.radio.about

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.egoriku.radiotok.R
import com.google.accompanist.coil.CoilImage

@Composable
fun TopSection(logoUrl: String, title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioLogo(logoUrl)

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            fontFamily = FontFamily(Font(resId = R.font.khula_semibold)),
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun RadioLogo(logoUrl: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(300.dp)
            .shadow(30.dp, CircleShape)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = CircleShape
            )
            .border(10.dp, MaterialTheme.colors.primary, CircleShape)

    ) {
        CoilImage(
            fadeIn = true,
            data = logoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp),
            error = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_radio),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = null
                )
            }
        )
    }
}