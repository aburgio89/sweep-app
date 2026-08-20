package com.example.sweepapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sweepapp.R
import com.example.sweepapp.data.SweepCategory
import com.example.sweepapp.ui.theme.SweepBackground

@Composable
fun SweepScreen(
    category: SweepCategory,
    sweepNumber: Int,
    totalSweeps: Int,
    onComplete: () -> Unit,
    onCancel: () -> Unit
) {
    val isLastSweep = sweepNumber == totalSweeps
    var showFaqDialog by remember { mutableStateOf(false) }
    val accentColor = Color(android.graphics.Color.parseColor(category.colorHex))
    val doneButtonTextColor = if (accentColor.luminance() > 0.5f) Color.Black else Color.White

    ScreenScaffold(
        title = "Sweep $sweepNumber: ${ category.name }",
        titleContent = {
            Row{
            Text(text = "Sweep $sweepNumber: ", style = MaterialTheme.typography.headlineMedium)
            Text(text = category.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.sweep_monster),
            contentDescription = null,
            colorFilter = ColorFilter.tint(accentColor),
            alignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        )

        Text(
            text = category.description,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sweep $sweepNumber of $totalSweeps",
            textAlign = TextAlign.Center
        )
        LinearProgressIndicator(
            progress = { sweepNumber / totalSweeps.toFloat() },
            color = accentColor,
            modifier = Modifier.fillMaxWidth().height(8.dp)
                .padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedButton(
            onClick = { showFaqDialog = true },
            shape = CircleShape,
            border = BorderStroke(4.dp, accentColor),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
            modifier = Modifier.size(60.dp)
        ) {
            Text("?", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancel,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(4.dp, accentColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Cancel Sweeps",
                    tint = accentColor,
                    modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onComplete,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = doneButtonTextColor
                ),
                modifier = Modifier.weight(1f).height(64.dp)
            ) {
                Text(if (isLastSweep) "Complete!" else "Next!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,)
            }
        }
    }

    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            title = { Text("Sweep $sweepNumber: ${category.name}") },
            text = { Text(category.faq) },
            confirmButton = {
                TextButton(onClick = { showFaqDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )) {
                    Text("Continue")
                }
            },
            titleContentColor = SweepBackground
        )
    }
}