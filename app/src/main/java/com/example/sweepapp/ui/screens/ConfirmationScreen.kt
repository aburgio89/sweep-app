package com.example.sweepapp.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.sweepapp.R
import com.example.sweepapp.ui.theme.SweepAppTheme
import com.example.sweepapp.ui.theme.SweepBackground
import com.example.sweepapp.ui.theme.SweepPrimary

@Composable
fun ConfirmationScreen(
    wasFullSweep: Boolean,
    onReturnHome: () -> Unit
) {
    val title = if (wasFullSweep) "Full Sweep" else "Partial Sweep"
    val message = if (wasFullSweep) {
        "Congrats - full sweep complete!"
    } else {
        "Nice job on your progress so far!"
    }
    val bounceOffset = remember { Animatable(-100f) }

    LaunchedEffect(wasFullSweep) {
        if (wasFullSweep){
            repeat(3) {
                bounceOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }

    ScreenScaffold(title = title) {
        if(wasFullSweep) {
            Image(
                painter = painterResource(id = R.drawable.sweepmonster_happy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(SweepPrimary),
                modifier = Modifier
                    .size(200.dp)
                    .offset { IntOffset(0, bounceOffset.value.toInt()) }
            )
        }

        Text(
            text = message,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Return to Home", color = SweepBackground, fontWeight = Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmPreview() {
    SweepAppTheme {
        ConfirmationScreen(
            wasFullSweep = true,
            onReturnHome = {}
        )
    }
}