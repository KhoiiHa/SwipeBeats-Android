# SwipeBeats Android

SwipeBeats ist ein Android-MVP fuer swipe-basierte Musik-Discovery. Die App kombiniert eine Tinder-aehnliche Swipe-Experience fuer Songs mit iTunes-Suche, Audio-Preview, globalem Mini Player und persistenten Favorites.

Der Fokus des Projekts liegt auf einer klaren Compose-UI, einer einfachen MVVM-Struktur und nachvollziehbaren Produktentscheidungen fuer ein mobiles MVP.

## Screenshots

> Platzhalter: Screenshots werden nach dem naechsten UI-Polish ergaenzt.

| Swipe | Explore | Details | Favorites |
| --- | --- | --- | --- |
| Screenshot folgt | Screenshot folgt | Screenshot folgt | Screenshot folgt |

## Features

- Swipe Screen mit Card Stack fuer Musik-Discovery
- Like- und Nope-Interaktionen per Swipe-Geste und Buttons
- Smooth Fly-Out- und Snap-Back-Animationen mit Compose `Animatable`
- Echte Track-Daten ueber die iTunes Search API
- Separater Explore Screen mit Suchfeld und Ergebnisliste
- Track Detail Screen mit Artwork, Metadaten, Favorite-Aktion und Preview-Steuerung
- Globaler PreviewPlayerManager auf Basis von ExoPlayer
- Mini Player im App-Root mit Play/Pause, Dismiss und Statusanzeige
- Bewusster Preview-Start per Nutzeraktion, kein automatisches Audio beim Kartenwechsel
- Room-basierte Favorites
- Like im Swipe Screen speichert den Track automatisch als Favorite
- Loading-, Error-, Empty- und Content-Zustaende im Swipe Flow

## Tech Stack

- Kotlin
- Jetpack Compose
- Android Architecture Components / ViewModel
- Retrofit
- Gson Converter
- iTunes Search API
- ExoPlayer / AndroidX Media3
- Room
- Kotlin Coroutines
- Gradle Kotlin DSL

## Architektur / Projektstruktur

Das Projekt ist bewusst schlank gehalten und folgt einem MVP-tauglichen MVVM-Ansatz. UI, ViewModel-State, API-Zugriff, Player-Logik und Persistence sind getrennt, ohne eine grosse Dependency-Injection- oder Clean-Architecture-Schicht einzufuehren.

```text
com.khoi.swipebeats
├── SwipeBeatsApp.kt          # App-Root, Bottom Navigation, Mini Player, globaler Player
├── MainActivity.kt           # Activity Entry Point
├── explore/                  # Explore Screen, Detail Screen, ExploreViewModel, Track UI model
├── swipe/                    # Swipe Screen und SwipeViewModel
├── itunes/                   # Retrofit API, DTOs, Mapper, Repository
├── player/                   # PreviewPlayerManager / ExoPlayer-Wrapper
├── favorites/                # Favorites UI und globaler Favorites Store
├── favorites/local/          # Room Entity, DAO und Database
└── ui/theme/                 # Compose Theme
```

### Swipe

Der Swipe Flow verwendet `SwipeViewModel` fuer den produktrelevanten State:

- Track-Liste
- aktueller Index
- aktueller Track
- naechster Track
- gelikte Track-IDs
- abgelehnte Track-IDs
- Loading- und Error-State

Der `SwipeScreen` konzentriert sich auf Darstellung, Gesten, Animationen und UI-Events.

### Explore

Der Explore Flow nutzt `ExploreViewModel`, um Suchanfragen gegen die iTunes API auszufuehren. Die UI bildet die Zustaende `Empty`, `Loading`, `Error` und `Content` ab.

### iTunes Data Layer

Das Package `itunes/` enthaelt:

- `ITunesApiService`
- `ITunesSearchResponseDto`
- `ITunesTrackDto`
- `ITunesMappers`
- `ITunesRepository`
- `RetrofitInstance`

