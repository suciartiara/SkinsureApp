package com.example.skinsure.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skinsure.R
import com.example.skinsure.viewModel.SignUpViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUp(signUpViewModel: SignUpViewModel = viewModel(), navHostController: NavHostController){
    val loading by signUpViewModel.isLoading.collectAsState()
    val status by signUpViewModel.isSignUpSuccess.collectAsState()
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(status){
        if (status){
            navHostController.navigate("intro")
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
                    .height(575.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                    .background(
                        color = Color(0XFFf5efef),
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ), contentAlignment = Alignment.Center) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        textFieldBox(width = 280, desc = "email", value = signUpViewModel.signUpState.value.email, onValueChange = { signUpViewModel.setEmail(it) })
                        if (signUpViewModel.signUpState.value.emailError.isNotEmpty()){
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(25.dp), contentAlignment = Alignment.CenterStart) {
                                Text(text = signUpViewModel.signUpState.value.emailError, color = Color(0XFF5e6a73), textAlign = TextAlign.Start, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        textFieldBox(width = 280, desc = "name", value = signUpViewModel.signUpState.value.name, onValueChange = { signUpViewModel.setName(it) })
                        if (signUpViewModel.signUpState.value.nameError.isNotEmpty()){
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(25.dp), contentAlignment = Alignment.CenterStart) {
                                Text(text = signUpViewModel.signUpState.value.nameError, color = Color(0XFF5e6a73), textAlign = TextAlign.Start, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        TextFieldPasswordBox(width = 240, desc = "password", value = signUpViewModel.signUpState.value.password, onValueChange = { signUpViewModel.setPassword(it) })
                        if (signUpViewModel.signUpState.value.passwordError.isNotEmpty()){
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(25.dp), contentAlignment = Alignment.CenterStart) {
                                Text(text = signUpViewModel.signUpState.value.passwordError, color = Color(0XFF5e6a73), textAlign = TextAlign.Start, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        TextFieldPasswordBox(width = 240, desc = "confirm password", value = signUpViewModel.signUpState.value.confirmPassword, onValueChange = { signUpViewModel.setConfirmPassword(it) })
                        if (signUpViewModel.signUpState.value.confirmPasswordError.isNotEmpty()){
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(25.dp), contentAlignment = Alignment.CenterStart) {
                                Text(text = signUpViewModel.signUpState.value.confirmPasswordError, color = Color(0XFF5e6a73), textAlign = TextAlign.Start, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        if(loading){
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
                                CircularProgressIndicator(color = Color.Gray, strokeWidth = 4.dp, modifier = Modifier.size(30.dp))
                            }
                        } else {
                            Box(
                                Modifier
                                    .width(130.dp)
                                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                                    .clickable {
                                        coroutine.launch {
                                            if (signUpViewModel.validateAllFields()) {
                                                signUpViewModel.signUp(
                                                    name = signUpViewModel.signUpState.value.name,
                                                    email = signUpViewModel.signUpState.value.email,
                                                    password = signUpViewModel.signUpState.value.confirmPassword
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
                                Text(text = "Sign Up", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }

            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp)) {
                Text(text = "Already have an account? ", color = Color(0XFF5e6a73), fontSize = 16.sp)
                Text(text = "Log In", color = Color(0XFF5e6a73), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {
                    navHostController.navigate("intro")
                })
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