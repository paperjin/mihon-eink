package eu.kanade.presentation.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.library.LibraryItem
import kotlinx.coroutines.launch
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.library.model.LibraryDisplayMode
import tachiyomi.domain.library.model.LibraryManga
import kotlin.math.ceil
import kotlin.math.max

/**
 * Paginated library view for E-Ink devices.
 * Shows a fixed number of items per page with navigation arrows.
 */
@Composable
fun PaginatedLibraryView(
    items: List<LibraryItem>,
    contentPadding: PaddingValues,
    selection: Set<Long>,
    displayMode: LibraryDisplayMode,
    columns: Int,
    searchQuery: String?,
    onGlobalSearchClicked: () -> Unit,
    onClickManga: (LibraryManga) -> Unit,
    onLongClickManga: (LibraryManga) -> Unit,
    onClickContinueReading: ((LibraryManga) -> Unit)?,
    itemsPerPage: Int = 12,
) {
    val scope = rememberCoroutineScope()
    val itemsForCurrentCategory = remember(items) { items }

    // Calculate total pages
    val totalPages = if (itemsForCurrentCategory.isEmpty()) {
        1
    } else {
        max(1, ceil(itemsForCurrentCategory.size.toFloat() / itemsPerPage).toInt())
    }

    val pagerState = remember(items.size, itemsPerPage) {
        PagerState(pageCount = totalPages)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        // Main content area with pagination
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) { page ->
            val startIndex = page * itemsPerPage
            val endIndex = minOf(startIndex + itemsPerPage, itemsForCurrentCategory.size)
            val pageItems = itemsForCurrentCategory.subList(startIndex, endIndex)

            if (pageItems.isEmpty()) {
                PaginatedLibraryEmptyScreen(
                    searchQuery = searchQuery,
                    onGlobalSearchClicked = onGlobalSearchClicked,
                )
            } else {
                // Render items using the appropriate grid/list based on display mode
                PaginatedLibraryGrid(
                    items = pageItems,
                    displayMode = displayMode,
                    columns = if (columns > 0) columns else 3,
                    selection = selection,
                    onClickManga = onClickManga,
                    onLongClickManga = onLongClickManga,
                    onClickContinueReading = onClickContinueReading,
                )
            }
        }

        // Bottom navigation bar with arrows and page indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Previous page button
            IconButton(
                onClick = {
                    scope.launch {
                        val targetPage = max(0, pagerState.currentPage - 1)
                        pagerState.animateScrollToPage(targetPage)
                    }
                },
                enabled = pagerState.currentPage > 0,
            ) {
                Text("◀", style = MaterialTheme.typography.titleMedium)
            }

            // Page indicator
            Text(
                text = "${pagerState.currentPage + 1} / $totalPages",
                style = MaterialTheme.typography.bodyMedium,
            )

            // Next page button
            IconButton(
                onClick = {
                    scope.launch {
                        val targetPage = minOf(totalPages - 1, pagerState.currentPage + 1)
                        pagerState.animateScrollToPage(targetPage)
                    }
                },
                enabled = pagerState.currentPage < totalPages - 1,
            ) {
                Text("▶", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun PaginatedLibraryEmptyScreen(
    searchQuery: String?,
    onGlobalSearchClicked: () -> Unit,
) {
    // Simple empty screen for paginated view
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (!searchQuery.isNullOrEmpty()) {
            Text("No results found")
        } else {
            Text("No manga in library")
        }
    }
}

@Composable
private fun PaginatedLibraryGrid(
    items: List<LibraryItem>,
    displayMode: LibraryDisplayMode,
    columns: Int,
    selection: Set<Long>,
    onClickManga: (LibraryManga) -> Unit,
    onLongClickManga: (LibraryManga) -> Unit,
    onClickContinueReading: ((LibraryManga) -> Unit)?,
) {
    // Simple vertical layout for paginated items
    // In a full implementation, this would use the appropriate grid layout
    // Based on displayMode (CompactGrid, CoverOnlyGrid, ComfortableGrid, or List)
    
    val isLandscape = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val actualColumns = if (isLandscape) max(columns, 4) else columns

    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            when (item) {
                is LibraryItem.Manga -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = item.libraryManga.manga.title,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}
