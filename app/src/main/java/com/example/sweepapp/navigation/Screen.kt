package com.example.sweepapp.navigation

//All screens defined here for navigation
//Standard user flow is:
//Login -> Home -> (Settings || Start Sweeps) -> DoomBox -> Confirmation -> Home

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object DoomBoxList : Screen("doom_box_list")
    data object Settings : Screen("settings")
    data object DoomBoxCapture : Screen("doombox_capture")
    data object Confirmation : Screen("confirmation")
    data object SignUp : Screen("sign-up")

    data object AboutSweep : Screen("about_sweep/{pageNumber}") {
        fun createRoute(pageNumber: Int) = "about_sweep/$pageNumber"
    }

    data object Sweep : Screen("sweep/{sweepNumber}") {
        fun createRoute(sweepNumber: Int) = "sweep/$sweepNumber"
    }

    data object DoomBoxReport : Screen("doom_box_report")
}
