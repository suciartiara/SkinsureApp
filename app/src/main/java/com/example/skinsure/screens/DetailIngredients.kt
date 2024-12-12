package com.example.skinsure.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skinsure.viewModel.IngredientViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skinsure.viewModel.IngredientsDetailViewModel

@Composable
fun DetailIngredients(ingredientsDetailViewModel: IngredientsDetailViewModel = viewModel(), navController: NavController) {
    val data by ingredientsDetailViewModel.IngredientsDetail.collectAsState()

    LaunchedEffect(Unit){
        ingredientsDetailViewModel.getIngredientDetail(ingredientsDetailViewModel.tempName.value)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFFF5EFEF))
    ) {
        if (data != null) {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(140.dp)
                        .shadow(elevation = 5.dp)
                        .background(color = Color(0XFFf5efef)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier.size(50.dp).clickable { navController.navigate("list") }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Ingredient Detail",
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = data.IngredientName,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                    ) {
                        Column(Modifier.padding(horizontal = 20.dp, vertical = 30.dp)) {
                            Text(
                                text = "FUNCTIONS",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = data.Function,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "DESCRIPTIONS",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = data.Description,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "RISK LEVEL",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            if(data.RiskLevel.equals("Low", ignoreCase = true)){
                                low(risk = data.RiskLevel)
                            } else if (data.RiskLevel.equals("low to moderate", ignoreCase = true)){
                                lowToModerate(risk = data.RiskLevel)
                            } else if (data.RiskLevel.equals("moderate", ignoreCase = true)){
                                moderate(risk = data.RiskLevel)
                            } else if (data.RiskLevel.equals("moderate to high", ignoreCase = true)){
                                moderateToHigh(risk = data.RiskLevel)
                            } else if (data.RiskLevel.equals("high", ignoreCase = true)){
                                high(risk = data.RiskLevel)
                            }
                        }
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = Color.Gray,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 5.dp
                )
            }
        }
    }
}