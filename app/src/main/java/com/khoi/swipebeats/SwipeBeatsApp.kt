package com.khoi.swipebeats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Simple screen enum for MVP (no Navigation Compose yet)
enum class BottomTab {
    SWIPE, EXPLORE, FAVORITES, PLAYLISTS
}

@Composable
fun SwipeBeatsApp() {

    var selectedTab by remember { mutableStateOf(BottomTab.EXPLORE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == BottomTab.SWIPE,
                    onClick = { selectedTab = BottomTab.SWIPE },
                    icon = { Icon(Icons.Outlined.PlayArrow, contentDescription = "Swipe") },
                    label = { Text("Swipe") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.EXPLORE,
                    onClick = { selectedTab = BottomTab.EXPLORE },
                    icon = { Icon(Icons.Outlined.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.FAVORITES,
                    onClick = { selectedTab = BottomTab.FAVORITES },
                    icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = selectedTab == BottomTab.PLAYLISTS,
                    onClick = { selectedTab = BottomTab.PLAYLISTS },
                    icon = { Icon(Icons.Outlined.LibraryMusic, contentDescription = "Playlists") },
                    label = { Text("Playlists") }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when (selectedTab) {
                BottomTab.SWIPE -> Text("Swipe Screen")
                BottomTab.EXPLORE -> ExploreScreen()
                BottomTab.FAVORITES -> Text("Favorites Screen")
                BottomTab.PLAYLISTS -> Text("Playlists Screen")
            }
        }
    }
}