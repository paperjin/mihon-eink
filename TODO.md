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

### v0.19.4-eink
- [x] Initial e-ink optimizations

---

## 🏗️ In Progress

### Library Pagination (E-Ink Navigation)
**Goal:** Replace infinite scroll with paginated views + volume key navigation

**Status:** ✅ Volume key debounce implemented! Preferences infrastructure started

**Plan:**
1. ✅ Add library pagination preference (`pref_library_pagination_eink`)
2. ⏳ Modify `LibraryPager` to support pagination mode
3. ✅ ~~Add volume key handlers for page navigation~~ → **Implemented as debounce fix**
4. ⏳ Add arrow buttons (← →) at bottom of library view
5. ⏳ Implement wraparound pagination (page N → page 1, etc.)
6. ⏳ Configurable items per page (default: 12)

**Technical Notes:**
- Use `HorizontalPager` from Compose Foundation
- Integrate with existing `LibraryContent` component
- Volume keys: `KeyEvent.KEYCODE_VOLUME_UP` / `KEYCODE_VOLUME_DOWN` with 600ms debounce
- Consider: Should categories = pages, or should each category have internal pagination?

---

## ❌ Future / Backlog

### De-Scroll the UI
**Goal:** Replace ALL scrolling with pages/buttons for E-Ink friendliness

**Areas to tackle:**
- [ ] **Settings screens** → Tabbed pages or arrow navigation
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
