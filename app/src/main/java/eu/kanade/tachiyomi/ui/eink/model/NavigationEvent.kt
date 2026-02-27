package eu.kanade.tachiyomi.ui.eink.model

/**
 * Navigation event sent between pages
 */
sealed interface NavigationEvent {
    data class OpenLibrary(val page: Int = 0) : NavigationEvent
    data class OpenSource(val sourceId: String) : NavigationEvent
    data class Search(val query: String) : NavigationEvent
    data class OpenSettings(val tab: String) : NavigationEvent
    data class OpenManga(val mangaId: String) : NavigationEvent
    object GoBack : NavigationEvent
    object Quit : NavigationEvent
}

/**
 * State for page-based navigation
 */
data class PageState(
    val pageName: String = "library",
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val itemsPerPage: Int = 20,
    val selectedItemIndex: Int = 0,
    val searchQuery: String = "",
    val settingsTab: String = "reader"
)
