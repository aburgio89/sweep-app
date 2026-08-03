package com.example.sweepapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sweepapp.navigation.SweepAppNavGraph
import com.example.sweepapp.ui.theme.SweepAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SweepAppRoot()
        }
    }
}

@Composable
fun SweepAppRoot() {
    SweepAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SweepAppNavGraph()
        }
    }
}