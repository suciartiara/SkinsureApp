package com.example.skinsure.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.skinsure.viewModel.IngredientViewModel
import com.example.skinsure.viewModel.IngredientsDetailViewModel
import com.example.skinsure.viewModel.LogInViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientList(navController: NavHostController, ingredientViewModel: IngredientViewModel = viewModel(), ingredientsDetailViewModel: IngredientsDetailViewModel = viewModel(), logInViewModel: LogInViewModel = viewModel()){
        val data by ingredientViewModel.ingredientDetails.collectAsState()
        var productName by remember {
                mutableStateOf("")
        }
        var isSaveBoxOpen by remember {
                mutableStateOf(false)
        }
        val image by ingredientViewModel.imageBitmap.collectAsState()
        val coroutine = rememberCoroutineScope()
        val context = LocalContext.current
        val loading by ingredientViewModel.isLoading.collectAsState()
        val ingredientNames = data.map { it.IngredientName }
        val showSnackbar by remember {
                mutableStateOf(false)
        }
        val status by ingredientViewModel.isSavedResultSuccess.collectAsState()

        LaunchedEffect(status){
                if (status){
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }
        }

        BackHandler {
                ingredientViewModel.setIngredientDetailsBackToEmpty()
                navController.navigate("scan")
        }

        Box(
                Modifier
                        .fillMaxSize()
                        .background(color = Color.White)) {
                Column(
                        Modifier
                                .fillMaxSize()
                                .padding(top = 60.dp, start = 30.dp, end = 30.dp, bottom = 30.dp)) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart){
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Text(text =  "Bahan berbahaya"  , fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }

                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                                Modifier
                                        .border(
                                                width = 2.dp,
                                                color = Color(0XFFfe96c7),
                                                shape = RoundedCornerShape(20.dp)
                                        )
                                        .clickable {
                                                if (isSaveBoxOpen) isSaveBoxOpen =
                                                        false else isSaveBoxOpen = true
                                        }, contentAlignment = Alignment.Center) {
                                Text(text = if(isSaveBoxOpen) "Close" else  "Save Result?", color = Color(0XFFfe96c7), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp))
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier
                                        .size(300.dp)){
                                        image?.let { bitmap ->
                                                Image(
                                                        bitmap = bitmap.asImageBitmap(),
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                                .fillMaxSize(),
                                                        contentScale = ContentScale.Fit
                                                )
                                        }
                                }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier.fillMaxSize()){
                              LazyColumn(contentPadding = PaddingValues(top = 10.dp)){
                                      items(data) { item ->
                                              IngredientListBox(item.IngredientName, onBoxClicked = { ingredientsDetailViewModel.tempName.value = item.IngredientName
                                                                                                    navController.navigate("detailIngredient")
                                                                                                    }, risk = item.RiskLevel)
                                              Spacer(modifier = Modifier.height(10.dp))
                                      }
                              }
                        }

                }
                if(isSaveBoxOpen){
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Box(
                                        Modifier
                                                .width(300.dp)
                                                .shadow(
                                                        elevation = 7.dp,
                                                        shape = RoundedCornerShape(20.dp)
                                                )
                                                .background(
                                                        color = Color(0XFFF5EFEF),
                                                        shape = RoundedCornerShape(20.dp)
                                                ), contentAlignment = Alignment.TopCenter) {
                                        Column(
                                                Modifier
                                                        .padding(vertical = 20.dp, horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(text = "Enter a name to save this scan", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Box(
                                                        Modifier
                                                                .fillMaxWidth()
                                                                .height(60.dp)
                                                                .background(
                                                                        color = Color.Transparent,
                                                                        shape = RoundedCornerShape(
                                                                                10.dp
                                                                        )
                                                                )
                                                                .border(
                                                                        width = 2.dp,
                                                                        color = Color(0XFFfe96c7),
                                                                        shape = RoundedCornerShape(
                                                                                10.dp
                                                                        )
                                                                ), contentAlignment = Alignment.Center) {
                                                        TextField(value = productName, onValueChange = { productName = it },
                                                                modifier = Modifier
                                                                        .width(300.dp)
                                                                        .height(60.dp), colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                                                                ), textStyle = TextStyle(color = Color.Black, fontSize = 13.sp), placeholder = {
                                                                        Text(text = "Product Name", color = Color.Gray, fontSize = 13.sp)
                                                                }
                                                        )
                                                }
                                                Spacer(modifier = Modifier.height(20.dp))
                                                Box(
                                                        Modifier
                                                                .shadow(
                                                                        elevation = 5.dp,
                                                                        shape = RoundedCornerShape(
                                                                                20.dp
                                                                        )
                                                                )
                                                                .clickable {
                                                                        coroutine.launch {
                                                                                ingredientViewModel.saveResult(
                                                                                        uri = ingredientViewModel.saveBitmapToFile(
                                                                                                context = context,
                                                                                                bitmap = image!!
                                                                                        )!!,
                                                                                        email = logInViewModel.fetchData.value.email,
                                                                                        name = productName,
                                                                                        result = ingredientNames
                                                                                )
                                                                        }

                                                                }
                                                                .background(
                                                                        color = Color.White,
                                                                        shape = RoundedCornerShape(
                                                                                20.dp
                                                                        )
                                                                ), contentAlignment = Alignment.Center) {
                                                        if(loading){
                                                                CircularProgressIndicator(strokeWidth = 4.dp, color = Color.Black, modifier = Modifier.size(25.dp))
                                                        } else {
                                                                Text(text = "save", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp))
                                                        }

                                                }
                                        }
                                }
                        }
                } else  {
                        Box(modifier = Modifier)
                }
        }
}

@Composable
fun IngredientListBox(name: String, risk: String, onBoxClicked: ()-> Unit){
        Box(
                Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .shadow(
                                elevation = 7.dp,
                                shape = RoundedCornerShape(10.dp)
                        )
                        .background(
                                color = Color(0XFFF5EFEF),
                                shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                                onBoxClicked()
                        }, contentAlignment = Alignment.Center) {
                Row(
                        Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column() {
                                Text(text = name, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(5.dp))
                                if(risk.equals("Low", ignoreCase = true)){
                                        low(risk = risk)
                                } else if (risk.equals("low to moderate", ignoreCase = true)){
                                        lowToModerate(risk = risk)
                                } else if (risk.equals("moderate", ignoreCase = true)){
                                        moderate(risk = risk)
                                } else if (risk.equals("moderate to high", ignoreCase = true)){
                                        moderateToHigh(risk = risk)
                                } else if (risk.equals("high", ignoreCase = true)){
                                        high(risk = risk)
                                }
                        }
                        Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = "",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                        )
                }
        }
}

@Composable
fun low(risk: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFFbd996e),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = risk)
        }
}

@Composable
fun lowToModerate(risk: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFFbd6e6e),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFFbd6e6e),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = risk)
        }
}

@Composable
fun moderate(risk: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF854b4b),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF854b4b),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF854b4b),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = risk)
        }
}

@Composable
fun moderateToHigh(risk: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF713737),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF713737),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF713737),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0XFF713737),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = risk)
        }
}

@Composable
fun high(risk: String){
        Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0xFFC8463C),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0xFFC8463C),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0xFFC8463C),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0xFFC8463C),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                        Modifier
                                .size(13.dp)
                                .background(
                                        color = Color(0xFFC8463C),
                                        shape = RoundedCornerShape(
                                                15.dp
                                        )
                                ))
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = risk)
        }
}

//0xFFC8463C