package com.example.sweepapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sweepapp.data.AccountSettingsRepository
import com.example.sweepapp.data.AppDataRepository
import com.example.sweepapp.data.AuthRepository
import com.example.sweepapp.data.SweepCategoryRepository
import com.example.sweepapp.ui.screens.AboutSweepScreen
import com.example.sweepapp.ui.screens.AccountSettingsScreen
import com.example.sweepapp.ui.screens.ConfirmationScreen
import com.example.sweepapp.ui.screens.DoomBoxCaptureScreen
import com.example.sweepapp.ui.screens.DoomBoxListScreen
import com.example.sweepapp.ui.screens.DoomBoxReportScreen
import com.example.sweepapp.ui.screens.HomeScreen
import com.example.sweepapp.ui.screens.LoginScreen
import com.example.sweepapp.ui.screens.SignUpScreen
import com.example.sweepapp.ui.screens.SweepScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SweepAppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val startDestination = if (AuthRepository.isLoggedIn) Screen.Home.route else Screen.Login.route

    LaunchedEffect(Unit) {
        AuthRepository.currentUserId?.let { uid ->
            AppDataRepository.start(uid)
            AccountSettingsRepository.start(uid)
            SweepCategoryRepository.start()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        //LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, password -> AuthRepository.signIn(email, password) },
                onLoginSuccess = {
                    AuthRepository.currentUserId?.let { uid ->
                        AppDataRepository.start(uid)
                        AccountSettingsRepository.start(uid)
                        SweepCategoryRepository.start()
                    }
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
                    AuthRepository.currentUserId?. let { uid ->
                        AppDataRepository.start(uid)
                        AccountSettingsRepository.start(uid)
                        SweepCategoryRepository.start()
                    }
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
                },
                onViewAboutSweep = {
                    navController.navigate(Screen.AboutSweep.createRoute(1))
                }
            )
        }

        //SETTINGS
        composable(Screen.Settings.route) {
            AccountSettingsScreen(
                onBack = { navController.popBackStack() },
                onSignedOut = {
                    AppDataRepository.stop()
                    AccountSettingsRepository.stop()
                    SweepCategoryRepository.stop()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        //ABOUT
        composable(Screen.AboutSweep.route,
            arguments = listOf(navArgument("pageNumber") { type = NavType.IntType})
        ) {
            backStackEntry ->
            val pageNumber = backStackEntry.arguments?.getInt("pageNumber") ?: 1
            AboutSweepScreen(
                pageNumber = pageNumber,
                onNext = {
                    navController.navigate(Screen.AboutSweep.createRoute(pageNumber + 1)) {
                        popUpTo(Screen.AboutSweep.createRoute(pageNumber)) { inclusive = true }
                    }
                },
                onClose = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        //DOOM LIST
        composable(Screen.DoomBoxList.route) {
            DoomBoxListScreen(
                onBack = { navController.popBackStack() },
                onViewReport = { navController.navigate(Screen.DoomBoxReport.route) }
            )
        }

        //DOOM REPORT
        composable(Screen.DoomBoxReport.route) {
            DoomBoxReportScreen(
                onBack = { navController.popBackStack() }
            )
        }

        //SWEEP
        composable(route = Screen.Sweep.route,
        arguments = listOf(navArgument("sweepNumber") {type = NavType.IntType})) {
            backStackEntry ->
            val sweepNumber = backStackEntry.arguments?.getInt("sweepNumber") ?: 1
            val categories by SweepCategoryRepository.categories.collectAsState()

            if (categories.isEmpty() || sweepNumber > categories.size) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {inclusive = true}
                    }
                }
            }
            else {
                SweepScreen(
                    category = categories[sweepNumber - 1],
                    sweepNumber = sweepNumber,
                    totalSweeps = categories.size,
                    onComplete = {
                        if (sweepNumber < categories.size) {
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