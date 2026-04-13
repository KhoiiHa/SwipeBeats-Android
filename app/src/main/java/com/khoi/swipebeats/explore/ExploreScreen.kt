package com.khoi.swipebeats.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khoi.swipebeats.explore.ExploreUiState

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier
) {
    val query = remember { mutableStateOf("") }
    val uiState = remember { mutableStateOf<ExploreUiState>(ExploreUiState.Empty) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Explore")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for songs or artists") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState.value) {
            ExploreUiState.Empty -> {
                Text(text = "No results yet")
            }

            ExploreUiState.Loading -> {
                Text(text = "Loading...")
            }

            is ExploreUiState.Error -> {
                Text(text = state.message)
            }

            ExploreUiState.Content -> {
                Text(text = "Track list coming soon")
            }
        }
    }
}