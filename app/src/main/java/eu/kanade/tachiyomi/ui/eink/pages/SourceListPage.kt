package eu.kanade.tachiyomi.ui.eink.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.*

/**
 * Source list page - lists available sources
 */
@Composable
fun SourceListPage(
    onNavigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sources", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Source list
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "MangaDex", style = MaterialTheme.typography.bodyLarge)
            Text(text = "MangaPill", style = MaterialTheme.typography.bodyLarge)
            Text(text = "WeebCentral", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Bato.to", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Tachiyomi Source", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "↑↓: Select | ⏎: Install | ESC: Back",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
