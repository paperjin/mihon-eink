<div align="center">

<a href="https://mihon.app">
    <img src="./.github/assets/logo.png" alt="Mihon logo" title="Mihon logo" width="80"/>
</a>

# mihon-eink

### E-Ink Optimized Manga Reader
A fork of [Mihon](https://github.com/mihonapp/mihon) optimized for E-Ink Android devices (e.g., Onyx Boox, Hisense, reMarkable, Musnap).

**Discover and read manga, webtoons, comics, and more – easier than ever on E-Ink displays.**

[![Discord server](https://img.shields.io/discord/1195734228319617024.svg?label=&labelColor=6A7EC2&color=7389D8&logo=discord&logoColor=FFFFFF)](https://discord.gg/mihon)
[![GitHub downloads](https://img.shields.io/github/downloads/paperjin/mihon-eink/total?label=downloads&labelColor=27303D&color=0D1117&logo=github&logoColor=FFFFFF&style=flat)](https://github.com/paperjin/mihon-eink/releases)

[![Build Status](https://img.shields.io/github/actions/workflow/status/paperjin/mihon-eink/build.yml?labelColor=27303D)](https://github.com/paperjin/mihon-eink/actions)
[![License: Apache-2.0](https://img.shields.io/github/license/paperjin/mihon-eink?labelColor=27303D&color=0877d2)](/LICENSE)

## Download

[![mihon-eink Latest](https://img.shields.io/github/v/release/paperjin/mihon-eink.svg?maxAge=3600&label=Latest&labelColor=06599d&color=043b69)](https://github.com/paperjin/mihon-eink/releases)

*Requires Android 8.0 or higher.*

---

## ✨ E-Ink Features

### What makes mihon-eink different?

| Feature | Description |
|---------|-------------|
| **📊 Status Overlay** | Bottom-left corner overlay showing time, battery %, WiFi status, and page count – always visible while reading |
| **🎨 Monochrome Theme** | High-contrast black/gray UI theme (set as default) – easy on E-Ink displays |
| **⚡ Instant Transitions** | Zero-duration animations for page turns and UI changes – no fade or slide effects |
| **🖼️ E-Ink Image Rendering** | Dithering + bitmap filtering always enabled – smoother gradients on 1-bit displays |
| **📄 Page Flashing** | Full-page flash on chapter turns to reduce ghosting (configurable interval) |
| **⚫ Grayscale Toggle** | Optional grayscale rendering for images |
| **🔒 Privacy-First** | Google Analytics + Crashlytics **disabled by default** – no tracking unless you opt in |
| **📦 Offline-First** | Local reading, backup/restore, no cloud dependency |

---

## 📚 Features

<div align="left">

### Core Reading Experience
- Local reading of content (CBZ, CBR, ZIP, RAR, 7z, EPUB, PDF)
- Configurable reader with multiple viewers (pager, long strip, continuous)
- Reading directions: right-to-left, left-to-right, vertical, webtoon
- Page flash on turn for E-Ink ghosting reduction
- Grayscale and high-contrast rendering options

### Library Management
- Categories to organize your library
- Smart update predictions for release dates
- Batch operations: mark read/unread, download, delete
- Duplicate detection and migration tools

### Tracking Support
- [MyAnimeList](https://myanimelist.net/), [AniList](https://anilist.co/), [Kitsu](https://kitsu.app/)
- [MangaUpdates](https://mangaupdates.com), [Shikimori](https://shikimori.one), [Bangumi](https://bgm.tv/)
- Private tracking support for AniList, Bangumi, Kitsu
- Auto-sync on chapter read

### Themes & Appearance
- **Monochrome** (default – E-Ink optimized)
- Material You (Monet) dynamic theming
- Catppuccin, Nord, Lavender, Tidal Wave, and more
- Light/dark mode toggle

### Advanced Features
- Schedule library updates
- Backup/restore to local or cloud storage
- Reader notes per manga
- Description markdown support
- Reader status overlay (time, battery, WiFi, page count)

</div>

---

## 🛠️ Build from Source

### Prerequisites

- **Java 17** (Temurin recommended)
- **Android SDK** (API 34+)
- **Android NDK** (optional, for native deps)
- **Git**

### Quick Build (Linux/WSL)

```bash
# Clone the repo
git clone https://github.com/paperjin/mihon-eink
cd mihon-eink

# Set up environment
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$JAVA_HOME/bin:$PATH

# Build debug APK
./gradlew assembleDebug

# Install to connected device
adb install -r app/build/outputs/apk/debug/app-arm64-v8a-debug.apk
```

### Local Properties

Create `local.properties` in the project root:

```properties
sdk.dir=/home/youruser/Android/Sdk
```

---

## 📋 Roadmap & TODO

### ✅ Completed
- Status Overlay (time, battery, WiFi, page count)
- Monochrome theme as default
- Analytics + Crashlytics disabled by default
- E-Ink dithering + bitmap filtering always enabled
- **Volume Key Debounce** (600ms) - prevents double-presses on e-ink devices
- **Settings Pagination** - Paginated settings with inline preferences per category, volume key navigation

### 🏗️ In Progress
- **Library Pagination** - Replace infinite scroll with paginated views + volume key navigation (Vol Up/Down to turn pages, arrow buttons, configurable items per page)

### ❌ Future / Backlog
- **De-Scroll the UI** - Replace ALL scrolling with pages/buttons:
  - ~~Settings screens~~ → ✅ **DONE** - Paginated with volume key navigation
  - Browse/Sources → Paginated list
  - History → Paginated view
  - Updates tab → Paginated chapters
  - Manga details → Page through chapters
  - Reader settings → Multi-page dialog
- **E-Ink Optimizations** - Faster refresh rates, configurable animation durations, optional full-screen flash between UI changes
- **Hardcoded Source Research** - Komanga compatibility, local file sources

---

## 🤝 Contributing

[Code of conduct](./CODE_OF_CONDUCT.md) · [Contributing guide](./CONTRIBUTING.md)

Pull requests are welcome! For major changes, please open an issue first.

See the [Roadmap & TODO](#-roadmap--todo) section above for areas we'd love help on.

---

## 📄 License

```
Copyright © 2015 Javier Tomás
Copyright © 2024 Mihon Open Source Project
Copyright © 2026 mihon-eink contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### Disclaimer

The developer(s) of this application does not have any affiliation with the content providers available, and this application hosts zero content.

---

<div align="center">

**Happy reading!** 📚🦙

</div>
