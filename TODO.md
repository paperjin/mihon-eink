# mihon-eink TODO & Roadmap

Last Updated: 2026-03-10

---

## ✅ Completed

### v0.19.5-eink (Current)
- [x] Status Overlay (bottom-left: time, battery, WiFi, page count)
- [x] Monochrome theme as default
- [x] Analytics + Crashlytics disabled by default
- [x] App renamed to "mihon-eink"
- [x] E-Ink dithering + bitmap filtering always enabled
- [x] README with full E-Ink feature documentation
- [x] Proper GitHub fork relationship with upstream
- [x] **Volume Key Debounce** - 600ms gate to prevent double-presses on e-ink devices (PagerViewer + WebtoonViewer)
- [x] **Settings Pagination** - Paginated settings with inline preferences, volume key navigation, removed More tab icon
- [x] **Library Pagination** - Pagination preference infrastructure, volume key handlers, debounce support

### v0.19.4-eink
- [x] Initial e-ink optimizations

---

## 🏗️ In Progress

### Library View Enhancements
**Goal:** Complete library pagination UI with arrow buttons and configurable items per page

**Status:** Core infrastructure complete, UI polish remaining

**Remaining:**
- ⏳ Add arrow buttons (← →) at bottom of library view
- ⏳ Implement wraparound pagination (page N → page 1, etc.)
- ⏳ Configurable items per page (default: 12)

**Technical Notes:**
- Use `HorizontalPager` from Compose Foundation
- Volume keys: `KeyEvent.KEYCODE_VOLUME_UP` / `KEYCODE_VOLUME_DOWN` with 600ms debounce
- Consider: Should categories = pages, or should each category have internal pagination?

---

## ❌ Future / Backlog

### De-Scroll the UI
**Goal:** Replace ALL scrolling with pages/buttons for E-Ink friendliness

**Areas to tackle:**
- [x] **Settings screens** → ✅ Paginated with volume key navigation
- [ ] **Browse/Sources** → Paginated list
- [ ] **History** → Paginated view
- [ ] **Updates tab** → Paginated chapters
- [ ] **Manga details** → Page through chapters instead of scroll
- [ ] **Reader settings** → Multi-page dialog

**Notes:**
- Every scroll interaction causes e-ink refresh artifacts
- Buttons/arrows preferred over swipe gestures
- Consider: Should this be global setting or per-screen?

### E-Ink Optimizations
- [ ] Faster refresh rates / screen mode switching
- [ ] Configurable animation durations (or disable entirely)
- [ ] Optional full-screen flash between all UI changes
- [ ] High contrast mode toggle (separate from monochrome theme)

### Komanga/Hardcoded Source Research
- [ ] Investigate Komanga source compatibility
- [ ] Add configurable hardcoded sources (local files, specific URLs)
- [ ] Adjustable settings for source (page size, filters, etc.)

---

## Development Notes

### Build Commands
```bash
cd /home/albert/.openclaw/workspace/mihon-eink-fresh

# Build debug APK
./gradlew assembleDebug

# Install to device via ADB
export ANDROID_HOME=/home/albert/Android/Sdk
$ANDROID_HOME/platform-tools/adb install -r app/build/outputs/apk/debug/app-arm64-v8a-debug.apk

# Connect to device (if needed)
$ANDROID_HOME/platform-tools/adb connect 100.116.36.81:38775
```

### Upstream Sync
```bash
# Fetch upstream Mihon updates
git fetch upstream

# Merge into our fork (preserves our changes)
git merge upstream/main

# OR rebase (puts our commits on top)
git rebase upstream/main

# Push to GitHub
git push origin main
```

---

## Priority Order

1. **Library Pagination** - High impact, core user experience
2. **De-scroll UI** - Medium, improves overall E-Ink feel
3. **Hardcoded source research** - Medium, adds functionality
4. **E-Ink optimizations** - Low (already good, just polish)

---

## Open Questions

- Should pagination be opt-in (setting) or always on for mihon-eink?
- What's the ideal items per page for E-Ink devices? (12? 16? configurable?)
- Should volume keys work everywhere (library, reader, settings) or just library?
- How to handle search within paginated views?

---

**Note:** Keep this file updated as features ship!

---

## Development Workflow

**Rule:** Do NOT push commits until tested on device first!

1. **Make code changes** (spawn coding subagent)
2. **Build debug APK** (gradle assembleDebug)
3. **Install APK to musnap** (adb install)
4. **TEST on device** (wait for Albert to verify)
5. **THEN commit + push** (only after approval)

**Important:** Coding subagents should build AND install APK before reporting completion. Git commits happen after device testing passes.