DTOs werden ueber einen einfachen Mapper in das bestehende `Track`-Model ueberfuehrt.

### Player

`PreviewPlayerManager` kapselt ExoPlayer und stellt einfache Funktionen fuer Preview-Wiedergabe bereit:

- `playOrToggle(...)`
- `playPreview(...)`
- `pause()`
- `stop()`
- `release()`

Der Manager wird im App-Root erzeugt und an Screens weitergegeben. Dadurch existiert nur eine zentrale Player-Instanz fuer die App.

### Favorites

Favorites werden lokal mit Room gespeichert. Der MVP nutzt `FavoriteTracksStore` als einfache globale Schnittstelle fuer:

- Initialisierung
- Favorite-Status
- Toggle Favorite
- Add Favorite
- Abrufen gespeicherter Favoriten

## Wichtige UX-Entscheidungen

### Kein Auto-Play

Im Swipe Screen startet Audio nicht automatisch, wenn eine neue Karte erscheint. Das verhindert unerwartete Wiedergabe und macht das Verhalten kontrollierbarer.

### Bewusster Preview-Start

Previews starten nur durch eine klare Nutzeraktion, zum Beispiel ueber den Preview-Button auf einer Karte oder im Detail Screen.

### Mini Player mit Status

Der Mini Player sitzt im App-Root und bleibt sichtbar, wenn ein Track aktiv als Preview ausgewaehlt wurde. Er zeigt den aktuellen Track sowie den Zustand:

- `Preview laeuft`
- `Preview pausiert`

### Like speichert Favorite

Ein Like im Swipe Flow speichert den Track automatisch als Favorite. Nope entfernt oder speichert nichts.

## Aktueller Stand

Der aktuelle Stand ist ein funktionsfaehiges Mobile-MVP mit echten API-Daten, Swipe-Interaktion, Audio-Preview und lokaler Persistenz fuer Favorites.

Umgesetzt sind:

- iTunes API Integration
- SwipeViewModel mit Track-State
- Swipe Screen mit echter Datenquelle
- globaler PreviewPlayerManager
- Mini Player im App-Root
- Explore Screen mit Suche
- Track Detail Screen
- Room Favorites
- Like-to-Favorite Integration
- Error und Retry im Swipe Flow

Noch bewusst offen:

- UI-Polish und finale visuelle Gestaltung
- bessere Fehlertexte fuer unterschiedliche API-/Netzwerkfaelle
- Tests fuer Repository, Mapper und ViewModels
- sauberere langfristige State-Quelle fuer Player und Favorites
- Navigation mit stabilerer Backstack-Struktur

## Naechste geplante Schritte

1. UI-Polish fuer Swipe Cards, Mini Player und Empty/Error States
2. Stabilere Player-State-Struktur fuer Mini Player, Detail Screen und Swipe Screen
3. Tests fuer iTunes Mapper, SwipeViewModel und Favorites
4. Favoriten-Flow weiter aufraeumen, ohne die MVP-Struktur zu stark aufzublasen
5. Optional: Navigation Compose einfuehren, sobald der Screen-Flow weiter waechst

## Lernfokus / Portfolio Value

SwipeBeats zeigt zentrale Android-Themen in einem kompakten Produktkontext:

- Compose UI mit Gesten und Animationen
- MVVM-State-Management in einem realen Screen-Flow
- Retrofit-Anbindung an eine externe API
- DTO-zu-Domain-Mapping
- ExoPlayer-Integration mit globalem Player-Konzept
- Room-Persistenz fuer Favorites
- UX-Entscheidungen rund um Audio, Mini Player und bewusste Nutzeraktionen
- inkrementelle MVP-Entwicklung ohne ueberdimensionierte Architektur

Das Projekt eignet sich als Portfolio-Projekt, weil es nicht nur API-Daten anzeigt, sondern mehrere typische Mobile-Themen kombiniert: Interaktion, Animation, Audio, lokale Datenhaltung und saubere Zustandsfuehrung.
