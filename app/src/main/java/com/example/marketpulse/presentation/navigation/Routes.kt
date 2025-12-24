package com.example.marketpulse.presentation.navigation

import com.example.marketpulse.domain.model.AssetType

object Routes {
    const val Splash = "splash"
    const val Home = "home"
    const val Search = "search"
    const val Settings = "settings"

    const val Detail = "detail/{type}/{symbol}"

    fun detail(type: AssetType, symbol: String): String =
        "detail/${type.name}/${symbol}"
}
