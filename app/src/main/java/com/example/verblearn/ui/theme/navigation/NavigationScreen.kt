package com.example.verblearn.ui.theme.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.verblearn.R
import com.example.verblearn.ui.theme.verb.FavoriteScreen
import com.example.verblearn.ui.theme.verb.HomeScreen
import com.example.verblearn.ui.theme.verb.SplashScreen
import com.example.verblearn.ui.theme.verb.SupportScreen
import com.example.verblearn.ui.theme.verb.TranslateScreen
import com.example.verblearn.ui.theme.viewModel.VerbViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            if (currentDestination.value?.destination?.route != Destination.Splash.route) {
                BottomNavigationBar(navController = navController, appItems = Destination.toList)
            }
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                AppNavigation(navController = navController)
            }
        }
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation(navController: NavHostController, viewModel: VerbViewModel = hiltViewModel()) {

    NavHost(
        navController,
        startDestination = Destination.Splash.route,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) },
        popEnterTransition = { fadeIn(animationSpec = tween(500)) },
        popExitTransition = { fadeOut(animationSpec = tween(500)) },
    ) {
        composable(Destination.Splash.route) {
            SplashScreen(navController)
        }
        composable(Destination.Home.route) {
            HomeScreen(navController)
        }
        composable(Destination.Support.route) {
            SupportScreen()
        }
        composable(Destination.Favorites.route) {
            val favorites by viewModel.favorites.collectAsState()
            FavoriteScreen(verbs = favorites, navController)
        }
        composable(
            "${Destination.Translate.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { capturar ->
            val id = capturar.arguments?.getInt("id") ?: 0

            TranslateScreen(idVerb = id)
        }


    }
}

@Composable
fun BottomNavigationBar(navController: NavController, appItems: List<Destination>) {

    BottomAppBar(
        containerColor = colorResource(id = R.color.ColorAplication),
        contentColor = Color.White
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            items(appItems) { appitem ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(appitem.route)
                        }
                        .padding(horizontal = 33.dp)
                ) {
                    Icon(
                        imageVector = appitem.icon,
                        contentDescription = appitem.title
                    )
                    Text(
                        text = appitem.title,
                        color = Color.White
                    )
                }
            }
        }
    }
}