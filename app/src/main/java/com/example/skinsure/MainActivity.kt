package com.example.skinsure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.example.skinsure.navigations.navigation
import com.example.skinsure.screens.DetailIngredients
import com.example.skinsure.screens.IngredientList
import com.example.skinsure.screens.IngredientScanScreen
import com.example.skinsure.screens.Intro
import com.example.skinsure.screens.Scan
import com.example.skinsure.ui.theme.SkinSureTheme
import com.example.skinsure.viewModel.IngredientViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(), Color.Transparent.toArgb()
            ),
        )
        setContent {
            navigation()
        }
    }
}
