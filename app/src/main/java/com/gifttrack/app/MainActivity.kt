package com.gifttrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gifttrack.app.ui.theme.GiftTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GiftTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GiftTrackApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GiftTrackApp(
    modifier: Modifier = Modifier,
    viewModel: com.gifttrack.app.ui.main.MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎁 GiftTrack",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Geschenkplanung & Einkaufsübersicht",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Display Hilt DI status
            Text(
                text = when (uiState) {
                    is com.gifttrack.app.ui.main.MainUiState.Loading -> "✅ Hilt DI: Lädt..."
                    is com.gifttrack.app.ui.main.MainUiState.Empty -> "✅ Hilt DI: Erfolgreich! (Keine Daten)"
                    is com.gifttrack.app.ui.main.MainUiState.Success -> {
                        val orders = (uiState as com.gifttrack.app.ui.main.MainUiState.Success).orders
                        "✅ Hilt DI: Erfolgreich! (${orders.size} Orders)"
                    }
                    is com.gifttrack.app.ui.main.MainUiState.Error -> {
                        val error = (uiState as com.gifttrack.app.ui.main.MainUiState.Error).message
                        "❌ Fehler: $error"
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GiftTrackAppPreview() {
    GiftTrackTheme {
        GiftTrackApp()
    }
}
