package eu.kanade.tachiyomi.ui.eink

import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.ui.eink.model.*

/**
 * Main activity for Mihon E-Ink frontend.
 * Uses page-based navigation instead of scrolling.
 * Keyboard-focused interface for e-ink devices.
 */
@OptIn(ExperimentalComposeUiApi::class)
class EinkMainActivity : AppCompatActivity() {
    
    private val navigationController = NavigationController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MihonEinkTheme {
                val navigationState = remember { navigationController.getState() }
                
                // Handle keyboard input
                LaunchedEffect(key1 = true) {
                    handleKeyboardInput()
                }
                
                EinkNavigation(navigationState = navigationState)
            }
        }
    }
    
    @OptIn(ExperimentalComposeUiApi::class)
    private fun handleKeyboardInput() {
        val currentActivity = this
        currentActivity.window.decorView.viewTreeObserver.addOnUnhandledKeyListener { v, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        navigationController.decrementSelection()
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        navigationController.incrementSelection()
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        navigationController.goToPreviousPage()
                        true
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        navigationController.goToNextPage()
                        true
                    }
                    KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_SPACE -> {
                        // Open selected item
                        true
                    }
                    KeyEvent.KEYCODE_ESCAPE -> {
                        navigationController.navigate(eu.kanade.tachiyomi.ui.eink.model.NavigationEvent.GoBack)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    }
}

@Composable
fun MihonEinkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors(
            primary = Colors.Default.primary,
            onPrimary = Colors.Default.onPrimary,
            primaryContainer = Colors.Default.primaryContainer,
            onPrimaryContainer = Colors.Default.onPrimaryContainer,
            secondary = Colors.Default.secondary,
            onSecondary = Colors.Default.onSecondary,
            secondaryContainer = Colors.Default.secondaryContainer,
            onSecondaryContainer = Colors.Default.onSecondaryContainer,
            tertiary = Colors.Default.tertiary,
            onTertiary = Colors.Default.onTertiary,
            tertiaryContainer = Colors.Default.tertiaryContainer,
            onTertiaryContainer = Colors.Default.onTertiaryContainer,
            background = Colors.Default.background,
            onBackground = Colors.Default.onBackground,
            surface = Colors.Default.surface,
            onSurface = Colors.Default.onSurface,
            surfaceVariant = Colors.Default.surfaceVariant,
            onSurfaceVariant = Colors.Default.onSurfaceVariant,
            surfaceTint = Colors.Default.surfaceTint,
            inverseSurface = Colors.Default.inverseSurface,
            inverseOnSurface = Colors.Default.inverseOnSurface,
            outline = Colors.Default.outline,
            outlineVariant = Colors.Default.outlineVariant,
            scrim = Colors.Default.scrim,
            surfaceBright = Colors.Default.surfaceBright,
            surfaceDim = Colors.Default.surfaceDim,
            surfaceContainer = Colors.Default.surfaceContainer,
            surfaceContainerHigh = Colors.Default.surfaceContainerHigh,
            surfaceContainerHighest = Colors.Default.surfaceContainerHighest,
            surfaceContainerLow = Colors.Default.surfaceContainerLow,
            surfaceContainerLowest = Colors.Default.surfaceContainerLowest,
        ),
        content = content
    )
}

@Composable
fun EinkNavigation(navigationState: PageState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.Default.surface)
    ) {
        when (navigationState.pageName) {
            "library" -> LibraryPage(
                pageState = navigationState,
                onNavigate = { /* TODO */ }
            )
            "settings" -> SettingsPage(
                currentPage = navigationState.settingsTab,
                onNavigate = { /* TODO */ }
            )
            "search" -> SearchPage(
                onNavigate = { /* TODO */ }
            )
            "source" -> SourceListPage(
                onNavigate = { /* TODO */ }
            )
            "manga" -> MangaPage(
                mangaId = navigationState.settingsTab,
                onNavigate = { /* TODO */ }
            )
        }
    }
}
