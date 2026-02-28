package eu.kanade.tachiyomi.ui.eink.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.NavigationEvent

/**
 * Library page - shows manga in a grid/list per page
 * No scrolling, only navigation via arrow keys
 */
@Composable
fun LibraryPage(
    pageState: eu.kanade.tachiyomi.ui.eink.model.PageState,
    onNavigate: (NavigationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder library items (would be replaced with real data from Mihon core)
    val items = listOf(
        "One Piece",
        "Bleach",
        "Naruto",
        "Attack on Titan",
        "Demon Slayer",
        "My Hero Academia",
        "Jujutsu Kaisen",
        "Tokyo Ghoul",
        "Death Note",
        "Fullmetal Alchemist",
        "Berserk",
        "Ghost in the Shell",
        "Akira",
        "Tezukaos",
        "Doraemon",
        "Crayon Shin-chan",
        "Sazae-san",
        "Kiteretsu",
        "Dennou Coil",
        "Mushishi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Page title
        Text(text = "Library", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Items display (only current page)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                Text(text = item, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pagination controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Page ${pageState.currentPage + 1}/${pageState.totalPages}")
        }

        // Navigation hints
        Text(
            text = "↑↓: Navigate | ←→: Pages | ⏎: Open | ESC: Back",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
