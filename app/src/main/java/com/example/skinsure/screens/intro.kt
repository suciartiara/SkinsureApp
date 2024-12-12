package com.example.skinsure.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skinsure.R
import com.example.skinsure.viewModel.LogInViewModel
import com.example.skinsure.viewModel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun Intro(navHostController: NavHostController, signUpViewModel: SignUpViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel()){
    var isIntro by remember {
        mutableStateOf(true)
    }
    val coroutine = rememberCoroutineScope()
    val status by logInViewModel.isLoggedin.collectAsState()
    val loading by logInViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit){
        signUpViewModel.setBackToFalse()
    }

    LaunchedEffect(status){
        if (status){
            navHostController.navigate("home")
        }
    }

    BackHandler {
        if (isIntro == false){
            isIntro = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.White)) {
        Column(Modifier.align(Alignment.BottomCenter)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(475.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                    .background(
                        color = Color(0XFFf5efef),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ), contentAlignment = if (isIntro) Alignment.TopCenter else Alignment.Center) {
                if (isIntro){
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Welcome to SkinSure", color = Color.Black, fontWeight = FontWeight.W900, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(100.dp))
                        Text(text = "Scan and ensure your beauty products are safe", textAlign = TextAlign.Center, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 100.dp))
                        Spacer(modifier = Modifier.height(30.dp))

                    }
                } else {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        textFieldBox(width = 280, desc = "email", value = signUpViewModel.signUpState.value.email, onValueChange = { signUpViewModel.setEmail(it) })
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldPasswordBox(width = 240, desc = "password", value = signUpViewModel.signUpState.value.password, onValueChange = { signUpViewModel.setPassword(it) })
                        Spacer(modifier = Modifier.height(20.dp))
                        if (loading){
                            Box(
                                Modifier
                                    .width(130.dp)
                                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                                    .height(50.dp)
                                    .background(
                                        color = Color(0XFFc79898),
                                        shape = RoundedCornerShape(20.dp)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.Gray, strokeWidth = 4.dp)
                            }
                        } else {
                            Box(
                                Modifier
                                    .width(130.dp)
                                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                                    .clickable {
                                        if (signUpViewModel.signUpState.value.emailError.isEmpty() && signUpViewModel.signUpState.value.passwordError.isEmpty()) {
                                            coroutine.launch {
                                                logInViewModel.logIn(
                                                    email = signUpViewModel.signUpState.value.email,
                                                    password = signUpViewModel.signUpState.value.password
                                                )
                                            }
                                        }
                                    }
                                    .height(50.dp)
                                    .background(
                                        color = Color(0XFFc79898),
                                        shape = RoundedCornerShape(20.dp)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Login", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }

            }
        }
        if(isIntro){
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)) {
                Box(
                    Modifier
                        .width(300.dp)
                        .height(65.dp)
                        .clickable {
                            isIntro = false
                        }
                        .background(
                            color = Color(0XFFC79898),
                            shape = RoundedCornerShape(50.dp)
                        ), contentAlignment = Alignment.Center) {
                    Text(text = "Get Started", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)) {
                Text(text = "Dont have an account? ", color = Color(0XFF5e6a73), fontSize = 16.sp)
                Text(text = "Sign Up", color = Color(0XFF5e6a73), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                    navHostController.navigate("signUp")
                })
            }
        }
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 30.dp)
                .offset(y = 110.dp)) {
            Box(
                Modifier
                    .width(300.dp)
                    .height(150.dp), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "", contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){
    Box(
        Modifier
            .width(300.dp)
            .height(60.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White, shape = RoundedCornerShape(7.dp))
            .border(width = 2.dp, color = Color(0XFF5e6a73), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 2.dp, horizontal = 10.dp)) {
        TextField(value = value, onValueChange = { onValueChange(it)},
            modifier = Modifier
                .width(width.dp)
                .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            ), textStyle = TextStyle(color = Color.Black, fontSize = 13.sp), placeholder = {
                Text(text = desc, color = Color.Gray, fontSize = 13.sp)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldPasswordBox(width: Int, desc: String, value: String, onValueChange: (String) -> Unit){


    var isPasswordVisible = remember { mutableStateOf(false) }

    Box(
        Modifier
            .width(300.dp)
            .height(60.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .border(width = 2.dp, color = Color(0XFF5e6a73), shape = RoundedCornerShape(20.dp))
            .padding(vertical = 2.dp, horizontal = 10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = value, onValueChange = { onValueChange(it)},
                visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .width(width.dp)
                    .height(70.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ), textStyle = TextStyle(color = Color.Black, fontSize = 13.sp), placeholder = {
                    Text(text = desc, color = Color.Gray, fontSize = 13.sp)

                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(imageVector = if (isPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = "", tint = Color(0XFF0A4635))
            }
        }

    }
}