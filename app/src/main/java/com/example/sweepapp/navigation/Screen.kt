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

    data object Sweep : Screen("sweep/{sweepNumber}") {
        fun createRoute(sweepNumber: Int) = "sweep/$sweepNumber"
    }
}
//Indexed order, 0=Trash, 1=Recycling, etc.
val sweepNames = listOf(
    "Trash",
    "Recycle",
    "Dishes",
    "Laundry",
    "Clutter",
    "Clean",
    "Return"
)
