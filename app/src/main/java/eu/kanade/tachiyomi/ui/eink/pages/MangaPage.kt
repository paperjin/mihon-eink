package eu.kanade.tachiyomi.ui.eink.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.*

/**
 * Manga reader page
 */
@Composable
fun MangaPage(
    mangaId: String,
    onNavigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Manga Reader", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Chapter list
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Chapter 1", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Chapter 2", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Chapter 3", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Chapter 4", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Chapter 5", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "↑↓: Select | ⏎: Read | ESC: Back",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
