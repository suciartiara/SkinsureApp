package com.example.skinsure.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.skinsure.presentation.navigation.Screen
import com.example.skinsure.presentation.screens.LoginScreen
import com.example.skinsure.presentation.navigation.SkinSureAppRouter
import com.example.skinsure.presentation.screens.SignUpScreen
import com.example.skinsure.presentation.screens.TermsAndConditionsScreen

@Composable
fun SkinSureApp(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Crossfade(targetState = SkinSureAppRouter.currentScreen) { currentState->
            when(currentState.value){
                is Screen.SignUpScreen ->{
                    SignUpScreen()
                }
                is Screen.TermsAndConditionsScreen -> {
                    TermsAndConditionsScreen()
                }
                is Screen.LoginScreen ->{
                    LoginScreen()
                }
            }
        }

    }
    
}
