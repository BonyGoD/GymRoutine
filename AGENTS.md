# AGENTS.md — GymRoutine

Kotlin Multiplatform project (Compose Multiplatform). Targets: **Android** + **iOS**. No Desktop target.

## Module structure

```
:composeApp   — all shared code: UI, domain, data, DI (this is where you work 99% of the time)
:androidApp   — thin Android launcher, depends on :composeApp
iosApp/       — Xcode project, consumes :composeApp as a static XCFramework
```

The Application class is `dev.bonygod.gymroutine.GymRoutineApp` in `composeApp/src/androidMain/`. The `androidApp` module has no Kotlin source files of its own — it is purely a launcher shell.

## Essential setup before building

Two files are gitignored and must exist locally:

**`local.properties`** (repo root) — required for BuildConfig code generation:
```properties
FIREBASE_API_KEY=<firebase-web-api-key>
CLIENT_ID=<google-oauth-client-id>
```
Without these, `BuildConfig.FIREBASE_API_KEY` and `BuildConfig.CLIENT_ID` are empty strings and Koin will inject empty values — auth will silently fail.

**`iosApp/iosApp/GoogleService-Info.plist`** — required for iOS Firebase init. `google-services.json` is already present in `androidApp/` (gitignored but committed).

## Key build commands

```bash
# Android
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:installDebug

# Formatting — run before committing; CI will fail on dirty output
./gradlew spotlessCheck
./gradlew spotlessApply   # auto-fix

# iOS framework (macOS only)
./gradlew :composeApp:assembleXCFramework
```

**JDK 21** is required (Zulu, per CI workflow). Gradle configuration cache is enabled (`gradle.properties`).

## Formatting

Spotless + ktlint applied to all subprojects. Two rules are globally disabled:
- `ktlint_standard_function-naming` — allows `@Composable` PascalCase function names
- `ktlint_standard_no-wildcard-imports`

Always run `spotlessApply` before committing.

## Architecture

**MVI-like**: every feature has a `State/Event/Effect` triad in `ui/interactions/`.
- `StateFlow<XxxState>` — UI state with a default
- `SharedFlow<XxxEffect>` — one-shot side effects (navigation, errors)
- `fun onEvent(XxxEvent)` — UI dispatches sealed events

**State mutations**: `HomeState` and siblings expose named `fun` methods returning `copy(...)` (e.g., `fun setRoutines(...) = copy(...)`). Use the `setState { reducer }` pattern in ViewModels — do not mutate `_state.value` directly.

**Layered per-feature**: `data/` (datasource, mapper, DTO, repository impl) → `domain/` (model, repository interface, usecase, mapper, error) → `ui/` (screen, ViewModel, interactions).

## Dependency injection — Koin 4.1.1

Single module: `composeApp/.../di/AppModule.kt`. No feature-specific submodules.

Use `koinViewModel()` in Compose screens. Use `factory { }` for use cases, `single { }` for repositories and datasources. ViewModels are registered with `viewModel { }`.

`initKoin()` is called from `GymRoutineApp.onCreate()` (Android) and `iOSApp.init()` (iOS via `AppModuleKt.doInitKoin()`). Do not call it again anywhere.

## Firebase

- KMP SDK: `dev.gitlive.firebase` (auth, firestore, crashlytics, analytics)
- Android only: `Firebase.initialize(this)` in `MainActivity.onCreate()` before any Firebase use
- iOS only: `FirebaseApp.configure()` in `AppDelegate`
- Firestore path: `users/{uid}/routines/{routineId}` and `users/{uid}/workoutLogs/{logId}`

## Day/routine scheduling

`Routine.days` is a comma-separated Spanish abbreviation string (e.g., `"LUN,MIÉ,VIE"`). Use `normalizeDayToken()` from `DayUtils.kt` when parsing — it handles both abbreviated (`"LUN"`) and full names (`"LUNES"`). Helper extensions live in `routines/domain/mapper/RoutineMapper.kt`: `List<Routine>.routinesForDay(abbr)` and `hasRoutineForDay(abbr)`.

The `DayOfWeek → Spanish abbr` mapping exists only in `HomeViewModel` as a private extension. If needed elsewhere, duplicate it or extract it to `DayUtils.kt`.

## Testing

No tests exist. There are no test source sets configured anywhere.

## Things that will trip you up

- **iOS framework registration is macOS-gated** — `binaries.framework { }` block in `composeApp/build.gradle.kts` is wrapped in `if (System.getProperty("os.name").contains("Mac"))`. Don't expect iOS targets to resolve on Windows/Linux.
- **CI workflow is gated** — both Android and iOS jobs check `if: github.event.repository.name == 'KMP-App-Template'` and will not run unless that condition is updated.
- **`ads/ui/`**, `common/ui/state/`, `core/database/`, `history/data/domain/`, `profile/data/domain/` are empty stubs — don't wire anything to them yet.
- **AdMob app ID** in `AndroidManifest.xml` is Google's test placeholder (`ca-app-pub-3940256099942544~3347511713`) — replace before release.
- **`SignInKMP`** (`com.github.BonyGoD.SignInKMP:signin-kmp:2.0.0`) is a first-party KMP library from the repo owner, sourced from JitPack.
