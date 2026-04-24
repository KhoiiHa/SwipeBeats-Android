# 🎧 SwipeBeats Android

SwipeBeats ist eine Compose-basierte Android-App zur Musik-Discovery über eine Swipe-Experience.

Die App kombiniert eine Tinder-ähnliche Interaktion mit echten iTunes-Daten, Audio-Preview, globalem Mini Player und lokal gespeicherten Favoriten.

👉 Fokus: **bewusste Nutzerinteraktion, klarer Swipe-State und saubere UX-Entscheidungen**

---

## 📸 Screenshots

> Screenshots folgen nach UI-Polish

| Swipe | Explore | Details | Favorites |
|------|--------|--------|----------|
| Coming Soon | Coming Soon | Coming Soon | Coming Soon |

---

## 🚀 Features

### 🎴 Swipe Discovery
- Card Stack mit Swipe-Gesten (Like / Nope)
- Smooth Animationen (Fly-Out + Snap-Back)
- Konsistente UX zwischen Buttons und Gesten

### 🔍 Explore & Suche
- iTunes Search API Integration
- Suche mit Debounce
- Ergebnisliste und Detailansicht

### 🎧 Audio Preview
- ExoPlayer-basierter globaler Player
- Play / Pause Toggle direkt im UI
- **kein Auto-Play → bewusstes Nutzerverhalten**
- Preview startet nur durch Nutzeraktion

### 📊 Mini Player
- global sichtbarer Mini Player
- aktueller Track + Play/Pause-Status
- Play / Pause direkt steuerbar
- bleibt nach bewusst gestartetem Preview sichtbar

### ❤️ Favorites
- Room-basierte lokale Speicherung
- Like im Swipe → automatisch Favorite
- keine Duplikate durch `OnConflictStrategy.REPLACE`

### ⚠️ State Handling
- klare Trennung:
  - Loading
  - Error
  - Empty
  - Content
- Retry-Mechanismus bei API-Fehlern

---

## 🧱 Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Retrofit + Gson
- iTunes Search API
- ExoPlayer (Media3)
- Room
- Kotlin Coroutines

---

## 🧠 Architektur

Das Projekt folgt einem schlanken MVVM-Ansatz mit klarer Trennung von UI, State und Datenzugriff.

```text
com.khoi.swipebeats
├── SwipeBeatsApp.kt      # App-Root + Mini Player + Navigation
├── MainActivity.kt
├── explore/
├── swipe/
├── itunes/
├── player/
├── favorites/
├── favorites/local/
└── ui/theme/
