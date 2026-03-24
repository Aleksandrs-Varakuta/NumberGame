# NumberGame

## Current project structure

```text
NumberGame/
├── app/
│   ├── src/main/
│   │   ├── java/com/rtu/number/game/
│   │   │   ├── App.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   └── repository/
│   │   │   ├── domain/
│   │   │   │   ├── ai/
│   │   │   │   ├── engine/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── rules/
│   │   │   ├── theme/
│   │   │   ├── ui/
│   │   │   │   ├── component/
│   │   │   │   └── screens/
│   │   │   ├── usecase/
│   │   │   └── vm/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── core/
│   ├── common/
│   ├── data/
│   ├── datastore/
│   └── domain/
├── datastore/
├── feature/
│   └── home/
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

## Module overview

### `app`
Main Android application module.

Contains:
- application entry points (`App.kt`, `MainActivity.kt`)
- current UI implementation
- ViewModel layer
- use cases
- core game logic that is still located inside the app module

### `app/data`
Contains data-layer implementations.

Example responsibility:
- repository implementations used by the game session

### `app/domain`
Contains the core business logic of the game.

Subpackages:
- `ai` — AI-related classes and algorithms
- `engine` — game engine abstraction used to apply and evaluate moves
- `model` — game state, settings, moves, status, player identifiers
- `repository` — domain repository contracts
- `rules` — pure game rules, move validation, move generation, and state transitions

### `app/ui`
Compose UI layer.

Subpackages:
- `component` — reusable UI components
- `screens` — screen-level composables such as Home, Settings, and Game

### `app/usecase`
Application actions that connect UI and domain logic.

Examples:
- start a new game
- observe current game state
- apply a move
- make an AI move

### `app/vm`
ViewModel layer for screen state and UI interaction handling.

### `app/theme`
Application theme, colors, and typography for Compose.

## Declared modular structure

## Architecture direction

The current codebase already follows a layered structure inside the `app` module:

- UI (`ui`, `vm`)
- Application layer (`usecase`)
- Domain layer (`domain`)
- Data layer (`data`)

This makes it easier to gradually move shared and feature-specific code into dedicated modules later.

## Current functionality focus

The project currently includes:

- player vs player mode
- player vs AI mode
- configurable game settings
- AI algorithm selection
- adjustable AI depth
- game state management through a ViewModel and use cases
