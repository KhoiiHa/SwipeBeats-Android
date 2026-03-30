package com.khoi.swipebeats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.khoi.swipebeats.ui.theme.SwipeBeatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SwipeBeatsTheme {
                SwipeBeatsApp()
            }
        }
    }
}