package com.example.skinsure.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.skinsure.viewModel.IngredientViewModel
import com.example.skinsure.viewModel.IngredientsDetailViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

@Composable
fun DetailHistory(ingredientsDetailViewModel: IngredientsDetailViewModel = viewModel(), ingredientViewModel: IngredientViewModel = viewModel(), navtController: NavHostController) {
    val data by ingredientViewModel.ingredientDetails.collectAsState()
    val historyDetail by ingredientViewModel.historyDetails.collectAsState()
    var imageUrl by remember { mutableStateOf<String?>(null) }
    val storageRef = FirebaseStorage.getInstance().reference
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val fileRef = storageRef.child(historyDetail!!.link)
        fileRef.downloadUrl.addOnSuccessListener { uri ->
            imageUrl = uri.toString()
            isLoading = false
        }.addOnFailureListener {
            imageUrl = null
            isLoading = false
        }
    }

    BackHandler {
        navtController.navigate("profile")
    }


    if (historyDetail == null) {
        Box {

        }
    } else {
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
                                modifier = Modifier.size(50.dp).clickable { navtController.navigate("profile") }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = historyDetail!!.name,
                                color = Color.Black,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(250.dp)
                        ) {
                            if (isLoading){
                                CircularProgressIndicator(
                                    strokeWidth = 4.dp,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.Center),
                                    color = Color.Black
                                )
                            } else
                                AsyncImage(model = imageUrl, contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)

                        }



                        Spacer(modifier = Modifier.height(20.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                if (data.isEmpty()) {
                                    CircularProgressIndicator(
                                        strokeWidth = 4.dp,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .align(Alignment.Center),
                                        color = Color.Black
                                    )
                                } else {
                                    LazyColumn(contentPadding = PaddingValues(top = 5.dp)) {
                                        items(data) { item ->
                                            IngredientListBox(
                                                name = item.IngredientName,
                                                risk = item.RiskLevel,
                                                onBoxClicked = { ingredientsDetailViewModel.tempName.value = item.IngredientName
                                                    navtController.navigate("detailIngredient") })
                                            Spacer(modifier = Modifier.height(5.dp))
                                        }
                                    }

                                }
                            }
                    }
                }
            } else {
                Box(Modifier.fillMaxSize()) {
                    /*CircularProgressIndicator(
                    color = Color.Gray,
                    modifier = Modifier.size(30.dp),
                    strokeWidth = 5.dp
                )*/
                }
            }
        }
    }
}