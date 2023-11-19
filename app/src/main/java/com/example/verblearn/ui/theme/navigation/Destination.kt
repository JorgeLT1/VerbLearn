package com.example.verblearn.ui.theme.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String, val icon: ImageVector, val title: String) {
    object Support : Destination(
        route = "Support", icon = Icons.Filled.Person,
        title = "Support"
    )

    object Home : Destination(
        route = "Home", icon = Icons.Filled.Email,
        title = "Home"
    )

    object Favorites : Destination(
        route = "Favorites", icon = Icons.Filled.Email,
        title = "Favorites"
    )

    companion object {
        val toList = listOf(Support, Home, Favorites)
    }
}