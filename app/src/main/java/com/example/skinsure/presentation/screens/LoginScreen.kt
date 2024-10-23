package com.example.skinsure.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skinsure.presentation.components.ButtonComponent
import com.example.skinsure.presentation.components.ClickableLoginTextComponent
import com.example.skinsure.presentation.components.DividerTextComponent
import com.example.skinsure.presentation.components.HeadingTextComponent
import com.example.skinsure.presentation.components.MyTextFieldComponent
import com.example.skinsure.presentation.components.NormalTextComponent
import com.example.skinsure.presentation.components.PasswordTextFieldComponent
import com.example.skinsure.presentation.components.UnderLinedTextComponent
import com.example.skinsure.presentation.navigation.Screen
import com.example.skinsure.presentation.navigation.SkinSureAppRouter
import com.example.skinsure.presentation.navigation.SystemBackButtonHandler
import com.example.skinsure.R

//import com.nativemobilebits.loginflow.R
//import com.nativemobilebits.loginflow.components.*
//import com.nativemobilebits.loginflow.navigation.PostOfficeAppRouter
//import com.nativemobilebits.loginflow.navigation.Screen
//import com.nativemobilebits.loginflow.navigation.SystemBackButtonHandler

@Composable
fun LoginScreen() {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            NormalTextComponent(value = stringResource(id = R.string.login))
            HeadingTextComponent(value = stringResource(id = R.string.welcome))
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.message),
                onTextSelected = {})

            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.lock),
                onTextSelected = {}
            )

            Spacer(modifier = Modifier.height(40.dp))
            UnderLinedTextComponent(value = stringResource(id = R.string.forgot_password))

            Spacer(modifier = Modifier.height(40.dp))

            ButtonComponent(value = stringResource(id = R.string.login),
                onButtonClicked = {})

            Spacer(modifier = Modifier.height(20.dp))

            DividerTextComponent()

            ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                SkinSureAppRouter.navigateTo(Screen.SignUpScreen)
            })
        }
    }

    SystemBackButtonHandler {
        SkinSureAppRouter.navigateTo(Screen.SignUpScreen)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}