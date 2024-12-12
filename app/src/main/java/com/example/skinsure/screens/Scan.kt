package com.example.skinsure.screens

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.skinsure.R
import com.example.skinsure.viewModel.IngredientViewModel

@Composable
fun Scan(navController: NavHostController, ingredientViewModel: IngredientViewModel = viewModel()){
    val context = LocalContext.current
    val loading by ingredientViewModel.isLoading.collectAsState()
    val image by ingredientViewModel.imageBitmap.collectAsState()
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            ingredientViewModel.setImage(bitmap)
        }
    }
    val result by ingredientViewModel.ingredientDetails.collectAsState()

    LaunchedEffect(result){
        if (result.isNotEmpty()){
            navController.navigate("list")
        }
    }

    BackHandler {
        navController.navigate("home")
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color(0XFfF5EFEF))) {
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(350.dp)
                    .offset(y = -40.dp)) {
                image?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .size(120.dp)) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(
                                    color = Color(0XFFC79898),
                                    shape = RoundedCornerShape(topStart = 10.dp)
                                ))
                            Box(modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(
                                    color = Color(0XFFC79898),
                                    shape = RoundedCornerShape(topStart = 10.dp)
                                ))
                        }
                        Box(
                            Modifier
                                .size(120.dp)) {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(
                                    color = Color(0XFFC79898),
                                    shape = RoundedCornerShape(topEnd = 10.dp)
                                ))
                            Row(Modifier.align(Alignment.CenterEnd)) {
                                Box(modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(
                                        color = Color(0XFFC79898),
                                        shape = RoundedCornerShape(topEnd = 10.dp)
                                    ))
                            }

                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .size(120.dp)) {
                            Row(Modifier.align(Alignment.BottomStart)) {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(
                                        color = Color(0XFFC79898),
                                        shape = RoundedCornerShape(bottomStart = 10.dp)
                                    ))
                            }
                            Box(modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight()
                                .background(
                                    color = Color(0XFFC79898),
                                    shape = RoundedCornerShape(bottomStart = 10.dp)
                                ))
                        }
                        Box(
                            Modifier
                                .size(120.dp)) {
                            Row(Modifier.align(Alignment.BottomStart)) {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(
                                        color = Color(0XFFC79898),
                                        shape = RoundedCornerShape(bottomEnd = 10.dp)
                                    ))
                            }
                            Row(Modifier.align(Alignment.CenterEnd)) {
                                Box(modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(
                                        color = Color(0XFFC79898),
                                        shape = RoundedCornerShape(bottomEnd = 10.dp)
                                    ))
                            }

                        }
                    }
                }

            }
            if(loading){
                Box(modifier = Modifier
                    .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(color = Color.Black, strokeWidth = 4.dp, modifier = Modifier.size(25.dp))
                }
            } else {
                Box(modifier = Modifier
                    .shadow(elevation = 7.dp, shape = RoundedCornerShape(20.dp))
                    .clickable {
                        image?.let { bitmap ->
                            ingredientViewModel.extractTextFromImage(bitmap)
                        }
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center){
                    Text(text = "Scan", color = Color(0XFFC79898), fontSize = 20.sp, modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp), fontWeight = FontWeight.Bold)
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp, bottom = 40.dp)
                .align(
                    Alignment.BottomCenter
                ), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(
                Modifier
                    .size(70.dp)
                    .clickable {
                        imageLauncher.launch("image/*")
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(35.dp)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.gallery), contentDescription = "", modifier = Modifier.size(35.dp), contentScale = ContentScale.Fit)
            }
            Box(
                Modifier
                    .size(90.dp)
                    .clip(shape = RoundedCornerShape(35.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(45.dp)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.camera), contentDescription = "", modifier = Modifier.size(35.dp), contentScale = ContentScale.Fit)
            }
            Box(
                Modifier
                    .size(70.dp)
                    .clickable {
                        navController.navigate("home")
                    }
                    .background(color = Color.White, shape = RoundedCornerShape(35.dp)), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "", modifier = Modifier.size(35.dp))
            }
        }
    }
}
