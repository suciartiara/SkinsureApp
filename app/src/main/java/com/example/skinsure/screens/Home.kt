package com.example.skinsure.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skinsure.R
import com.example.skinsure.viewModel.IngredientViewModel
import com.example.skinsure.viewModel.IngredientsDetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skinsure.viewModel.LogInViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientScanScreen(logInViewModel: LogInViewModel = viewModel(), viewModel: IngredientViewModel = viewModel(), ingredientsDetailViewModel: IngredientsDetailViewModel = viewModel(), navController: NavHostController) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    val searchResults by viewModel.searchResults.collectAsState()


    BackHandler {
    }

    LaunchedEffect(Unit){

    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFfF5EFEF))) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(top = 70.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)
            .fillMaxWidth()) {
            Text(text = "Search or Scan", fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color(0XFF2C1515))
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = Color.White, shape = RoundedCornerShape(7.dp)), contentAlignment = Alignment.Center){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "", tint = Color.Black, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    TextField(value = query, onValueChange = { query = it
                        viewModel.search(it.text)
                    }, modifier = Modifier
                        .weight(1f)
                        .height(55.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        textStyle = TextStyle(color = Color.Black, fontSize = 13.sp), placeholder = {
                            Text(text = "ingredients", color = Color.Gray, fontSize = 13.sp)
                        })
                    //Icon(imageVector = Icons.Filled.Clear, contentDescription = "", tint = Color.Black, modifier = Modifier.size(25.dp))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(530.dp)
                .padding(top = 5.dp)){
                LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 5.dp)){
                    items(searchResults){
                        item ->
                        IngredientListBox(name = item.IngredientName, onBoxClicked = { ingredientsDetailViewModel.tempName.value = item.IngredientName
                            navController.navigate("detailIngredient")
                        }, risk = item.RiskLevel)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Scan ingredient list", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color(0XFF2C1515))
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(35.dp)), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Outlined.Home, contentDescription = "", modifier = Modifier.size(35.dp))
                }
                Box(
                    Modifier
                        .size(90.dp)
                        .clip(shape = RoundedCornerShape(35.dp))
                        .clickable {
                            navController.navigate("scan")
                        }
                        .background(color = Color.White, shape = RoundedCornerShape(45.dp)), contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.scan), contentDescription = "", modifier = Modifier.size(35.dp), contentScale = ContentScale.Fit)
                }
                Box(
                    Modifier
                        .size(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(35.dp))
                        .clickable {
                            navController.navigate("profile")
                        }, contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "", modifier = Modifier.size(35.dp))
                }
            }
        }
    }
}


