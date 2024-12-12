package com.example.skinsure.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skinsure.R
import com.example.skinsure.factory.LogInViewModelFactory
import com.example.skinsure.viewModel.IngredientViewModel
import com.example.skinsure.viewModel.LogInViewModel
import kotlinx.coroutines.launch

@Composable
fun Profile(logInViewModel: LogInViewModel = viewModel(), ingredientViewModel: IngredientViewModel = viewModel(), navController: NavHostController){
    val userData by logInViewModel.fetchData.collectAsState()
    val historyData by ingredientViewModel.historyData.collectAsState()
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(Unit){
        ingredientViewModel.getHistory(userData.email)
        ingredientViewModel.setIngredientDetailsBackToEmpty()
    }

    BackHandler {
        navController.navigate("home")
    }

Box(
    Modifier
        .fillMaxSize()
        .background(color = Color(0XFFf5efef))) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp, top = 70.dp, bottom = 30.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(70.dp)
                        .clip(shape = RoundedCornerShape(35.dp))
                        .background(color = Color.Black, shape = RoundedCornerShape(35.dp))) {
                    Image(painter = painterResource(id = R.drawable.profile), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column() {
                    Text(text = "Hi,", fontSize = 20.sp, fontWeight = FontWeight.Medium,  color = Color(0XFF5e6a73))
                    Text(text = userData.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0XFF5e6a73))
                }

            }
            Icon(imageVector = Icons.Filled.Logout, contentDescription = "", tint = Color.Red, modifier = Modifier
                .size(30.dp)
                .clickable {
                    logInViewModel.logout()
                })
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "History", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        if (historyData.isNotEmpty()){
            LazyColumn(contentPadding = PaddingValues(top = 5.dp)){
                items(historyData){
                    item ->   Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)){
                    Column {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
                            .clickable {
                                coroutine.launch {
                                    ingredientViewModel.tempName.value = item.name
                                    ingredientViewModel.filteredData(ingredientViewModel.tempName.value)
                                    ingredientViewModel.searchIngredientsInFirebase(
                                        ingredientViewModel.historyDetails.value!!.result
                                    )
                                    navController.navigate("detailHistory")
                                }

                            }
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp)), contentAlignment = Alignment.CenterStart)
                        {
                            Text(text = item.name, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        } else  {
            Box {

            }
        }
    }
}
}