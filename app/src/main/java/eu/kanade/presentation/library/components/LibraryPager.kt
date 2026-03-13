package eu.kanade.presentation.library.components

import android.content.res.Configuration
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import eu.kanade.core.preference.PreferenceMutableState
import eu.kanade.tachiyomi.ui.library.LibraryItem
import tachiyomi.domain.category.model.Category
import tachiyomi.domain.library.model.LibraryDisplayMode
import tachiyomi.domain.library.model.LibraryManga
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.screens.EmptyScreen
import tachiyomi.presentation.core.util.plus

@Composable
fun LibraryPager(
    state: PagerState,
    contentPadding: PaddingValues,
    hasActiveFilters: Boolean,
    selection: Set<Long>,
    searchQuery: String?,
    onGlobalSearchClicked: () -> Unit,
    getCategoryForPage: (Int) -> Category,
    getDisplayMode: (Int) -> PreferenceMutableState<LibraryDisplayMode>,
    getColumnsForOrientation: (Boolean) -> PreferenceMutableState<Int>,
    getItemsForCategory: (Category) -> List<LibraryItem>,
    onClickManga: (Category, LibraryManga) -> Unit,
    onLongClickManga: (Category, LibraryManga) -> Unit,
    onClickContinueReading: ((LibraryManga) -> Unit)?,
    // E-Ink pagination
    isPaginationEnabled: Boolean = false,
    itemsPerPage: Int = 12,
) {
    val itemsScope = rememberCoroutineScope()
    
    // Volume key handler - will be attached to the pager content
    fun handleVolumeKey(isNext: Boolean, itemsPagerState: PagerState) {
        if (!isPaginationEnabled) return
        itemsScope.launch {
            if (isNext) {
                // Volume down - next page
                if (itemsPagerState.currentPage < itemsPagerState.pageCount - 1) {
                    itemsPagerState.animateScrollToPage(itemsPagerState.currentPage + 1)
                } else {
                    itemsPagerState.animateScrollToPage(0) // wraparound
                }
            } else {
                // Volume up - previous page
                if (itemsPagerState.currentPage > 0) {
                    itemsPagerState.animateScrollToPage(itemsPagerState.currentPage - 1)
                } else {
                    itemsPagerState.animateScrollToPage(itemsPagerState.pageCount - 1) // wraparound
                }
            }
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = state,
        verticalAlignment = Alignment.Top,
    ) { page ->
        if (page !in ((state.currentPage - 1)..(state.currentPage + 1))) {
            // To make sure only one offscreen page is being composed
            return@HorizontalPager
        }
        val category = getCategoryForPage(page)
        val allItems = getItemsForCategory(category)

        if (allItems.isEmpty()) {
            LibraryPagerEmptyScreen(
                searchQuery = searchQuery,
                hasActiveFilters = hasActiveFilters,
                contentPadding = contentPadding,
                onGlobalSearchClicked = onGlobalSearchClicked,
            )
            return@HorizontalPager
        }

        val displayMode by getDisplayMode(page)
        val columns by if (displayMode != LibraryDisplayMode.List) {
            val configuration = LocalConfiguration.current
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            remember(isLandscape) { getColumnsForOrientation(isLandscape) }
        } else {
            remember { mutableIntStateOf(0) }
        }

        val onClickManga: (LibraryManga) -> Unit = { onClickManga(category, it) }
        val onLongClickManga: (LibraryManga) -> Unit = { onLongClickManga(category, it) }

        // E-Ink pagination: chunk items into pages
        val itemsToRender = if (isPaginationEnabled) {
            // Create nested pager for items within this category
            val itemsPagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { (allItems.size + itemsPerPage - 1) / itemsPerPage } // ceiling division
            )
            
            // Focus for volume key capture
            val pagerFocusRequester = remember { FocusRequester() }
            
            // Debounce for volume keys to prevent double-presses
            val lastVolumePressState = remember { mutableLongStateOf(0L) }
            val debounceMs = 300L // 300ms between presses
            
            // Volume key handler - same logic as arrow buttons with debounce
            val volumeKeyModifier = Modifier
                .focusRequester(pagerFocusRequester)
                .focusable()
                .onPreviewKeyEvent { event ->
                    val now = System.currentTimeMillis()
                    if (now - lastVolumePressState.longValue < debounceMs) {
                        // Too soon, consume but don't action
                        return@onPreviewKeyEvent true
                    }
                    
                    when (event.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_VOLUME_DOWN -> {
                            lastVolumePressState.longValue = now
                            // Same as right arrow - next page
                            itemsScope.launch {
                                if (itemsPagerState.currentPage < itemsPagerState.pageCount - 1) {
                                    itemsPagerState.animateScrollToPage(itemsPagerState.currentPage + 1)
                                } else {
                                    itemsPagerState.animateScrollToPage(0) // wraparound
                                }
                            }
                            true
                        }
                        KeyEvent.KEYCODE_VOLUME_UP -> {
                            lastVolumePressState.longValue = now
                            // Same as left arrow - previous page
                            itemsScope.launch {
                                if (itemsPagerState.currentPage > 0) {
                                    itemsPagerState.animateScrollToPage(itemsPagerState.currentPage - 1)
                                } else {
                                    itemsPagerState.animateScrollToPage(itemsPagerState.pageCount - 1) // wraparound
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
            
            // Request focus when this category page is visible
            LaunchedEffect(state.currentPage) {
                if (state.currentPage == page) {
                    pagerFocusRequester.requestFocus()
                }
            }
            
            Column {
                HorizontalPager(
                    modifier = Modifier
                        .weight(1f)
                        .then(volumeKeyModifier),
                    state = itemsPagerState,
                    verticalAlignment = Alignment.Top,
                ) { itemsPage ->
                    val startIdx = itemsPage * itemsPerPage
                    val endIdx = minOf(startIdx + itemsPerPage, allItems.size)
                    val pagedItems = allItems.subList(startIdx, endIdx)
                    
                    when (displayMode) {
                        LibraryDisplayMode.List -> {
                            LibraryList(
                                items = pagedItems,
                                contentPadding = contentPadding,
                                selection = selection,
                                onClick = onClickManga,
                                onLongClick = onLongClickManga,
                                onClickContinueReading = onClickContinueReading,
                                searchQuery = searchQuery,
                                onGlobalSearchClicked = onGlobalSearchClicked,
                            )
                        }
                        LibraryDisplayMode.CompactGrid, LibraryDisplayMode.CoverOnlyGrid -> {
                            LibraryCompactGrid(
                                items = pagedItems,
                                showTitle = displayMode is LibraryDisplayMode.CompactGrid,
                                columns = columns,
                                contentPadding = contentPadding,
                                selection = selection,
                                onClick = onClickManga,
                                onLongClick = onLongClickManga,
                                onClickContinueReading = onClickContinueReading,
                                searchQuery = searchQuery,
                                onGlobalSearchClicked = onGlobalSearchClicked,
                            )
                        }
                        LibraryDisplayMode.ComfortableGrid -> {
                            LibraryComfortableGrid(
                                items = pagedItems,
                                columns = columns,
                                contentPadding = contentPadding,
                                selection = selection,
                                onClick = onClickManga,
                                onLongClick = onLongClickManga,
                                onClickContinueReading = onClickContinueReading,
                                searchQuery = searchQuery,
                                onGlobalSearchClicked = onGlobalSearchClicked,
                            )
                        }
                    }
                }
                
                // Arrow navigation buttons (compact bottom row for E-Ink)
                if (itemsPagerState.pageCount > 1) {
                    val itemsScope = rememberCoroutineScope()
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Left arrow (circle outline, smaller)
                        OutlinedIconButton(
                            onClick = {
                                itemsScope.launch {
                                    if (itemsPagerState.currentPage > 0) {
                                        itemsPagerState.animateScrollToPage(itemsPagerState.currentPage - 1)
                                    } else {
                                        itemsPagerState.animateScrollToPage(itemsPagerState.pageCount - 1)
                                    }
                                }
                            },
                            modifier = Modifier.size(36.dp), // Smaller button
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous page",
                                modifier = Modifier.size(18.dp), // Smaller icon
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        
                        // Page indicator (center, smaller)
                        androidx.compose.material3.Text(
                            text = "${itemsPagerState.currentPage + 1} / ${itemsPagerState.pageCount}",
                            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                        )
                        
                        // Right arrow (circle outline, smaller)
                        OutlinedIconButton(
                            onClick = {
                                itemsScope.launch {
                                    if (itemsPagerState.currentPage < itemsPagerState.pageCount - 1) {
                                        itemsPagerState.animateScrollToPage(itemsPagerState.currentPage + 1)
                                    } else {
                                        itemsPagerState.animateScrollToPage(0)
                                    }
                                }
                            },
                            modifier = Modifier.size(36.dp), // Smaller button
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next page",
                                modifier = Modifier.size(18.dp), // Smaller icon
                                tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        } else {
            // Original behavior: render all items
            when (displayMode) {
                LibraryDisplayMode.List -> {
                    LibraryList(
                        items = allItems,
                        contentPadding = contentPadding,
                        selection = selection,
                        onClick = onClickManga,
                        onLongClick = onLongClickManga,
                        onClickContinueReading = onClickContinueReading,
                        searchQuery = searchQuery,
                        onGlobalSearchClicked = onGlobalSearchClicked,
                    )
                }
                LibraryDisplayMode.CompactGrid, LibraryDisplayMode.CoverOnlyGrid -> {
                    LibraryCompactGrid(
                        items = allItems,
                        showTitle = displayMode is LibraryDisplayMode.CompactGrid,
                        columns = columns,
                        contentPadding = contentPadding,
                        selection = selection,
                        onClick = onClickManga,
                        onLongClick = onLongClickManga,
                        onClickContinueReading = onClickContinueReading,
                        searchQuery = searchQuery,
                        onGlobalSearchClicked = onGlobalSearchClicked,
                    )
                }
                LibraryDisplayMode.ComfortableGrid -> {
                    LibraryComfortableGrid(
                        items = allItems,
                        columns = columns,
                        contentPadding = contentPadding,
                        selection = selection,
                        onClick = onClickManga,
                        onLongClick = onLongClickManga,
                        onClickContinueReading = onClickContinueReading,
                        searchQuery = searchQuery,
                        onGlobalSearchClicked = onGlobalSearchClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryPagerEmptyScreen(
    searchQuery: String?,
    hasActiveFilters: Boolean,
    contentPadding: PaddingValues,
    onGlobalSearchClicked: () -> Unit,
) {
    val msg = when {
        !searchQuery.isNullOrEmpty() -> MR.strings.no_results_found
        hasActiveFilters -> MR.strings.error_no_match
        else -> MR.strings.information_no_manga_category
    }

    Column(
        modifier = Modifier
            .padding(contentPadding + PaddingValues(8.dp))
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        if (!searchQuery.isNullOrEmpty()) {
            GlobalSearchItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                searchQuery = searchQuery,
                onClick = onGlobalSearchClicked,
            )
        }

        EmptyScreen(
            stringRes = msg,
            modifier = Modifier.weight(1f),
        )
    }
}
