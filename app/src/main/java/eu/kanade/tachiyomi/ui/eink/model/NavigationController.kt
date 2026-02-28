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

/**
 * Helper for managing page navigation state
 */
class NavigationController(
    private val initialState: PageState = PageState()
) {
    private var state: PageState = initialState

    fun getState(): PageState = state

    fun goToNextPage(): PageState {
        if (state.currentPage < state.totalPages - 1) {
            state = state.copy(currentPage = state.currentPage + 1)
        }
        return state
    }

    fun goToPreviousPage(): PageState {
        if (state.currentPage > 0) {
            state = state.copy(currentPage = state.currentPage - 1)
        }
        return state
    }

    fun incrementSelection(): PageState {
        val newIndex = kotlin.math.min(state.selectedItemIndex + 1, state.itemsPerPage - 1)
        state = state.copy(selectedItemIndex = newIndex)
        return state
    }

    fun decrementSelection(): PageState {
        val newIndex = kotlin.math.max(state.selectedItemIndex - 1, 0)
        state = state.copy(selectedItemIndex = newIndex)
        return state
    }
}
