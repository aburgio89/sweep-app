package com.example.sweepapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sweepapp.data.AccountSettingsRepository
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.data.AuthRepository
import com.example.sweepapp.ui.screens.AccountSettingsScreen
import com.example.sweepapp.ui.screens.ConfirmationScreen
import com.example.sweepapp.ui.screens.DoomBoxCaptureScreen
import com.example.sweepapp.ui.screens.DoomBoxListScreen
import com.example.sweepapp.ui.screens.HomeScreen
import com.example.sweepapp.ui.screens.LoginScreen
import com.example.sweepapp.ui.screens.SignUpScreen
import com.example.sweepapp.ui.screens.SweepScreen

@Composable
fun SweepAppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (AuthRepository.isLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        //LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, password -> AuthRepository.signIn(email, password) },
                onLoginSuccess = {
                    AccountSettingsRepository.updateEmailFromAuth(AuthRepository.currentUserEmail ?:"")
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUp = { email, password -> AuthRepository.signUp(email, password)},
                onSignUpSuccess = {
                    AccountSettingsRepository.updateEmailFromAuth(AuthRepository.currentUserEmail ?: "")
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        //HOME
        composable(Screen.Home.route) {
            HomeScreen(
                onStartSweeps = {
                    navController.navigate(Screen.Sweep.createRoute(1))
                },
                onOpenSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onViewDoomBox = {
                    navController.navigate(Screen.DoomBoxList.route)
                }
            )
        }

        //SETTINGS
        composable(Screen.Settings.route) {
            AccountSettingsScreen(
                onBack = { navController.popBackStack() },
                onSignedOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        //DOOM LIST
        composable(Screen.DoomBoxList.route) {
            DoomBoxListScreen(
                onBack = { navController.popBackStack() }
            )
        }

        //SWEEP
        composable(Screen.Sweep.route,
        arguments = listOf(navArgument("sweepNumber") {type = NavType.IntType})) {
            backStackEntry ->
            val sweepNumber = backStackEntry.arguments?.getInt("sweepNumber") ?: 1
            SweepScreen(
                sweepNumber = sweepNumber,
                sweepName = sweepNames[sweepNumber -1],
                totalSweeps = sweepNames.size,
                onComplete = {
                    if (sweepNumber < sweepNames.size) {
                        navController.navigate(Screen.Sweep.createRoute(sweepNumber + 1)) {
                            popUpTo(Screen.Sweep.createRoute(sweepNumber)) {inclusive = true}
                        }
                    }
                    else {
                        navController.navigate(Screen.DoomBoxCapture.route + "?full=true") {
                            popUpTo(Screen.Home.route) {inclusive = false}
                        }
                    }
                },
                onCancel = {
                    navController.navigate(Screen.DoomBoxCapture.route + "?full=false") {
                        popUpTo(Screen.Home.route) {inclusive=false}
                    }
                }
            )
        }

        //DOOM BOX
        composable(
            route = Screen.DoomBoxCapture.route + "?full={full}",
            arguments = listOf(navArgument("full") {
                type = NavType.BoolType
                defaultValue = true
            })
        ) { backStackEntry ->
            val wasFullSweep = backStackEntry.arguments?.getBoolean("full") ?: true

            LaunchedEffect(backStackEntry) {
                if (wasFullSweep) {
                    AppDataRepository.recordFullSweepCompleted()
                }
            }

            DoomBoxCaptureScreen(
                onDone = {
                    navController.navigate(Screen.Confirmation.route + "?full=$wasFullSweep")
                }
            )
        }

        //CONFIRMATION
        composable(
            route = Screen.Confirmation.route + "?full={full}",
            arguments = listOf(navArgument("full"){
                type = NavType.BoolType
                defaultValue = true
            })
        ) {
            backStackEntry ->
            val wasFullSweep = backStackEntry.arguments?.getBoolean("full") ?: true
            ConfirmationScreen(
                wasFullSweep = wasFullSweep,
                onReturnHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {inclusive = true}
                    }
                }
            )
        }
    }
}