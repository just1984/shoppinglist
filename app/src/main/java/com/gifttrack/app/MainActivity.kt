package com.gifttrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gifttrack.app.navigation.GiftTrackBottomBar
import com.gifttrack.app.navigation.GiftTrackNavHost
import com.gifttrack.app.ui.theme.GiftTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GiftTrackTheme {
                GiftTrackApp()
            }
        }
    }
}

@Composable
fun GiftTrackApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            GiftTrackBottomBar(navController = navController)
        }
    ) { innerPadding ->
        GiftTrackNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
