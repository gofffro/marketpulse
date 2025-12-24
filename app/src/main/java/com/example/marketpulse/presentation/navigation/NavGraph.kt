package com.example.marketpulse.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marketpulse.core.util.AppContainer
import com.example.marketpulse.domain.model.AssetType
import com.example.marketpulse.presentation.screen_detail.DetailScreen
import com.example.marketpulse.presentation.screen_home.HomeScreen
import com.example.marketpulse.presentation.screen_search.SearchScreen
import com.example.marketpulse.presentation.screen_settings.SettingsScreen
import com.example.marketpulse.presentation.screen_home.SplashScreen

@Composable
fun AppNavGraph(
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Routes.Splash,
        modifier = modifier
    ) {
        composable(Routes.Splash) {
            SplashScreen(
                container = container,
                onGoHome = { nav.navigate(Routes.Home) { popUpTo(Routes.Splash) { inclusive = true } } }
            )
        }

        composable(Routes.Home) {
            HomeScreen(
                container = container,
                onOpenSearch = { nav.navigate(Routes.Search) },
                onOpenSettings = { nav.navigate(Routes.Settings) },
                onOpenDetail = { type, symbol -> nav.navigate(Routes.detail(type, symbol)) }
            )
        }

        composable(Routes.Search) {
            SearchScreen(
                container = container,
                onBack = { nav.popBackStack() },
                onOpenDetail = { type, symbol -> nav.navigate(Routes.detail(type, symbol)) }
            )
        }

        composable(
            route = Routes.Detail,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("symbol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val typeRaw = backStackEntry.arguments?.getString("type").orEmpty()
            val symbol = backStackEntry.arguments?.getString("symbol").orEmpty()
            val type = runCatching { AssetType.valueOf(typeRaw) }.getOrDefault(AssetType.STOCK)

            DetailScreen(
                container = container,
                type = type,
                symbol = symbol,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.Settings) {
            SettingsScreen(
                container = container,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
