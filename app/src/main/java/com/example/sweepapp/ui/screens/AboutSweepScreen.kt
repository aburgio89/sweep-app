package com.example.sweepapp.ui.screens

import android.graphics.Paint.Align
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sweepapp.ui.theme.AboutSweepStyle
import com.example.sweepapp.ui.theme.SweepBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AboutSweepScreen(
    pageNumber: Int,
    onNext: () -> Unit,
    onClose: () -> Unit
) {
    val page = aboutSweepPages[pageNumber - 1]
    val isLastPage = pageNumber == aboutSweepPages.size

    var canAdvance by remember(pageNumber) { mutableStateOf(false) }
    LaunchedEffect(pageNumber) {
        delay(1000)
        canAdvance = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SweepBackground)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = canAdvance,
                onClick = if (isLastPage) onClose else onNext)
    ) {
        when (page) {
            is SingleTextPage -> {
                Image(
                    painter = painterResource(id = page.backgroundRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                SingleTextContent(page)
            }
            is CenteredTextPage -> {
                CenteredTextContent(page)
            }
            is WordCascadePage -> {
                WordCascadeContent(page, pageNumber)
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 15.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}

@Composable
private fun SingleTextContent(page: SingleTextPage) {
    var textVisible by remember { mutableStateOf(false) }
    LaunchedEffect(page) { textVisible = true }

    AnimatedVisibility(
        visible = textVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 24.dp, end = 24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter) {
                Text(
                    text = stringResource(id = page.textRes),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = AboutSweepStyle,
                    modifier = Modifier.padding(top = 40.dp)
                )
            }
        }
}

@Composable
private fun CenteredTextContent(page: CenteredTextPage) {
    var textVisible by remember { mutableStateOf(false) }
    LaunchedEffect(page) { textVisible = true }

    AnimatedVisibility(
        visible = textVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = page.textRes),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = AboutSweepStyle
            )
        }
    }
}

@Composable
private fun WordCascadeContent(page: WordCascadePage, pageNumber: Int) {
    val alphaStates = remember(pageNumber) { page.words.map { Animatable(0f) } }

    LaunchedEffect(pageNumber) {
        delay(400)
        alphaStates.forEach { anim ->
            launch { anim.animateTo(1f, animationSpec = tween(durationMillis = 600))}
            delay(500)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterVertically)
    ) {
        page.words.forEachIndexed { index, word ->
            Text(
                text = stringResource(id = word.textRes),
                color = Color.White,
                style = AboutSweepStyle,
                textAlign = if (word.align == WordAlign.Start) TextAlign.Start else TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .graphicsLayer { alpha = alphaStates[index].value}
                )
            }
        }
}
