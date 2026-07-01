# TestSoaint — AGENTS.md

## Project overview

Single-module Android app (Jetpack Compose, Material 3, dynamic color).  
Entrypoint: `app/src/main/java/com/nels/master/testsoaint/MainActivity.kt`  
DI: Hilt (`@HiltAndroidApp` in `TestSoaintApp.kt`).  
Architecture: Clean Architecture (packages: `data/`, `domain/`, `presentation/`, `di/`).

## Key versions

| Tool | Version |
|------|---------|
| Gradle | 9.4.1 (wrapper) |
| AGP | 9.2.1 |
| Kotlin | 2.2.10 |
| KSP | 2.2.10-2.0.2 |
| compileSdk | 36 (minorApiLevel=1) |
| minSdk / targetSdk | 28 / 36 |
| Compose BOM | 2026.02.01 |
| Hilt | 2.60 |
| Room | 2.7.1 |
| Retrofit | 2.11.0 |
| Navigation Compose | 2.8.9 |

## Build & test

```bash
./gradlew assembleDebug              # build
./gradlew test                        # unit tests only
./gradlew connectedAndroidTest        # instrumented (device/emulator)
./gradlew lint                        # lint check
./gradlew :app:dependencies           # dependency tree
```

Unit tests: `app/src/test/` (JUnit 4)  
Instrumented tests: `app/src/androidTest/` (AndroidX Test + Espresso + Compose UI test)

## Config quirks

- **Version catalog**: `gradle/libs.versions.toml` (single source of deps)
- **compileSdk with minorApiLevel**: `compileSdk { version = 36; minorApiLevel = 1 }` (new AGP API)
- **Configuration cache**: enabled via `gradle.properties`
- **KSP + built-in Kotlin**: `android.disallowKotlinSourceSets=false` in `gradle.properties` (required for AGP 9.x + KSP)
- **R8 keep rules**: `app/src/main/keepRules/rules.keep` (new AGP convention, not the legacy path)
- **Dynamic color**: enabled by default; falls back to static palette on Android < 12
- **Java target**: Java 11 (source & target compatibility)
- **Release build**: optimization explicitly disabled

## Architecture

```
com.nels.master.testsoaint/
├── TestSoaintApp.kt              # @HiltAndroidApp
├── di/                           # Hilt modules (Database, Network, Repository)
├── data/                         # Room entities/dao, Retrofit APIs, DTOs, mappers, repos impl
│   ├── local/ (entity, dao, database)
│   ├── remote/ (api, dto)
│   └── repository/
├── domain/                       # Pure Kotlin: models, repository interfaces, use cases
│   ├── model/
│   ├── repository/
│   └── usecase/
└── presentation/                 # Screens + ViewModels
    ├── navigation/ (Screen sealed class, NavGraph)
    ├── login/
    ├── operador/ (menu, crear, locales, remotos)
    ├── supervisor/ (menu, eliminar)
    └── components/
```

## Auth & roles

- Login: POST `/api/auth/login` via Retrofit to mockoon (localhost:3000)
- Mockoon returns a pre-generated JWT with `Rol` claim (`Operador` or `Supervisor`)
- JWT decoded via Base64 (`data/repository/AuthRepositoryImpl.kt`)
- Token stored in `EncryptedSharedPreferences`
- Session restored on app restart via `AuthRepository.getSession()`

**Test credentials** (configured in mockoon):
- `operador` / `123456` → JWT with Rol=Operador
- `supervisor` / `123456` → JWT with Rol=Supervisor

## Role features

| Rol | Opciones |
|-----|----------|
| Operador | 1. Crear registro (Room + POST a mockoon) |
| Operador | 2. Consultar registros locales (Room) |
| Operador | 3. Consultar registros remotos (GET mockoon) |
| Supervisor | 4. Eliminar registro local + recargar lista |

## Mockoon

Export config: `docs/mockoon-export.json` (importar en Mockoon).
- `POST /api/auth/login` — rules para operador/supervisor
- `GET /api/registros` — 5 registros pre-poblados
- `POST /api/registros` — crear registro (usa templates `{{bodyJson}}`)
- Puerto: 3000 (emulador usa `10.0.2.2`)

## Notes

- `enableEdgeToEdge()` in `MainActivity` — scaffold must account for system bar insets
- `rememberSaveable` used in CrearRegistroScreen fields for rotation resilience
- ViewModels with `StateFlow<UiState>` survive rotation via Hilt
- Room schema exported to `app/schemas/`
- Namespace / applicationId: `com.nels.master.testsoaint`
