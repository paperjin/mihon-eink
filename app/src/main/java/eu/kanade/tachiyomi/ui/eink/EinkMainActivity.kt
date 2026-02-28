package eu.kanade.tachiyomi.ui.eink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.*

/**
 * Main activity for Mihon E-Ink frontend.
 * Uses page-based navigation instead of scrolling.
 * Flat design theme for e-ink devices.
 */
class EinkMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MihonEinkTheme {
                val navigationState = remember { NavigationController().getState() }
                EinkNavigation(navigationState = navigationState)
            }
        }
    }
}

@Composable
fun MihonEinkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}

@Composable
fun EinkNavigation(navigationState: PageState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = "Mihon E-Ink",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Center)
        )
        
        // Navigation hint at bottom
        Text(
            text = "←→: Page | ↑↓: Select | ⏎: Open | ESC: Back",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}
