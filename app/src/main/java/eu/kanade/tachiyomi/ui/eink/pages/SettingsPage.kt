package eu.kanade.tachiyomi.ui.eink.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.NavigationEvent

/**
 * Settings page - tabbed interface for settings
 */
@Composable
fun SettingsPage(
    currentPage: String = "reader",
    onNavigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Tab bar
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Reader", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Download", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Network", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Settings area for ${currentPage} tab",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation hints
        Text(
            text = "←→: Tabs | ↑↓: Options | ⏎: Toggle | ESC: Back",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
