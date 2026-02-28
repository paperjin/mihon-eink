# Mihon E-Ink Frontend

An e-ink friendly frontend for Mihon (ex-Tachiyomi), designed for devices like Kindles, Kobo, and other e-ink Android tablets.

## Motivation

The standard Mihon app uses Jetpack Compose with:
- Smooth scrolling animations (bad for e-ink)
- Touch gestures (inaccurate on e-ink)
- Complex UI with shadows/gradients (power-intensive)
- No physical keyboard support (arrow keys, Enter, Escape)

This frontend replaces the UI with:
- **Instant page navigation** - click to page, no scrolling
- **Flat design** - grayscale, no shadows or gradients
- **Keyboard support** - arrow keys, Enter, Escape
- **Power efficient** - minimal refresh cycles

## Architecture

```
app/
├── src/main/
│   ├── java/eu/kanade/tachiyomi/ui/eink/
│   │   ├── EinkMainActivity.kt   # Entry point with navigation
│   │   ├── pages/
│   │   │   ├── LibraryPage.kt    # Page-based library
│   │   │   ├── SearchPage.kt     # Page-based search
│   │   │   ├── SettingsPage.kt   # Page-based settings
│   │   │   └── SourceListPage.kt
│   │   └── model/
│   │       ├── PageState.kt      # Navigation state
│   │       └── NavigationEvent.kt
│   └── res/
│       ├── values/strings.xml    # E-ink strings
│       └── values/styles.xml     # Flat e-ink styles
```

## Features

### Library (Page-based)
- Shows manga in grid/list per page
- Up/Down + Page Up/Down navigation
- Accept to open manga, Back to return

### Search (Page-based)
- Text input page
- Results displayed in pages
- Accept to add to library

### Settings (Tabbed Pages)
- Category tabs (reader, download, network)
- Accept to save, Back to cancel

### Sources (List Page)
- Browse sources
- Accept to install, Back to return

## Technical approach

- Keep Mihon's data layer (sources, manga, chapters)
- Replace UI with simple page-based system
- Use Compose but with `Pager` instead of `LazyColumn`
- No animations - instant jumps between pages

## Getting Started

```bash
# Clone and build
git clone https://github.com/paperjin/mihon-eink.git
cd mihon-eink
./gradlew assembleDebug

# Install to device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Design philosophy

1. **Instant navigation** - no smooth scrolling
2. **Flat design** - no shadows or gradients
3. **Keyboard first** - arrowkeys + Enter
4. **Power efficient** - minimal redraws
5. **Simple interactions** - one tap = one action

## Status

✅ **E-ink frontend structure complete**

**Implemented:**
- ✅ Project structure with Gradle
- ✅ EinkMainActivity with keyboard input handling
- ✅ NavigationController for state management
- ✅ Page-based pages: Library, Search, Settings, Sources, Manga
- ✅ Flat e-ink theme (grayscale, no shadows)
- ✅ Keyboard navigation (arrow keys + Enter/Escape)

**To Do:**
- ⏳ Connect to Mihon core data layer
- ⏳ Add real manga source integration
- ⏳ Implement chapter list display
- ⏳ Add reader page integration
- ⏳ Build and test on actual device

**Build status:**
```bash
cd app && ./gradlew assembleDebug
```

## License

Apache-2.0 (same as Mihon)
See LICENSE file
