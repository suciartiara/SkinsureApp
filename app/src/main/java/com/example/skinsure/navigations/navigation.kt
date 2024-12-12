package com.example.skinsure.navigations

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skinsure.factory.LogInViewModelFactory
import com.example.skinsure.screens.DetailHistory
import com.example.skinsure.screens.DetailIngredients
import com.example.skinsure.screens.IngredientList
import com.example.skinsure.screens.IngredientScanScreen
import com.example.skinsure.screens.Intro
import com.example.skinsure.screens.Profile
import com.example.skinsure.screens.Scan
import com.example.skinsure.screens.SignUp
import com.example.skinsure.viewModel.IngredientViewModel
import com.example.skinsure.viewModel.IngredientsDetailViewModel
import com.example.skinsure.viewModel.LogInViewModel
import com.example.skinsure.viewModel.SignUpViewModel

@Composable
fun navigation(){
    val context = LocalContext.current
    val navController = rememberNavController()
    val IngredientViewModel : IngredientViewModel = viewModel()
    val IngredientsDetailViewModel : IngredientsDetailViewModel = viewModel()
    val signUpViewModel : SignUpViewModel = viewModel()
    val logInViewModel : LogInViewModel = viewModel(factory = LogInViewModelFactory(context))

    val isLoggedIn by logInViewModel.isLoggedin.collectAsState()
    
    NavHost(navController = navController, startDestination = if(isLoggedIn) "home" else "intro"){
        composable("home") { IngredientScanScreen(viewModel = IngredientViewModel, ingredientsDetailViewModel = IngredientsDetailViewModel, navController = navController, logInViewModel = logInViewModel) }
        composable("detailIngredient") { DetailIngredients(ingredientsDetailViewModel = IngredientsDetailViewModel, navController = navController) }
        composable("scan") { Scan(navController = navController, ingredientViewModel = IngredientViewModel) }
        composable("list") { IngredientList(navController = navController, logInViewModel = logInViewModel, ingredientViewModel = IngredientViewModel, ingredientsDetailViewModel = IngredientsDetailViewModel) }
        composable("intro") { Intro(navHostController = navController, signUpViewModel = signUpViewModel, logInViewModel = logInViewModel) }
        composable("signUp") { SignUp(navHostController = navController, signUpViewModel = signUpViewModel) }
        composable("profile") { Profile(logInViewModel = logInViewModel, navController = navController, ingredientViewModel = IngredientViewModel) }
        composable("detailHistory") { DetailHistory(ingredientsDetailViewModel = IngredientsDetailViewModel, ingredientViewModel = IngredientViewModel, navtController = navController) }
    }
}