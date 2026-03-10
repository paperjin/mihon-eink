# Mihon E-ink Fork - TODO

## Priority Items

### 0. E-ink Page Transitions - ✅ ALREADY DONE (upstream)
- [x] Animations already disabled in `ReaderAppBars.kt`
  - `readerBarsAnimationSpec = tween<IntOffset>(0)` - instant
  - `readerFadeAnimationSpec = tween<Float>(0)` - instant
- [x] No fade/slide effects for app bars
- [x] Check viewer components for page transitions

### 1. Custom Reader Status Bar (Overlay) - ✅ COMPLETE
- [x] Create `ReaderStatusOverlay.kt` - Compose component showing:
  - Current time (HH:MM AM/PM format)
  - Battery level (percentage)
  - WiFi status
  - Page indicator (currentPage / totalPages)
- [x] Add to `ReaderAppBars.kt` overlay component
- [x] Position: Bottom-left corner (above bottom navigation bar)
- [x] Text-only display (no icons) with stroke+text outline effect

**Implementation:**
- Location: `app/src/main/java/eu/kanade/presentation/reader/ReaderStatusOverlay.kt`
- Overlay is shown by default during reading
- Text uses stroke+text pattern matching `ReaderPageIndicator`
- Elements: Time → Divider → Battery % → Divider → WiFi → Divider → Page

### 2. E-ink Page Transitions - ✅ COMPLETE
- [x] Remove/disable all animations in `ReaderAppBars.kt`
  - Set animation durations to 0 (`tween(0)`)
  - Use instant transitions (`slideInVertically`/`slideOutVertically` with 0ms)
- [x] Disable animations in `ReaderStatusOverlay.kt` (fadeIn/fadeOut with 0ms)
- [x] Disable page transition animations in viewer components

## Build Setup

### Java 17 (Required for Gradle 8.14.4)
- Downloaded from Adoptium Temurin
- Location: `/tmp/jdk-17.0.12+7/`

### Android SDK
- Location: `/home/albert/Android/Sdk`
- Platforms: android-34, android-36
- Build tools: 33.0.1, 34.0.0, 35.0.0

### Build Commands
```bash
cd /home/albert/.openclaw/workspace/mihon-eink

export JAVA_HOME=/tmp/jdk-17.0.12+7
export PATH=$JAVA_HOME/bin:$PATH
export ANDROID_HOME=/home/albert/Android/Sdk

# Build APK
./gradlew assembleDebug

# Install to device
adb connect 192.168.0.116:42911
adb install -r app/build/outputs/apk/debug/app-arm64-v8a-debug.apk
```

## Recent Changes

### 2026-03-09
- Overlay now positioned at **bottom-left corner** (was top)
- Converted from icon-based (emojis) to text-only (stroke+text outline)
- Updated `ReaderAppBars.kt` to position overlay above bottom bar
- Created `local.properties` with SDK path

### Commits
- `7402f049f` - docs: Fix typo - removed animations not added
- `0a2f1e0db` - Add custom reader status overlay - bottom position, e-ink optimized
- `4428e97c2` - Add custom reader status overlay with time, battery, page indicator
- `4d49042dc` - Add custom status bar with time, battery, page indicator overlay

## Current Status

| Feature | Status |
|---------|--------|
| E-ink Instant Transitions | ✅ Already implemented (0ms animations) |
| Custom Status Overlay | ✅ Working (bottom-left, text-only with outline) |
| High Contrast Mode | ✅ Implemented (Reader Settings → General) |

## Next Steps

- [ ] Test on actual E-Ink device (if available)
- [ ] Adjust spacing/position if needed
- [ ] Consider adding icons later (requires proper vector drawables)
- [ ] Test with various manga chapters

---

## Build/Test Checklist
- [x] Fork created: `paperjin/mihon-eink`
- [x] Clone: `/home/albert/.openclaw/workspace/mihon-eink`
- [x] Build: `./gradlew assembleDebug` (with Java 17)
- [x] Install to device: `adb install -r app/build/outputs/apk/debug/app-arm64-v8a-debug.apk`
- [x] Push to GitHub
