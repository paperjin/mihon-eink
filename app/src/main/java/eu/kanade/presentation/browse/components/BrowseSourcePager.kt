package eu.kanade.presentation.browse.components

import android.view.KeyEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.StateFlow
import mihon.icons.materialsymbols.MaterialSymbols
import mihon.icons.materialsymbols.automirroredrounded.ArrowBack
import mihon.icons.materialsymbols.automirroredrounded.ArrowForward
import tachiyomi.domain.library.model.LibraryDisplayMode
import tachiyomi.domain.manga.model.Manga

/**
 * E-Ink pagination for a source catalog.
 *
 * Mirrors the library pager: a fixed number of items per page, prev/next controls and
 * volume-key navigation with a debounce. Unlike the library (which paginates an already
 * loaded list), the catalog is backed by Paging 3, so the page size is a window over the
 * lazily loaded items and the total page count is unknown until the source reports the
 * end of pagination.
 */
@Composable
fun BrowseSourcePager(
    mangaList: LazyPagingItems<StateFlow<Manga>>,
    columns: GridCells,
    displayMode: LibraryDisplayMode,
    contentPadding: PaddingValues,
    pageItems: Int,
    onMangaClick: (Manga) -> Unit,
    onMangaLongClick: (Manga) -> Unit,
) {
    var page by remember { mutableIntStateOf(0) }
    val pageSize = pageItems.coerceAtLeast(1)
    val startIndex = page * pageSize

    // Ask Paging for the first item of this page when the window isn't loaded yet.
    LaunchedEffect(page) {
        if (mangaList.itemCount <= startIndex) {
            mangaList.get(startIndex)
        }
    }

    val focusRequester = remember { FocusRequester() }
    val lastVolumePress = remember { mutableLongStateOf(0L) }

    val appendState = mangaList.loadState.append
    val endReached = appendState is LoadState.NotLoading && appendState.endOfPaginationReached
    val canNext = !endReached || startIndex + pageSize < mangaList.itemCount
    val canPrev = page > 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                val now = System.currentTimeMillis()
                when (event.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP -> {
                        if (now - lastVolumePress.longValue < 300L) {
                            return@onPreviewKeyEvent true
                        }
                        lastVolumePress.longValue = now
                        if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                            if (canNext) page += 1
                        } else if (canPrev) {
                            page -= 1
                        }
                        true
                    }
                    else -> false
                }
            },
    ) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Box(modifier = Modifier.weight(1f)) {
            when (displayMode) {
                LibraryDisplayMode.ComfortableGrid -> {
                    BrowseSourceComfortableGrid(
                        mangaList = mangaList,
                        columns = columns,
                        contentPadding = contentPadding,
                        onMangaClick = onMangaClick,
                        onMangaLongClick = onMangaLongClick,
                        pageStart = startIndex,
                        pageSize = pageSize,
                    )
                }
                LibraryDisplayMode.List -> {
                    BrowseSourceList(
                        mangaList = mangaList,
                        contentPadding = contentPadding,
                        onMangaClick = onMangaClick,
                        onMangaLongClick = onMangaLongClick,
                        pageStart = startIndex,
                        pageSize = pageSize,
                    )
                }
                LibraryDisplayMode.CompactGrid, LibraryDisplayMode.CoverOnlyGrid -> {
                    BrowseSourceCompactGrid(
                        mangaList = mangaList,
                        columns = columns,
                        contentPadding = contentPadding,
                        onMangaClick = onMangaClick,
                        onMangaLongClick = onMangaLongClick,
                        pageStart = startIndex,
                        pageSize = pageSize,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedIconButton(
                onClick = { if (canPrev) page -= 1 },
                modifier = Modifier.size(36.dp),
                enabled = canPrev,
            ) {
                Icon(
                    imageVector = MaterialSymbols.AutoMirroredRounded.ArrowBack,
                    contentDescription = "Previous page",
                    modifier = Modifier.size(18.dp),
                )
            }

            Text(
                text = "Page ${page + 1}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            OutlinedIconButton(
                onClick = { if (canNext) page += 1 },
                modifier = Modifier.size(36.dp),
                enabled = canNext,
            ) {
                Icon(
                    imageVector = MaterialSymbols.AutoMirroredRounded.ArrowForward,
                    contentDescription = "Next page",
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}
