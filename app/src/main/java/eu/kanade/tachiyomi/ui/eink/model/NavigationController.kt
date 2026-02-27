package eu.kanade.tachiyomi.ui.eink.model

import eu.kanade.tachiyomi.ui.eink.model.NavigationEvent

/**
 * Helper for managing page navigation state
 */
class NavigationController(
    private val initialState: PageState = PageState()
) {
    private var state: PageState = initialState

    fun getState(): PageState = state

    fun navigate(event: NavigationEvent): PageState {
        state = when (event) {
            is NavigationEvent.OpenLibrary -> state.copy(
                pageName = "library",
                currentPage = event.page
            )
            is NavigationEvent.OpenSource -> state.copy(
                pageName = "source",
                settingsTab = event.sourceId
            )
            is NavigationEvent.Search -> state.copy(
                pageName = "search",
                searchQuery = event.query
            )
            is NavigationEvent.OpenSettings -> state.copy(
                pageName = "settings",
                settingsTab = event.tab
            )
            is NavigationEvent.OpenManga -> state.copy(
                pageName = "manga",
                selectedItemIndex = 0
            )
            NavigationEvent.GoBack -> {
                // Return to library on back
                state.copy(pageName = "library")
            }
            NavigationEvent.Quit -> state
        }
        return state
    }

    fun goToNextPage(): PageState {
        if (state.currentPage < state.totalPages - 1) {
            return state.copy(currentPage = state.currentPage + 1)
        }
        return state
    }

    fun goToPreviousPage(): PageState {
        if (state.currentPage > 0) {
            return state.copy(currentPage = state.currentPage - 1)
        }
        return state
    }

    fun incrementSelection(): PageState {
        val newIndex = minOf(state.selectedItemIndex + 1, state.itemsPerPage - 1)
        return state.copy(selectedItemIndex = newIndex)
    }

    fun decrementSelection(): PageState {
        val newIndex = maxOf(state.selectedItemIndex - 1, 0)
        return state.copy(selectedItemIndex = newIndex)
    }
}
